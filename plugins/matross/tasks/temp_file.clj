(ns matross.tasks.temp-file
  (:require [matross.tasks.core :refer [deftask run-task task-result]]
            [clojure.string :refer [trim-newline]]
            [me.raynes.conch.low-level :refer [stream-to-string]]))

(defn temp-file-command
  "Generates the mktemp shell command for the given options"
  [{:keys [temp-dir] :or {temp-dir "/tmp"}}]
  (str "mktemp " temp-dir "/matross.XXXXXX"))

(deftask temp-file
  "Creates  a temporary file on the target machine and returns the file path in [:data :path]"

  {:options {:temp-dir "Remote temporary directory (must already exist)"}
   :defaults {:temp-dir "/tmp"}
   :examples [{:type :temp-file :temp-dir "/my/other/tmp"}
              "" ";; you can also use the with-temp-files conveneince macro"
              '(with-temp-files [tmp-a tmp-b]
                 (use-temp-files tmp-a tmp-b))]}

  [conn opts]
  (let [mktemp (temp-file-command opts)
        {:keys [data succeeded?]} (run-task conn {:type :command :command mktemp})
        temp-file (trim-newline (stream-to-string data :out))]
    (task-result succeeded? true (assoc data :path temp-file))))

(defn temp-file-list [conn opts n]
  (let [command-str (clojure.string/join "; " (repeatedly n #(temp-file-command opts)))
        {:keys [data succeeded?]} (run-task conn {:type :command :command command-str})
        out (stream-to-string data :out)]
    (clojure.string/split-lines out)))

;; TODO: Optimize this to make a single command call with multiple calls to mktemp
;;        split on newline, and bind each line to a given binding
(defmacro with-temp-files
  "Evaluate the body with the symbols in `bindings` bound to temp file paths on the target machine. Once the
  body has been evaluated, the temp files are deleted from the target machine."

  [conn & args]

  (let [[opts args] (if (map? (first args))
                       [(first args) (rest args)]
                       [nil args])
        opts (merge opts {:type :temp-file})
        [bindings body] [(first args) (rest args)]]
    (cond
      (some #(= conn %1) bindings)
      (throw (IllegalArgumentException. "Collission between conn and tempfile bindings."))
      :else
      (let [binding-count (count bindings)
            temp-files (gensym "temp-files")]
        `(let [~temp-files (temp-file-list ~conn ~opts ~binding-count)
               ~@(apply concat
                        (map-indexed (fn [i v] [v `(nth ~temp-files ~i)])
                                     bindings))
               cleanup# (str "/bin/rm -f " (clojure.string/join " " ~bindings))]
           (try ~@body
                (finally
                  (run-task ~conn {:type :command :command cleanup#}))))))))
