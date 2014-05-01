(ns matross.tasks.template
  (:require [matross.tasks.core :refer [deftask run-task]]
            [clostache.parser :as template]))


(deftask :template [conn {:keys [dest vars file inline]}]

  (comment
    :dest where to put the templated content
    :vars for template substitution
    :file a file to render
    :inline a string to render (optional, instead of :file))

  (let [content (if file (slurp file) inline)
        rendered (template/render content vars)]
    (run-task
     conn
     {:type :stream-to-file
      :dest dest
      :src rendered})))
