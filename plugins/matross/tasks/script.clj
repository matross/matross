(ns matross.tasks.script
  (:require [matross.tasks.core :refer [deftask run-task]]
            [matross.validators :refer :all]
            [matross.tasks.template :as template]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [validateur.validation :refer :all]))

(defn get-script [conf]
  (or (get conf :inline)
      (slurp (get conf :file))))

(defn template-conf [conf src dest]
  (-> conf
      (assoc :type :template)
      (assoc :inline src)
      (assoc :dest (clojure.string/trim-newline dest))
      (update-in [:vars] #(if (:template conf) %1 {}))))

(deftask script
  "Execute a script :file or :inline content on the target machine,
   optionally (by default) rendering it as a mustache template prior to execution.

   See also ``matross.tasks.command``"

  {:options {:file "a file to render"
             :inline "a string to render (optional, instead of ``:file``)"
             :template "whether or not to template the script"
             :env "environment variables to expose to the script"
             :vars "map of vars for templating (optional)"} 

   :defaults {:template true
               :vars {}
               :env {}}
   :examples [{:type :script
               :file "hello.sh.mustache"
               :vars {:cool "script, yo"}}]
   :validator (validation-set
               (only-one-of [:file :inline])
               (if-set-file-exists :file))}

  [conn conf]
  (with-temp-files conn [script]
    (let [src (get-script conf)
          template-result (run-task conn (template-conf conf src script))
          command (str "chmod +x " script "; " script)]
      (run-task conn {:type :command :command command :env (get conf :env {})}))))
