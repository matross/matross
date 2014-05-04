(ns matross.tasks.script
  (:require [matross.tasks.core :refer [deftask run-task]]
            [matross.tasks.template :as template]
            [matross.tasks.temp-file :refer [with-temp-files]]))

(def defaults {:template true
               :vars {}
               :env {}})

(defn get-script [conf]
  (if (:file conf)
    (slurp (:file conf))
    (get conf :inline "")))

(defn template-conf [conf src dest]
  (-> conf
      (assoc :type :template)
      (assoc :inline src)
      (assoc :dest (clojure.string/trim-newline dest))
      (update-in [:vars] #(if (:template conf) %1 {}))))

(deftask script
  "Execute a script file or an inline script on the target machine, optionally running the
   contents through the templating engine prior to transfer.

   See also `matross.tasks.command`"
  [conn conf]
  (comment
    :file a file to render
    :inline a string to render (optional, instead of :file)
    :template boolean whether or not to template the script (default true)
    :env environment variables to expose to the script
    :vars map of vars for templating)
  (with-temp-files conn [script]
    (let [conf (merge defaults conf)
          src (get-script conf)
          final (run-task conn (template-conf conf src script))]
      (run-task conn {:type :command :command (str "chmod +x " script)})
      (run-task conn {:type :command :command script :env (get conf :env {})}))))
