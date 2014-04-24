(ns matross.connections.default
  (:require [matross.connections.core :refer [run]]
            [me.raynes.conch.low-level :refer (stream-to stream-to-string)]
            [me.raynes.fs :refer (file exists? expand-home)])
  (:import [java.io FileInputStream]))

(defn debug-process [{:keys [exit] :as proc}]
  (prn {:exit @exit
        :out (stream-to-string proc :out)
        :err (stream-to-string proc :err)}))

(defn put-file [conn src dest]
  (let [src-path (expand-home src)]
    (if (exists? src-path)
      (let [file-stream (-> src-path file FileInputStream.)
            cat (str "cat > " dest "")
            opts {:cmd ["/bin/sh" "-c" cat] :in file-stream}
            {:keys [exit] :as proc} (run conn opts)]
        (if (= @exit 0)
          dest
          (debug-process proc)))
      :MISSING-SRC)))

(defn get-file [conn src dest]
  (let [dest-path (expand-home dest)]
    (if-not (exists? dest-path)
      (let [dest-file (file dest-path)
            proc (run conn {:cmd ["/bin/sh" "-c" (str "cat " src)]})]
        (stream-to proc :out dest-file)
        (if (exists? dest-path)
          dest
          (debug-process proc)))
      :DEST-EXISTS)))
