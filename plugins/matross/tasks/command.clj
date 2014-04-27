(ns matross.tasks.command
  (:require [matross.tasks.util :refer [exit-ok? deftask task-result]]
            [matross.connections.core :refer [run]]))

(defn key=val [map-entry]
  (let [[key val] map-entry
        keyname (name key)]
    (str keyname "=" val)))

(deftask :command [conn {:keys [shell command env]
                         :or   {shell "/bin/sh"}}]
  (let [cmd (concat ["/usr/bin/env" "-i"] (map key=val env) [shell "-c" command])
        proc (run conn {:cmd cmd})]
    (task-result (exit-ok? proc) true proc)))
