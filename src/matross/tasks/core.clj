(ns matross.tasks.core)

(defmulti get-module identity)

(defprotocol ITask
  "How to run tasks"
  (exec [self conns conf]))
