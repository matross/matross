(ns matross.connections.core
  (:import [java.io SequenceInputStream ByteArrayInputStream]))

(defprotocol IConnect
  "Interface for matross connections"

  (connect [self]
    "connect to a machine")

  (disconnect [self]
    "disconnect from a machine"))

(defprotocol IRun
  (run [self opts]
    "execute a command on a machine"))

(defmulti get-connection
  "get a connection instance of a specific type" :type)

(defn str-to-stream [& s]
  (-> (apply str s) .getBytes ByteArrayInputStream.))

(defn concat-streams [right left]
  (if-not right
    left
    (new SequenceInputStream left right)))

(defn sudo-command [command user]
  ;; sudo accepts password over stdin but does not strip it
  ;; in all cases of passwordless sudo (cached pw, nopasswd, etc)
  (apply conj ["sudo" "-S" "-u" user "-H"] command))

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
  (new SudoRunner runner user password))
