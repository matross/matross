(ns matross.tasks.stream-to-file
  (:require [matross.tasks.core :refer [deftask task-result]]
            [matross.connections.core :refer [run exit-ok?]]
            [validateur.validation :refer :all]))

(deftask stream-to-file
  "Write the contents of the given source input to the desired file on the target machine."

  {:options {:src "source input, string or stream"
             :dest "remote filepath"}
   :examples [{:type :stream-to-file
               :src "alias git=wow"
               :dest "~/.bash_aliases"}]
   :validator (validation-set
               (presence-of :src)
               (presence-of :dest))}

  [conn {:keys [src dest]}]
  (let [cat  (str "cat > " dest "")
        proc (run conn {:in src
                        :cmd ["/bin/sh" "-c" cat]})]
    (task-result (exit-ok? proc) true proc)))
