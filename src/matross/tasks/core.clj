(ns matross.tasks.core)

(defmulti get-task :type)

(defrecord TaskResult [succeeded? changed? data])

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))
