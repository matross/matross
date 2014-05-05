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
    (run-task {:type :script
               :inline script
               :vars conf
               :env (get-env conf)})))

(def script
"
#!/bin/sh

set -e

if [ $absent = MATROSS ]
then
  rm -r {{ path }}
  exit 0
fi

if [ $directory = MATROSS ]
then
  mkdir -p {{ path }}
fi

if [ $file = MATROSS ]
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

(defn get-env [{:keys [state mode owner group]}]
  (let [matross-key "MATROSS"
        owner
         (cond
           (and owner group) (str owner ":" group)
           group (str ":" group)
           owner owner
           :else nil)]
    {:file      (if (= :file state)      matross-key)
     :directory (if (= :directory state) matross-key)
     :absent    (if (= :absent state)    matross-key)
     :chmod mode
     :chown owner}))
