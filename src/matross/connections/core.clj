(ns matross.connections.core)

(defprotocol IConnection
  (connect [self])
  (run [self command-conf])
  (get-file [self file-conf])
  (put-file [self file-conf])
  (disconnect [self]))
