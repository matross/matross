(ns matross.connections.vagrant
  (:require [me.raynes.conch :refer [with-programs]]
            [matross.connections.core :refer [IConnect IRun get-connection] :as core]
            [matross.connections.ssh :refer [ssh-connection]]
            [clojure.set]))

(deftype Vagrant [ssh-future]

  IConnect
  (connect [self] (core/connect @ssh-future))
  (disconnect [self] (core/disconnect @ssh-future))

  IRun
  (run [self command]
    (core/run @ssh-future command)))

(def translations {"HostName" :hostname
                   "User" :username
                   "IdentityFile" :private-key-path
                   "Port" :port
                   "StrictHostKeyChecking" :strict-host-key-checking})

(defn- convert-keys [m]
  (if-let [port (:port m)]
    (assoc m :port (Integer. port))
    m))

(defn- translate-ssh-config [s]
  "Convert a single host ssh config file, like the ones generated
   by `vagrant ssh-config` into a map usable by the ssh connection
   plugin"
  (->> s
      (clojure.string/split-lines)
      (map clojure.string/trim)
      (map clojure.string/trim-newline)
      (remove clojure.string/blank?)
      (map #(clojure.string/split %1 #" "))
      (map (fn [parts] [(first parts) (clojure.string/join " " (rest parts))]))
      (into {:type :ssh})
      (#(clojure.set/rename-keys %1 translations))
      (convert-keys)))


(defn vagrant-ssh-connection [spec]
  (with-programs [vagrant]
    (let [ssh-config (vagrant "ssh-config" (:name spec))
          ssh-spec (translate-ssh-config ssh-config)]
      (ssh-connection (merge ssh-spec spec)))))

(defmethod get-connection :vagrant [spec]
  (Vagrant. (future (vagrant-ssh-connection spec))))
