(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask run-task]]
            [matross.docs :refer [defdocs]]
            [clostache.parser :as template]))

(defdocs :template
  {:description "The template module lets you template files or contents and put them on the remote machine."
   :options {:dest "where to put the templated content"
             :vars "for template substitution"
             :file "a local template file to render"
             :inline "string to render (optional, instead of file)"}
   :examples [{:type :template
               :inline "{{ name }} is cool!"
               :vars {:name "matross"}
               :dest "~/wow"}]})

(deftask :template [conn {:keys [dest vars file inline]}]
  (let [content (if file (slurp file) inline)
        rendered (template/render content vars)]
    (run-task conn
      {:type :stream-to-file
       :dest dest
       :src rendered})))
