(ns matross.tasks.stream-to-file
  (:require [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(comment
:src input stream or string
:dest remote filepath)

(deftask stream-to-file
  "Write the contents of the given Reader-ish to the desired file on the target machine."
  [conn {:keys [src dest]}]
  (let [cat  (str "cat > " dest "")
        proc (run conn {:in src
                        :cmd ["/bin/sh" "-c" cat]})]
    (task-result (exit-ok? proc) true proc)))
