(ns matross.tasks.stream-from-file
  (:require [me.raynes.conch.low-level :as sh]
            [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]))

(deftask stream-from-file
  "Low level task to stream the contents of a file on the target machine
   to the given writer locally."

  {:options {:src "remote file path"
             :dest "destination writer"}
   :examples [{:type :stream-from-file
               :src "~/.bashrc"
               :dest 'my-input-stream}]}

  [conn {:keys [src dest]}]
  (let [cat (str "cat " src)
        proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
    (sh/stream-to proc :out dest)
    (task-result (exit-ok? proc) false proc)))
