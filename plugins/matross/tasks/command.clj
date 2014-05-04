(ns matross.tasks.command
  (:require [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(deftask command
  "Execute a shell command on the target machine in a normalized environment.

   For example, ``{:command \"echo $VAR\" :env {:VAR \"herp\"}}`` is equivalent
   to running ``/usr/bin/env -i VAR=herp /bin/sh -c 'echo $VAR'`` on the target
   machine."

  {:options {:command "command to run"
              :env "map containing shell environment (optional)"
              :shell "path to shell to use (optional)"}
    :defaults {:shell "/bin/sh" :env {}}
    :examples [{:command "echo $message"
                :env {:message "hello, world!"}}]}

  [conn {:keys [shell command env]
         :or   {shell "/bin/sh"}}]
  (let [key=val (fn [[key val]] (-> (name key) (str "=" val)))
        cmd (concat ["/usr/bin/env" "-i"] (map key=val env) [shell "-c" command])
        proc (run conn {:cmd cmd})]
    (task-result (exit-ok? proc) true proc)))
