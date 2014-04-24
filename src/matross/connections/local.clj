(ns matross.connections.local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.connections.core :refer [IConnect IRun ITransfer get-connection]]))

(deftype Local
  [conf]

  IConnect
  (connect [self] true)
  (disconnect [self] true)

  IRun
  (run [self command]
    (let [command ["whoami"]
          proc (apply sh/proc command)]
      {:exit (future (sh/exit-code proc))
       :out (:out proc)
       :err (:err proc)}))

  ITransfer
  (get-file [self file-conf])

  (put-file [self file-conf]))

(defmethod get-connection :local [spec]
  (new Local spec))
