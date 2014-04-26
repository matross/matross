(ns matross.connections.sudo
  (:require [matross.connections.core :refer [IRun run]])
  (:import [java.io SequenceInputStream ByteArrayInputStream]))

(defn str-to-stream [& s]
  (-> (apply str s) .getBytes ByteArrayInputStream.))

(defn concat-streams [right left]
  (if-not right
    left
    (new SequenceInputStream left right)))

(defn verify-sudo-state [conn]
  ;; here we can do things like kill the cache
  ;; verify a password is needed, the pass is correct, etc
  (let [proc (run conn {:cmd ["sudo" "-K"]})]
    (when (zero? (-> proc :exit deref))
       conn)))

(defn sudo-command [command user]
  ;; sudo accepts password over stdin but does not strip it
  ;; in all cases of passwordless sudo (cached pw, nopasswd, etc)
  (apply conj ["sudo" "-k" "-S" "-p" "::matross-sudo::" "-u" user "-H" "--"] command))

(deftype SudoRunner
  [runner user password]
  IRun
  (run [self opts]
    (let [pw-stream (str-to-stream password "\n")
          opts (-> opts
                   (update-in [:cmd] sudo-command user)
                   (update-in [:in] concat-streams pw-stream))]
      (run runner opts))))

(defn sudo-runner [runner user password]
  ;; expects runner to be connected
  ;; will try to run and kill sudo cache
  (let [runner (new SudoRunner runner user password)]
    (verify-sudo-state runner)))
