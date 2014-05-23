(ns matross.test.helper
  "Helper functions for writing and running tests"
  (:require [me.raynes.conch :refer [with-programs]]
            [matross.connections.ssh :refer [translate-ssh-config ssh-connection]]
            [matross.connections.local :refer [local-connection]]
            [matross.connections.debug :refer [debug-connection]]
            [matross.connections.core :refer [connect disconnect]]))

(defn get-available-vms []
  (with-programs [vagrant grep cut]
    (cut "-f1" "-d "
         (vagrant "status" {:seq true})
         {:seq true})))

(def ssh-test-connection
  (let [ssh-conf (System/getenv "TEST_SSH_CONF")]
    (if ssh-conf
      (memoize (fn [] (translate-ssh-config (slurp ssh-conf)))))))

(defn test-connection []
  (let [debug (System/getenv "TEST_DEBUG")
        local (System/getenv "TEST_LOCAL")
        conn (if local
               (local-connection)
               (ssh-connection (ssh-test-connection)))]
    (if debug
      (debug-connection conn)
      conn)))

(defmacro task-tests
  "Evaluate the body against the test vm connection, exposed as conn"
  [conn & body]

  `(let [~conn (test-connection)]
     (connect ~conn)
     ~@body
     (disconnect ~conn)))
