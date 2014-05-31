(ns matross.executor
  (:require [matross.config :refer [config-resolver]]
            [matross.strata :refer [strata-fifo strata-lifo enable-debug stratum]]
            [matross.util :refer [prepare]]
            [matross.plugins :refer [load-plugins!]]
            [matross.tasks.core :refer [get-task task-defaults exec]]
            [matross.connections.core :refer [connect disconnect]]
            [matross.connections.debug :refer [debug-connection debug-process]]))

(defn runtime-connection [opts conn]
  (if (get-in opts [:options :debug])
    (debug-connection conn)
    conn))


(defn task-strata [spec]
  (-> (strata-lifo)
      (enable-debug)
      (conj (stratum "Hardcoded task default" (task-defaults spec)))
      (conj (stratum "Provided task configuration" spec))))

(defn test-run-connection! [base-config conn tasks]
  (connect conn)
  (reduce-kv (fn [previous k task]
               (let [config (assoc base-config
                              :task {:current (task-strata task)}
                              :previous previous)
                     resolver (config-resolver config)
                     task-instance (get-task (:task/current resolver))
                     result (exec task-instance conn)
                     previous (:data result)]
                 (prn (debug-process previous))
                 previous))
             nil
             tasks)
  (disconnect conn))

(defn prepare-configs [opts config]
  (let [vars (-> (strata-fifo)
                 (conj (:extra-vars (:options opts)))
                 (conj (:vars config)))]
    {:var vars}))

(defn run! [opts config]
  (let [{:keys [connections tasks]} (prepare opts config)]
    (load-plugins!)
    (doseq [conn connections]
      (let [conn (runtime-connection opts conn)]
        (println "Running against:" conn)
        (test-run-connection!
         (prepare-configs opts config)
         conn
         tasks)
        (println)))
    (shutdown-agents)))
