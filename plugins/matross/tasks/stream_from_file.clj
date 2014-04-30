(ns matross.tasks.cat-to-local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(comment
:src remote file to read
:dest input stream to write remote file contents into)

(deftask :stream-from-file [conn {:keys [src dest]}]
  (let [cat (str "cat " src)
        proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
    (sh/stream-to proc :out dest)
    (task-result (exit-ok? proc) false proc)))
