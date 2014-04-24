(ns matross.connections.local
  (:require [me.raynes.conch.low-level :as sh]
            [matross.connections.core :refer [IConnect IRun get-connection]]))

(deftype Local [conf]

  IConnect
  (connect [self] true)
  (disconnect [self] true)

  IRun
  (run [self {:keys [cmd in] :as opts}]
    (let [{:keys [out err] :as proc} (apply sh/proc cmd)]
      (when in
        (with-open [stream (:in proc)]
          (clojure.java.io/copy in (:in proc))))

      {:exit (future (sh/exit-code proc))
       :out out
       :err err})))

(defmethod get-connection :local [spec]
  (new Local spec))
