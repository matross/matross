(ns matross.tasks.command
  (:require [matross.docs :refer [defdocs]]
            [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(defdocs :command
  {:description "Run a shell command on a remote machine."
   :options {:command "command to run"
             :env "map containing shell environment (optional)"
             :shell "path to shell to use (optional)"}
   :defaults {:shell "/bin/sh" :env {}}
   :examples [{:command "echo $message"
               :env {:message "hello, world!"}}]})

(deftask command [conn {:keys [shell command env]
                         :or   {shell "/bin/sh"}}]
  (let [key=val (fn [[key val]] (-> (name key) (str "=" val)))
        cmd (concat ["/usr/bin/env" "-i"] (map key=val env) [shell "-c" command])
        proc (run conn {:cmd cmd})]
    (task-result (exit-ok? proc) true proc)))
