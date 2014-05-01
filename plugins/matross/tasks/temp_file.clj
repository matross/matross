(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [clojure.string :refer [trim-newline]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))


(deftask :temp-file [conn conf]
  (let [{:keys [data succeeded?]} (run-task conn {:type :command :command "mktemp"})
        temp-file (trim-newline (stream-to-string data :out))]
    (task-result succeeded? true (assoc data :path temp-file))))

(defmacro with-temp-files
  ;; hic sunt dracones
  "Evaluate the body with the names bound to temp files on the connection"
  [conn bindings & body]
  (cond
    (= (count bindings) 0)
      `(do ~@body)
    (= (bindings 0) conn)
      `(throw (IllegalArgumentException. "Collission between conn and tempfile bindings."))
    (symbol? (bindings 0))
      `(let [~(bindings 0) (get-in (run-task ~conn {:type :temp-file}) [:data :path])]
         (try
           (with-temp-files ~conn ~(subvec bindings 1) ~@body)
           (finally (run-task ~conn {:type :command
                                     :command (str "rm -f " ~(bindings 0))}))))))

