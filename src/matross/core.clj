(ns matross.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]
            [matross.connections.core :refer [run!]])
  (:gen-class))

(defn -main [& args]
  (let [ssh-conn (ssh/ssh-connection {})
        local-conn (local/local-connection {})
        configuration {}]
    (println (run! ssh-conn configuration))
    (println (run! local-conn configuration))))
