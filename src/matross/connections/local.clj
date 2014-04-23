(ns matross.connections.local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.connections.core :refer [IConnection]]
            [matross.model.core :refer [get-connection]]))

(deftype Local
  [conf]
  IConnection

  (connect [self] true)
  (disconnect [self] true)

  (run [self command]
    (let [command ["whoami"]
          proc (apply sh/proc command)]
      {:exit (sh/exit-code proc)
       :out (sh/stream-to-string proc :out)
       :err (sh/stream-to-string proc :err)}))

  (get-file [self file-conf])

  (put-file [self file-conf]))

(defn local-connection [conf] (new Local conf))

(defmethod get-connection :local [spec]
  (local-connection spec))
