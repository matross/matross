(ns matross.test.helper
  "Helper functions for writing and running tests"
  (:require [me.raynes.conch :refer [with-programs]]))

(defn get-running-vms []
  (with-programs [vagrant grep cut]
    (cut "-f1" "-d "
      (grep "running"
        (vagrant "status" {:seq true})
        {:seq true})
      {:seq true})))
