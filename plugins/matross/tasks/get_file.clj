(ns matross.tasks.get-file
  (:require [matross.connections.core :refer [run]]
            [matross.tasks.core :refer [task-result get-task ITask]]
            [me.raynes.conch.low-level :refer (stream-to)]))

(deftype GetFile [spec]
  ITask
  (exec [self conn]
    (let [{:keys [src dest]} spec]
        (let [cat (str "cat " src)
              proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
          (stream-to proc :out dest)
          (task-result (= 0 @(:exit proc)) false  proc)))))

(defmethod get-task :get-file [spec]
  (new GetFile spec))
