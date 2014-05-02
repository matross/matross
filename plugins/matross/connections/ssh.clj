(ns matross.connections.ssh
  (:require [clj-ssh.ssh :as ssh]
            [matross.connections.core :refer [IConnect IRun get-connection]]
            [clojure.set]))


(defn get-ssh-config [conn]
  ;; supported all configuration supported by `ssh -o`
  ;; also supports: :port :username :password
  (let [defaults {:strict-host-key-checking :no}]
    (merge defaults conn)))

(defn get-identity [conn]
  ;; supported keys
  ;; https://github.com/hugoduncan/clj-ssh/blob/0.5.9/src/clj_ssh/ssh.clj#L236
  (let [defaults {:private-key-path "~/.ssh/id_rsa"}]
    (-> conn
        (select-keys [:private-key
                      :public-key
                      :private-key-path
                      :public-key-path
                      :passphrase
                      :identity])
        ((partial merge defaults)))))

(defn create-session [conn]
  "Use the connection definition to create a jsch session"
  (let [agent  (ssh/ssh-agent {})
        host   (:hostname conn)
        config (get-ssh-config conn)]
    (ssh/add-identity agent (get-identity conn))
    (ssh/session agent host config)))

(defn auto-quote [part]
  ;; insanely hacky auto-quote function...
  ;; how sophisticated would this need to be?
  (if (re-find #" " part)
    (str "'" part "'")
    part))

(deftype SSH
  [conf ssh-session]
  IConnect
  (connect [self]
    (or (ssh/connected? ssh-session)
      (do (ssh/connect ssh-session)
          (ssh/connected? ssh-session))))
  (disconnect [self] (ssh/disconnect ssh-session))

  IRun
  (run [self {:keys [cmd in] :as opts}]
    (let [command (clojure.string/join " " (map auto-quote cmd))
          in (or in "")
          {:keys [channel out-stream err-stream]}
            (ssh/ssh-exec ssh-session command in :stream nil)]
      {:out out-stream
       :err err-stream
       :exit (future
               (while (ssh/connected-channel? channel)
                 (Thread/sleep 100))
               (let [exit (.getExitStatus channel)]
                 (ssh/disconnect-channel channel)
                 exit))})))

(defn ssh-connection [spec]
 (let [session (create-session spec)]
    (new SSH spec session)))

(defmethod get-connection :ssh [spec]
  (ssh-connection spec))

(def translations {"HostName" :hostname
                   "User" :username
                   "IdentityFile" :private-key-path
                   "Port" :port
                   "StrictHostKeyChecking" :strict-host-key-checking})

(defn- convert-keys [m]
  (if-let [port (:port m)]
    (assoc m :port (Integer. port))
    m))

(defn translate-ssh-config [s]
  "Convert a single host ssh config file, like the ones generated
   by `vagrant ssh-config` into a map usable by the ssh connection
   plugin"
  (->> s
      (clojure.string/split-lines)
      (map clojure.string/trim-newline)
      (map clojure.string/trim)
      (remove clojure.string/blank?)
      (map #(clojure.string/split %1 #" "))
      (map (fn [parts] [(first parts) (clojure.string/join " " (rest parts))]))
      (into {:type :ssh})
      (#(clojure.set/rename-keys %1 translations))
      (convert-keys)))
