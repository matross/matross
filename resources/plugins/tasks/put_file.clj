(ns matross.tasks.put-file
  (:require [me.raynes.fs :refer (file exists? expand-home)]
            [matross.tasks.core :refer [get-module ITask]]
            [matross.connections.core :refer [run]])
  (:import [java.io FileInputStream]))

(defmethod get-module :put-file [type]
  (reify ITask
    (exec [self {conn :remote} {:keys [src dest]}]
      (let [src-path (expand-home src)]
        (if (exists? src-path)
          (let [file-stream (-> src-path file FileInputStream.)
                cat (str "cat > " dest "")
                opts {:cmd ["/bin/sh" "-c" cat] :in file-stream}
                {:keys [exit] :as proc} (run conn opts)]
            (if (= @exit 0)
              dest
              :RUNTIME-ERR))
          :MISSING-SRC)))))
