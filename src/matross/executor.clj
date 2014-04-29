(ns matross.executor
  (:require [me.raynes.conch.low-level :refer [stream-to-string]]
            [matross.util :refer [load-plugins! prepare]]
            [matross.tasks.core :refer [get-task exec]]
            [matross.connections.core :refer [connect disconnect]]))

(defn debug-process [{:keys [exit] :as proc}]
  (prn {:exit @exit
        :out (stream-to-string proc :out)
        :err (stream-to-string proc :err)}))

(defn test-run-connection! [opts conn tasks]
  (connect conn)
  (doseq [task tasks]
    (-> (get-task task)
        (exec conn)
        :data debug-process))
  (disconnect conn))

(defn run! [opts config]
  (let [{:keys [connections tasks]} (prepare opts config)]
    (load-plugins!)
    (doseq [conn connections]
      (println "Running against:" conn)
      (test-run-connection! opts conn tasks)
      (println))
    (shutdown-agents)))
