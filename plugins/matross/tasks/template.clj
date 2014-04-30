(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask task-result run-task]]
            [matross.connections.core :refer [run exit-ok?]]
            [matross.tasks.put-file :as put]
            [clostache.parser :as template]))

(comment "
:dest where to put the templated content
:file a file to render
:content a string to render
")

(defn get-content [conf]
  (if (:file conf)
    (slurp (:file conf))
    (:content conf)))


(deftask :template [conn {:keys [dest] :as conf}]
  (let [content (get-content conf)
        rendered (template/render content (:vars conf))]
    (run-task
     conn
     {:type :put-file
      :dest dest
      :src rendered})))
