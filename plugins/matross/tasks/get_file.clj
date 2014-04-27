(ns matross.tasks.get-file
  (:require [me.raynes.conch.low-level :as sh]
            [matross.tasks.util :refer [exit-ok? deftask task-result]]
            [matross.connections.core :refer [run]]))

(deftask :get-file [conn {:keys [src dest]}]
  (let [cat (str "cat " src)
        proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
    (sh/stream-to proc :out dest)
    (task-result (exit-ok? proc) false proc)))
