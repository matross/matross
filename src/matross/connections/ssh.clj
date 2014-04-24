(ns matross.connections.ssh
  (:require [clj-ssh.ssh :as ssh]
            [matross.connections.core :refer [IConnect IRun get-connection]])
  (:import [java.io ByteArrayInputStream PipedInputStream PipedOutputStream]
           [com.jcraft.jsch Session ChannelExec]))

(declare ssh-exec)

(defn get-ssh-config [conn]
  ;; supported all configuration supported by `ssh -o`
  ;; also supports: :port :username :password
  (let [defaults {:strict-host-key-checking :no}]
    (merge defaults conn)))

(defn get-identity [conn]
  ;; supported keys
  ;; https://github.com/hugoduncan/clj-ssh/blob/0.5.9/src/clj_ssh/ssh.clj#L236
  (let [defaults {:private-key-path "~/.ssh/id_rsa"
                  :public-key-path "~/.ssh/id_rsa.pub"}]
    (merge defaults conn)))

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
          opts (dissoc opts :in :cmd)]
      (ssh-exec ssh-session command in opts))))

(defmethod get-connection :ssh [spec]
  (let [session (create-session spec)]
    (new SSH spec session)))

(defn- streams-for-out []
  (let [os (PipedOutputStream.)]
    [os (PipedInputStream. os (int (* 1024 10)))]))

(defn- ssh-exec
  ;; modified version of https://github.com/hugoduncan/clj-ssh/blob/0.5.9/src/clj_ssh/ssh.clj#L624
  [^Session session cmd stdin opts]
  (let [^ChannelExec exec (ssh/open-channel session :exec)
        [os out-stream] (streams-for-out)
        [es err-stream] (streams-for-out)]
    (doto exec
      (.setInputStream
       (if (string? stdin)
         (ByteArrayInputStream. (.getBytes stdin))
         stdin)
       false)
      (.setOutputStream os)
      (.setErrStream es)
      (.setCommand cmd))
    (when (contains? opts :env)
      (doseq [[k v] (:env opts)]
        (.setEnv exec (str k) (str v))))
    (ssh/connect-channel exec)
    {:exit (future
             (while (ssh/connected-channel? exec)
               (Thread/sleep 100))
             (let [exit (.getExitStatus exec)]
               (ssh/disconnect-channel exec)
               (.close os)
               (.close es)
               exit))
     :in stdin
     :out out-stream
     :err err-stream}))
