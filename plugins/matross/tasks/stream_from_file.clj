(ns matross.tasks.stream-from-file
  (:require [me.raynes.conch.low-level :as sh]
            [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]
            [validateur.validation :refer :all]))

(deftask stream-from-file
  "Write the contents of a file on a remote system, :src, to the given Writer, :dest"

  {:options {:src "remote file path"
             :dest "destination writer"}
   :examples [{:type :stream-from-file
               :src "~/.bashrc"
               :dest 'my-input-stream}]
   :validator (validation-set
               (presence-of :src)
               (presence-of :dest))}

  [conn {:keys [src dest]}]
  (let [cat (str "cat " src)
        proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
    (sh/stream-to proc :out dest)
    (task-result (exit-ok? proc) false proc)))
