(ns matross.test.helper
  "Helper functions for writing and running tests"
  (:require [me.raynes.conch :refer [with-programs]]))

(def running-vms
  (with-programs [vagrant grep cut]
    (cut "-f1" "-d "
         (grep "running"
               (vagrant "status" {:seq true})
               {:seq true})
         {:seq true})))

; need to figure out how to load plugins in the testing phase otherwise we
; won't be able to load plugins for testing
(comment defn vm-ssh-connection [name]
  (with-programs [vagrant]
    (get-connection (ssh-config-connection (vagrant "ssh-config" name)))))
