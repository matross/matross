(ns matross.connections.core)

(defprotocol IConnect
  "Interface for matross connections"

  (connect [self]
    "connect to a machine")

  (disconnect [self]
    "disconnect from a machine"))

(defprotocol IRun
  (run [self opts]
    "execute a command on a machine"))

(defprotocol ITransfer
  (get-file [self opts]
    "get a file from a machine")

  (put-file [self opts]
    "put a file onto a machine"))

(defmulti get-connection
  "get a connection instance of a specific type" :type)
