(ns matross.tasks.command
  (:require [matross.tasks.util :refer [exit-ok? deftask task-result]]
            [matross.connections.core :refer [run]]))

(defn key=val [[key val]] (str (name key) "=" val))
(defn format-env [env] (map key=val env))

(deftask :command [conn {:keys [command shell env]}]
  (let [shell (or shell "/bin/sh")
        cmd (concat ["/usr/bin/env" "-i"] (format-env env)
                    [shell "-c" command])
        proc (run conn {:cmd cmd})]
    (task-result (exit-ok? proc) true proc)))
