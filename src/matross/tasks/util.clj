(ns matross.tasks.util
  (:require [matross.tasks.core])
  (:import [matross.tasks.core TaskResult]))

(defn exit-code [{e :exit}]
  (deref e))

(defn exit-ok? [proc]
  (zero? (exit-code proc)))

(defn task-result [succeeded? changed? data]
  (new TaskResult succeeded? changed? data))

(defmacro deftask [type [conn config] & body]
  `(defmethod matross.tasks.core/get-task ~type [spec#]
     (reify
       matross.tasks.core/ITask
       (exec [_# conn#]
         (let [~conn conn#
               ~config spec#]
           ~@body)))))
