(ns matross.test.helper
  "Helper functions for writing and running tests"
  (:require [me.raynes.conch :refer [with-programs]]
            [matross.connections.core :refer [get-connection]]
            [matross.connections.ssh :refer [ssh-config-connection]]))

(defn get-running-vms []
  (with-programs [vagrant grep cut]
    (cut "-f1" "-d "
      (grep "running"
        (vagrant "status" {:seq true})
        {:seq true})
      {:seq true})))

(defn get-vagrant-connection [box-name]
  (with-programs [vagrant]
    (let [config (vagrant "ssh-config" box-name)]
      (-> config ssh-config-connection get-connection))))
