(ns matross.tasks.command
  (:require [matross.docs :refer [defdocs]]
            [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(comment
:command shell command to run
:shell system shell to use (optional)
:env map of environment (optional))

(defn key=val [map-entry]
  (let [[key val] map-entry
        keyname (name key)]
    (str keyname "=" val)))

(deftask :command [conn {:keys [shell command env]
                         :or   {shell "/bin/sh"}}]
  (let [cmd (concat ["/usr/bin/env" "-i"] (map key=val env) [shell "-c" command])
        proc (run conn {:cmd cmd})]
    (task-result (exit-ok? proc) true proc)))

(defdocs :command
  {:description "Run a shell command on a remote machine."
   :url "https://github.com/matross/matross/blob/master/plugins/matross/tasks/command.clj" 
   :options
     {:command "command to run"
      :env "map containing shell environment (optional)"
      :shell "path to shell to use (optional)"}
   :defaults {:shell "/bin/sh" :env {}}
   :examples [{:command "echo $message"
               :env {:message "hello, world!"}}]})
