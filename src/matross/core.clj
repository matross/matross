(ns matross.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]
            [matross.connections.core :refer [run!]]
            [matross.model.core :as model])
  (:gen-class))

(def c {:connections {:ssh [{:hostname "localhost"}
                            {:hostname "127.0.0.1"}]
                      }})

(defn -main [& args]
  (let [new-c (model/prepare c)
        derpy-run! (fn [conn] (run! conn {}))
        conns (model/get-connections new-c) ]
    (println (doall (map derpy-run! conns)))))
