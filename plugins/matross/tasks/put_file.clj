(ns matross.tasks.put-file
  (:require [matross.tasks.util :refer [exit-ok? deftask task-result]]
            [matross.connections.core :refer [run]]))

(deftask :put-file [conn {:keys [src dest]}]
  (let [cat  (str "cat > " dest "")
        proc (run conn {:in src
                        :cmd ["/bin/sh" "-c" cat]})]
    (task-result (exit-ok? proc) true proc)))
