(ns matross.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.core :refer [run!]])
  (:gen-class))

(defn -main [& args]
  (let [connection (ssh/ssh-connection {})
        configuration {}]
    (prn (run! connection configuration))))
