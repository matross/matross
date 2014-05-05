(ns matross.tasks.file
  (:require [matross.tasks.core :refer [deftask run-task]]))

(declare script get-env get-vars)

(deftask file
  "Coerces files on the remote filesystem"

  {:options {:path "remote file path"
             :state "one of: ``:file, :directory, :absent``"
             :mode "unix file permissions"
             :owner "file owner"
             :group "file group"}
   :defaults {:state :file}}

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

(defn get-env [{:keys [state mode owner group] :or {state :file}}]
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
