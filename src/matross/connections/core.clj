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

(deftype SudoRunner
  [runner user password]
  IRun
  (run [self opts]
    (let [pw-stream (ByteArrayInputStream. (.getBytes (str password "\n")))]
    (run runner (-> opts
                    (update-in [:cmd] #(apply conj ["sudo" "-S" "-u" user] %1))
                    (update-in [:in] #(SequenceInputStream. pw-stream %1)))))))

(defn sudo-runner [runner user opts]
  (new SudoRunner runner user opts))
