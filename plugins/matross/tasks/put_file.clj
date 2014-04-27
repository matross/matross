(ns matross.tasks.put-file
  (:require [matross.tasks.util :refer [exit-ok? deftask task-result]]
            [matross.connections.core :refer [run]]))

(deftask :put-file [conn {:keys [src dest]}]
  (let [cat (str "cat > " dest "")
        opts {:cmd ["/bin/sh" "-c" cat] :in src}
        proc (run conn opts)]
    (task-result (exit-ok? proc) true proc)))
