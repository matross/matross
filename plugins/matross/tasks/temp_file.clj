(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [clojure.string :refer [trim-newline]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))

(deftask temp-file
  "Creates  a temporary file on the target machine and returns the file path in [:data :path]"

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

;; TODO: Optimize this to make a single command call with multiple calls to mktemp
;;        split on newline, and bind each line to a given binding
(defmacro with-temp-files
  "Evaluate the body with the symbols in `bindings` bound to temp file paths on the target machine. Once the
   body has been evaluated, the temp files are deleted from the target machine."

  [conn bindings & body]

  (cond
    (some #(= conn %1) bindings)
      (throw (IllegalArgumentException. "Collission between conn and tempfile bindings."))
    :else
      (let [get-tmpfile (fn [_] `(-> (run-task ~conn {:type :temp-file}) (get-in [:data :path])))]
        `(let [~@(interleave bindings (map get-tmpfile bindings))
               cleanup# (str "/bin/rm -f " (clojure.string/join " " ~bindings))]
           (try ~@body
             (finally
               (run-task ~conn {:type :command :command cleanup#})))))))
