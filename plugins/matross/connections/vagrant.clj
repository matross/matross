(ns matross.connections.vagrant
  (:require [me.raynes.conch :refer [with-programs]]
            [matross.connections.core :refer [IConnect IRun get-connection] :as core]
            [matross.connections.ssh :refer [ssh-connection translate-ssh-config]]))

(deftype Vagrant [ssh-future]

  IConnect
  (connect [self] (core/connect @ssh-future))
  (disconnect [self] (core/disconnect @ssh-future))

  IRun
  (run [self command]
    (core/run @ssh-future command)))

(defn vagrant-ssh-connection [spec]
  (with-programs [vagrant]
    (let [ssh-config (vagrant "ssh-config" (get spec :name "default"))
          ssh-spec (translate-ssh-config ssh-config)]
      (ssh-connection (merge ssh-spec spec)))))

(defmethod get-connection :vagrant [spec]
  (Vagrant. (future (vagrant-ssh-connection spec))))
