(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]
            [matross.connections.core :refer [run!]]
            [matross.model.core :as model])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :ssh :hostname "127.0.0.1"}
                           {:type :local}]})

(def cli-opts
  [["-h" "--help"]])

(defn -main [& args]
  (let [opts (parse-opts args cli-opts)
        {:keys [connections]} (model/prepare config)]
    (if (get-in opts [:options :help])
      (prn "matross!")
      (doseq [result (map (partial run! config) connections)]
        (prn result)))))
