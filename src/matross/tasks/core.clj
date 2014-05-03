(ns matross.tasks.core)

(defmulti get-task #(keyword (:type %1)))

(defrecord TaskResult [succeeded? changed? data])

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))

(defn task-result [succeeded? changed? data]
  (TaskResult. succeeded? changed? data))

(defmacro deftask [type [conn config] & body]
  `(do
    (defn ~type [~conn ~config] ~@body)

    (defmethod matross.tasks.core/get-task (keyword '~type) [spec#]
       (reify
         matross.tasks.core/ITask
         (exec [_# conn#]
           (~type conn# spec#))))))

(defn run-task [conn conf]
  (exec (get-task conf) conn))
