(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask run-task]]
            [stencil.core :as mustache]
            [validateur.validation :refer :all]
            [matross.validators :refer :all]))

(deftask template
  "Render the :file or :inline content as a mustache template and write the result to the :dest
file on the target machine."
  {:options {:dest "where to put the templated content"
             :vars "for template substitution"
             :file "a local template file to render"
             :inline "string to render (optional, instead of file)"}
   :examples [{:type :template
               :inline "{{ example }} is cool!"
               :vars {:example "matross"}
               :dest "~/wow"}]
   :validator (validation-set
               (only-one-of :file :inline))}

  [conn {:keys [dest vars file inline]}]

  (let [content (if file (slurp file) inline)
        rendered (mustache/render-string content vars)]
    (run-task conn
              {:type :stream-to-file
               :dest dest
               :src rendered})))
