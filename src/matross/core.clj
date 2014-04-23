(ns matross.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]
            [matross.connections.core :refer [run!]]
            [matross.model.core :as model])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :ssh :hostname "127.0.0.1"}
                           {:type :local}]})

(defn -main [& args]
  (let [{:keys [connections]} (model/prepare config)]
    (doseq [result (map (partial run! config) connections)]
      (prn result))))
