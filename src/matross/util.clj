(ns matross.util
  (:require [matross.connections.core :refer [get-connection]]
            [matross.connections.sudo :refer [get-sudo-connection]]
            [matross.input :refer [get-passwords]]))

;; junk namespace... needs re-org
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
