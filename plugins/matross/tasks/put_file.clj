(ns matross.tasks.put-file
  (:require [matross.tasks.core :refer [task-result get-task ITask]]
            [matross.connections.core :refer [run]])
  (:import [java.io FileInputStream]))

(deftype PutFile [spec]
  ITask
  (exec [self conn]
    (let [{:keys [src dest]} spec]
        (let [cat (str "cat > " dest "")
              opts {:cmd ["/bin/sh" "-c" cat] :in src}
              proc (run conn opts)]
          (task-result (= 0 @(:exit proc)) true proc)))))

(defmethod get-task :put-file [spec]
   (new PutFile spec))
