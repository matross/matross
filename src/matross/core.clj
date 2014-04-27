(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.util :refer [run!]])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :local}
                           {:type :ssh :hostname "127.0.0.1"}]
             :tasks [{:type :command
                      :command "/bin/echo -n $test"
                      :env {:test "it works!"}}
                     {:type :command
                      :command "seq 10 | tail -n 5 | xargs echo"}]})

(def cli-opts
  [["-h" "--help"]
   ["-s" "--ask-password"]
   ["-S" "--ask-sudo-pass"]])

(defn display-help []
  (prn "matross!"))

(defn -main [& args]
  (let [opts (parse-opts args cli-opts)]
    (if (get-in opts [:options :help])
      (display-help)
      (run! opts config))))
