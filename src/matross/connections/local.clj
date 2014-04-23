(ns matross.connections.local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.connections.core :refer [IConnect IInteract get-connection]]))

(deftype Local
  [conf]

  IConnect
  (connect [self] true)
  (disconnect [self] true)

  IInteract
  (run [self command]
    (let [command ["whoami"]
          proc (apply sh/proc command)]
      {:exit (sh/exit-code proc)
       :out (sh/stream-to-string proc :out)
       :err (sh/stream-to-string proc :err)}))

  (get-file [self file-conf])

  (put-file [self file-conf]))

(defmethod get-connection :local [spec]
  (new Local spec))
