(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask task-result run-task]]
            [matross.connections.core :refer [run exit-ok?]]
            [matross.tasks.put-file :as put]
            [clostache.parser :as template]))

(comment
:dest where to put the templated content
:vars for template substitution
:file a file to render
:content a string to render (optional, instead of :file))

(deftask :template [conn {:keys [file content dest vars]}]
  (let [content (if file (slurp file) content)
        rendered (template/render content vars)]
    (run-task
     conn
     {:type :stream-to-file
      :dest dest
      :src rendered})))
