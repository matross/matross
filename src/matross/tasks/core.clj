(ns matross.tasks.core)

(defmulti get-module :type)

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))
