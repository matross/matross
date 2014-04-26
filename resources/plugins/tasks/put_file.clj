(ns matross.tasks.put-file
  (:require [me.raynes.fs :refer (file exists? expand-home)]
            [matross.tasks.core :refer [task-result get-task ITask]]
            [matross.connections.core :refer [run]])
  (:import [java.io FileInputStream]))

(deftype PutFile [spec]
  ITask
  (exec [self conn]
    (let [{:keys [src dest]} spec
          src-path (expand-home src)]
      (if (exists? src-path)
        (let [file-stream (-> src-path file FileInputStream.)
              cat (str "cat > " dest "")
              opts {:cmd ["/bin/sh" "-c" cat] :in file-stream}
              {:keys [exit] :as proc} (run conn opts)]
          (task-result (= 0 @exit) true {}))
        (task-result false false {})))))

(defmethod get-task :put-file [spec]
   (new PutFile spec))
