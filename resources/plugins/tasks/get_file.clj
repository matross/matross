(ns matross.tasks.get-file
  (:require [matross.connections.core :refer [run]]
            [matross.tasks.core :refer [get-module ITask]]
            [me.raynes.conch.low-level :refer (stream-to)]
            [me.raynes.fs :refer (file exists? expand-home)]))

(defmethod get-module :get-file [type]
  (reify ITask
    (exec [self {conn :remote} {:keys [src dest]}]
      (let [dest-path (expand-home dest)]
        (if-not (exists? dest-path)
          (let [dest-file (file dest-path)
                cat (str "cat " src)
                proc (run conn {:cmd ["/bin/sh" "-c" cat]})]
            (stream-to proc :out dest-file)
            (if (exists? dest-path)
              dest
              :RUNTIME-ERR))
          :DEST-EXISTS)))))
