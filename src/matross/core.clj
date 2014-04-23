(ns matross.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]
            [matross.connections.core :refer [run!]]
            [matross.model.core :as model])
  (:gen-class))

(def c {:connections [{:type :ssh :hostname "localhost"}
                      {:type :ssh :hostname "127.0.0.1"}
                      {:type :local}]})

(defn -main [& args]
  (let [new-c (model/prepare c)
        derpy-run! (fn [conn] (run! conn {}))]
    (println (doall (map derpy-run! (:connections new-c))))))
