(ns matross.tasks.core
  (:require [matross.docs :refer [defdocs]]))

(defmulti get-task #(keyword (:type %1)))

(defrecord TaskResult [succeeded? changed? data])

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))

(defn task-result [succeeded? changed? data]
  (TaskResult. succeeded? changed? data))

(defmacro deftask
  "Define a function and register it's name as an implementation of the matross.tasks.core/get-task multimethod.

   The arguments are the same as ``defn``.
   The only expectation is that the first argument will be
   bound to the IRun instance to opereate on and the second
   argument will be bound to the current configuration."

  [& fdecl]

  (let [type (first fdecl)
        param_count (count (some #(if (vector? %1) %1) fdecl))]
    (if (not (= param_count 2))
      (throw (IllegalArgumentException. "Tasks must be a function of two arguments, an IRun for the target machine, and the current configuration.")))
    `(do
       (defn ~@fdecl)

       (alter-meta! #'~type #(assoc %1 :doc-type :task))

       (defdocs (meta #'~type))

       (defmethod matross.tasks.core/get-task (keyword '~type) [spec#]
         (reify
           matross.tasks.core/ITask
           (exec [_# conn#]
             (let [spec# (merge (get (meta #'~type) :defaults {}) spec#)]
               (~type conn# spec#))))))))

(defn run-task [conn conf]
  (exec (get-task conf) conn))
