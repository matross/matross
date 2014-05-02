(ns matross.connections.debug
  "Connection decorator for debugging. Wraps a connection and records all calls
to IConnect and IRun"
  (:require [matross.connections.core :refer [IConnect IRun run connect disconnect]]
            [clojure.pprint :refer [pprint]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))

(defn debug-process [{:keys [exit] :as proc}]
  {:exit @exit
   ; need to figure out how to get the stream contents without exhausting it at the same time
   :out (stream-to-string proc :out)
   :err (stream-to-string proc :err)})

(defrecord DebugConnection [conn history*]
  IConnect
  (connect [self]
    (swap! history* conj :connect)
    (connect conn))

  (disconnect [self]
    (swap! history* conj :disconnect)
    (disconnect conn)
    (pprint @history*))

  IRun
  (run [self opts]
    (let [result (run conn opts)]
      (swap! history* conj {:input opts :output result})
      result)))

(defn debug-connection [conn]
  (DebugConnection. conn (atom [])))

