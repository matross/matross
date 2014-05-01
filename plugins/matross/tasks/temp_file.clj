(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))


(deftask :temp-file [conn conf]
  (let [temp-result (run-task conn {:type :command :command "mktemp"})
        temp-file (stream-to-string (get temp-result :data) :out)]
    (task-result (:succeeded? temp-result)
                 true
                 (assoc (:data temp-result) :path temp-file))))

(defmacro with-temp-files
  "Evaluate the body with the names bound to temp files on the given connection,
cleaning them up after execution."
  [conn bindings & body]
  `(let [~@(interleave bindings
                       (map (fn [b#] '(get-in (run-task conn {:type :temp-file}) [:data :path])) bindings))]
     ~@body))
