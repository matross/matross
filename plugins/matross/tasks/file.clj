(ns matross.tasks.file
  (:require [matross.tasks.core :refer [deftask run-task]]
            [validateur.validation :refer :all]))

(declare script get-env get-vars)

(deftask file
  "Ensure that a path on the target filesystem is in the defined state."

  {:options {:path "remote file path"
             :state "one of: ``:file, :directory, :absent``"
             :mode "unix file permissions"
             :owner "file owner"
             :group "file group"}
   :defaults {:state :file}
   :examples [{:type :file :path "/tmp/sensitive-file" :state :absent}
              {:type :file :path "/etc/db/required-directory" :state :directory :owner "root" :group "db" :mode 750}]

   :validator (validation-set
               (presence-of :path)
               (inclusion-of :state :in #{:file :directory :absent}))}

  [conn conf]
  (let [env (get-env conf)]
    (run-task conn {:type :script
                    :inline script
                    :vars conf
                    :env (get-env conf)})))

(def script
"#!/bin/sh

set -e

if [ \"$is_absent\" = true ]
then
  rm -rf {{ path }}
  exit 0
fi

if [ \"$is_directory\" = true ]
then
  mkdir -p {{ path }}
fi

if [ \"$is_file\" = true ]
then
  touch {{ path }}
fi

if [ -n \"$chmod\" ]
then
  chmod $chmod {{ path }} 
fi

if [ -n \"$chown\" ]
then
  chown $chown {{ path }}
fi
")

(defn get-env
  "Convert the task parameters into an Environment map that will be passed
  to the above shell command."

  [{:keys [state mode owner group]}]

  (let [owner
         (cond
           (and owner group) (str owner ":" group)
           group (str ":" group)
           owner owner
           :else nil)]
    {:is_file      (= :file state)
     :is_directory (= :directory state)
     :is_absent    (= :absent state)
     :chmod mode
     :chown owner}))
