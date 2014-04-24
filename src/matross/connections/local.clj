(ns matross.connections.local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.connections.core :refer [IConnect IRun ITransfer get-connection]]))

(deftype Local
  [conf]

  IConnect
  (connect [self] true)
  (disconnect [self] true)

  IRun
  (run [self {:keys [cmd in] :as opts}]
    (let [command ["whoami"]
          {:keys [in out err] :as proc} (apply sh/proc cmd)]
      ;; todo: write user provided in to process in
      {:exit (future (sh/exit-code proc))
       :out out
       :err err}))

  ITransfer
  (get-file [self file-conf])

  (put-file [self file-conf]))

(defmethod get-connection :local [spec]
  (new Local spec))
