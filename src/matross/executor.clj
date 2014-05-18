(ns matross.executor
  (:require [matross.util :refer [prepare]]
            [matross.plugins :refer [load-plugins!]]
            [matross.tasks.core :refer [get-task exec]]
            [matross.connections.core :refer [connect disconnect]]
            [matross.connections.debug :refer [debug-connection debug-process]]))

(defn runtime-connection [opts conn]
  (if (get-in opts [:options :debug])
    (debug-connection conn)
    conn))

(defn test-run-connection! [opts conn tasks]
  (connect conn)
  (doseq [task tasks]
    (-> (get-task task)
        (exec conn)
        :data
        debug-process
        prn))
  (disconnect conn))

(defn run! [opts config]
  (let [{:keys [connections tasks]} (prepare opts config)]
    (load-plugins!)
    (doseq [conn connections]
      (let [conn (runtime-connection opts conn)]
        (println "Running against:" conn)
        (test-run-connection! opts conn tasks)
        (println)))
    (shutdown-agents)))
