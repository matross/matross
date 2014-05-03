(ns matross.tasks.core)

(defmulti get-task :type)

(defrecord TaskResult [succeeded? changed? data])

(defprotocol ITask
  "How to run tasks"
  (exec [self conn]))

(defn task-result [succeeded? changed? data]
  (TaskResult. succeeded? changed? data))

(defmacro deftask [type [conn config] & body]
  `(defmethod matross.tasks.core/get-task (keyword '~type) [spec#]
     (reify
       matross.tasks.core/ITask
       (exec [_# conn#]
         (let [~conn conn#
               ~config spec#]
           ~@body)))))

(defn run-task [conn conf]
  (exec (get-task conf) conn))
