(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.util :refer [load-plugins! test-run-connection!]]
            [matross.model.core :as model])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :ssh :hostname "127.0.0.1"}
                           {:type :local}]
             :tasks [{:type :command
                      :env {:test "it works!"}
                      :command "/bin/echo -n $test"}
                     {:type :command
                      :command "seq 10 | tail -n 5 | xargs echo"}]})

(def cli-opts
  [["-h" "--help"]
   ["-s" "--ask-password"]
   ["-S" "--ask-sudo-pass"]])

(defn display-help []
  (prn "matross!"))

(defn -main [& args]
  (load-plugins!)
  (let [opts (parse-opts args cli-opts)
        {:keys [connections tasks]} (model/prepare opts config)]
    (if (get-in opts [:options :help])
      (display-help)
      (doseq [conn connections]
        (test-run-connection! opts conn tasks))))
  (shutdown-agents))
