(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [clojure.string :refer [trim-newline]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))


(deftask temp-file
  "Create a temporary file on the target machine."
  [conn conf]
  (let [temp-dir "/tmp"
        mktemp (str "mktemp " temp-dir "/matross.XXXXXX")
        {:keys [data succeeded?]} (run-task conn {:type :command :command mktemp})
        temp-file (trim-newline (stream-to-string data :out))]
    (task-result succeeded? true (assoc data :path temp-file))))

(defmacro with-temp-files
  "Evaluate the body with the names bound to temp file paths on the target machine. After the
   body has been evaluated, delete the temp files from the target machine."
  [conn bindings & body]
  (cond (some #(= conn %1) bindings)
        `(throw (IllegalArgumentException. "Collission between conn and tempfile bindings."))

        :else
        `(let [~@(interleave bindings
                             (map (fn [b] `(get-in (run-task ~conn {:type :temp-file}) [:data :path])) bindings))]
           (try
             ~@body
             (finally
               (run-task ~conn {:type :command :command (str "/bin/rm -f " (clojure.string/join " " ~bindings))}))))))
