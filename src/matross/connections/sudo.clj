(ns matross.connections.sudo
  (:require [matross.connections.core :refer [IRun run IConnect connect disconnect]])
  (:import [java.io SequenceInputStream ByteArrayInputStream]))

(defn str-to-stream [& s]
  (-> (apply str s) .getBytes ByteArrayInputStream.))

(defn concat-streams [right left]
  (if-not right
    left
    (new SequenceInputStream left right)))

(defn sudo-command [command user]
  ;; sudo accepts password over stdin but does not strip it
  ;; in all cases of passwordless sudo (cached pw, nopasswd, etc)
  (apply conj ["sudo" "-k" "-S" "-p" "::matross-sudo::" "-u" user "-H" "--"] command))

(deftype SudoConnection
  [runner user password]
  IConnect
  (connect [self] (connect runner))
  (disconnect [self] (disconnect runner))
  IRun
  (run [self opts]
    (let [pw-stream (str-to-stream password "\n")
          opts (-> opts
                   (update-in [:cmd] sudo-command user)
                   (update-in [:in] concat-streams pw-stream))]
      (run runner opts))))

(defn get-sudo-connection [conn {:keys [sudo-user sudo-pass]}]
  (new SudoConnection conn sudo-user sudo-pass))

(defn sudo? [conn] (isa? conn SudoConnection))
(defn verify-sudo-connection! [conn]
  ;; here we can do things like kill the cache
  ;; verify a password is needed, the pass is correct, etc
  (let [proc (run conn {:cmd ["sudo" "-K"]})]
    (when (zero? (-> proc :exit deref))
       conn)))
