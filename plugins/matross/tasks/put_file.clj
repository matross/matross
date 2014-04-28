(ns matross.tasks.put-file
  (:require [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(deftask :put-file [conn {:keys [src dest]}]
  (let [cat  (str "cat > " dest "")
        proc (run conn {:in src
                        :cmd ["/bin/sh" "-c" cat]})]
    (task-result (exit-ok? proc) true proc)))
