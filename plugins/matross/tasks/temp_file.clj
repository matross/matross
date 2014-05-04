(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [clojure.string :refer [trim-newline]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))

(deftask temp-file
  "Low level task to create a temporary file on the target machine.

   Runs ``mktemp $TEMPFILE`` and returns the created file to stdout."

  {:options {:temp-dir "Remote temporary directory (must already exist)"}
   :defaults {:temp-dir "/tmp"}
   :examples [{:type :temp-file :temp-dir "/my/other/tmp"}
              "" ";; you can also use the with-temp-files conveneince macro"
              '(with-temp-files [tmp-a tmp-b]
                 (use-temp-files tmp-a tmp-b))]}

  [conn {:keys [temp-dir] :or {temp-dir "/tmp"}}]
  (let [mktemp (str "mktemp " temp-dir "/matross.XXXXXX")
        {:keys [data succeeded?]} (run-task conn {:type :command :command mktemp})
        temp-file (trim-newline (stream-to-string data :out))]
    (task-result succeeded? true (assoc data :path temp-file))))

(defmacro with-temp-files
  "Evaluate the body with the names bound to temp file paths on the target machine. After the
   body has been evaluated, delete the temp files from the target machine."
  [conn bindings & body]
  (cond (some #(= conn %1) bindings)
        (throw (IllegalArgumentException. "Collission between conn and tempfile bindings."))

        :else
        `(let [~@(interleave bindings
                             (map (fn [b] `(get-in (run-task ~conn {:type :temp-file}) [:data :path])) bindings))]
           (try
             ~@body
             (finally
               (run-task ~conn {:type :command :command (str "/bin/rm -f " (clojure.string/join " " ~bindings))}))))))
