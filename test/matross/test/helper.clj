(ns matross.test.helper
  "Helper functions for writing and running tests"
  (:require [me.raynes.conch :refer [with-programs]]
            [matross.connections.ssh :refer [translate-ssh-config ssh-connection]]
            [matross.connections.core :refer [connect disconnect]]))

(defn get-available-vms []
  (with-programs [vagrant grep cut]
    (cut "-f1" "-d "
         (vagrant "status" {:seq true})
         {:seq true})))

(def vagrant-test-connection
  (translate-ssh-config (slurp (System/getenv "TEST_SSH_CONF"))))

(defmacro task-tests
  "Evaluate the body against the test vm connection, exposed as conn"
  [conn & body]

  `(let [~conn (ssh-connection vagrant-test-connection)]
     (connect ~conn)
     ~@body
     (disconnect ~conn)))
