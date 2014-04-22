(ns matross.connections.core)

(defprotocol IConnection
  "Interface for matross connections"

  (connect [self]
    "connect to a machine")

  (disconnect [self]
    "disconnect from a machine")

  (run [self opts]
    "execute a command on a machine")

  (get-file [self opts]
    "get a file from a machine")

  (put-file [self opts]
    "put a file onto a machine"))

(defn run! [conn opts]
  (try
    (if (connect conn)
      (run conn opts))
    (finally (disconnect conn))))
