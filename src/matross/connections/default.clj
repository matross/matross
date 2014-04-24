(ns matross.connections.default
  (:require [matross.connections.core :refer [run]]
            [me.raynes.conch.low-level :refer (stream-to)]
            [me.raynes.fs :refer (file exists? expand-home)])
  (:import [java.io FileInputStream]))

(defn put-file [conn src dest]
  (let [src-path (expand-home src)]
    (if (exists? src-path)
      (let [file-stream (-> src-path file FileInputStream.)
            cat (str "'cat > " dest "'")
            opts {:cmd ["/bin/sh" "-c" cat] :in file-stream}
            {:keys [exit]} (run conn opts)]
        (if (= @exit 0)
          dest
          :RUNTIME-ERR))
      :MISSING-SRC)))

(defn get-file [conn src dest]
  (let [dest-path (expand-home dest)]
    (if-not (exists? dest-path)
      (let [dest-file (file dest-path)
            cmd (str "cat " src)
            proc (run conn {:cmd [cmd]})]
        (stream-to proc :out dest-file)
        (if (exists? dest-path)
          dest
          :RUNTIME-ERR))
      :DEST-EXISTS)))
