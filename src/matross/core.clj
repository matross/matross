(ns matross.core
  (:require [matross.connections.ssh :as s]
            [matross.connections.core :as connection])
  (:gen-class))

(defn -main [& args]
  (let [c (s/ssh-connection {})]
    (connection/connect c)
    (prn (connection/run c {}))
    (connection/disconnect c)))
