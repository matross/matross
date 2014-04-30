(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.executor :refer [run!]])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :local}
                           {:type :ssh :hostname "127.0.0.1"}]
             :tasks [{:type :template
                      :content "I'm sorry, {{name}}. I can't let you do that."
                      :dest "/tmp/template"
                      :vars {:name "Darrell"}}
                     {:type :command
                      :command "/bin/echo -n `whoami`: $test"
                      :env {:test "it works!"}}
                     {:type :command
                      :command "seq 10 | tail -n 5 | xargs echo -n"}]})

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
