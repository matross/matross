(ns matross.tasks.core)

(defmulti get-task :type)

(defrecord TaskResult [conf succeeded? changed? data])

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))

(defn task-result [conf succeeded? changed? data]
  (TaskResult. conf succeeded? changed? data))
