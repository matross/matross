(ns matross.util
  (:require [me.raynes.fs :as fs]
            [cemerick.pomegranate :as pom]
            [matross.connections.core :refer [get-connection]]
            [matross.connections.sudo :refer [get-sudo-connection]]))

;; junk namespace... needs re-org
(defn load-plugins [& dirnames]
  "Loads all the clojure files in a directory"
  (doseq [dir dirnames]
    (doseq [file (fs/find-files dir #".*\.clj")]
      (-> file .getAbsolutePath load-file))))

(defn load-plugins! []
  (let [plugins-dir "plugins"
        dir (partial fs/file plugins-dir "matross")]
    (pom/add-classpath plugins-dir)
    (load-plugins (dir "connections") (dir "tasks"))))

(defn get-sensitive-user-input [prompt]
  (let [console (System/console)
        padded-prompt (str prompt " ")]
    (if console
      (-> console (.readPassword padded-prompt nil) String/valueOf)
      (throw (new Exception "Could not find system console.")))))

;;;; previously in model.core
;;;; runner logic?
(defn get-passwords [opts]
  ;; this needs to happen before we make our connections
  ;; since it can be used to configure our connections
  (let [sudo (get-in opts [:options :ask-sudo-pass])
        pass  (get-in opts [:options :ask-password])]
    {:password (if pass (get-sensitive-user-input "password:"))
     :sudo-pass (if sudo (get-sensitive-user-input "sudo password:"))}))

(defn get-connection-config [opts spec]
  ;; build a config to be used to create a connection
  ;; merge information from user defaults, env vars, etc
  (let [sudo-user (:sudo-user opts)]
    (merge {:sudo-user (or sudo-user "root")
            :sudo (or sudo-user (:sudo-pass opts))}
           (select-keys opts [:password :sudo-pass])
           spec)))

(defn get-sudo?-connection [spec]
  ;; get a connection, optionally wrapping it with sudo configuration
  (let [conn (get-connection spec)]
    (if-not (:sudo spec) conn
      (get-sudo-connection conn spec))))

(defn prepare [opts conf]
  ;; preprocess user provided configuration using
  ;; opts built up from  the cli
  (let [opts (merge (get-passwords opts) opts)
        get-conn-config (partial get-connection-config opts)
        get-connection (comp get-sudo?-connection get-conn-config)]
    (update-in conf [:connections] (partial map get-connection))))
