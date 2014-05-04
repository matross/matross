(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask run-task]]
            [clostache.parser :as template]))

(deftask template
  "Run the contents of the given file or inline template through a mustache template
   engine and write the result to the desired file on the target machine"

  {:options {:dest "where to put the templated content"
             :vars "for template substitution"
             :file "a local template file to render"
             :inline "string to render (optional, instead of file)"}
   :examples [{:type :template
               :inline "{{ example }} is cool!"
               :vars {:example "matross"}
               :dest "~/wow"}]}

  [conn {:keys [dest vars file inline]}]

  (let [content (if file (slurp file) inline)
        rendered (template/render content vars)]
    (run-task conn
              {:type :stream-to-file
               :dest dest
               :src rendered})))
