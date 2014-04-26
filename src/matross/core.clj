(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.util :refer [load-plugins! test-run-connection!]]
            [matross.model.core :as model])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :ssh :hostname "127.0.0.1"}
                           {:type :local}]})

(def cli-opts
  [["-h" "--help"]
   ["-K" "--ask-sudo-pass"]])

(defn display-help []
  (prn "matross!"))

(defn -main [& args]
  (load-plugins!)
  (let [opts (parse-opts args cli-opts)
        {:keys [connections]} (model/prepare config)]
    (if (get-in opts [:options :help])
      (display-help)
      (let [command {:type :command
                     :env {:test "it works!"}
                     :command "/bin/echo -n $test"}]
        (doseq [conn connections]
          (test-run-connection! opts conn command))
        (shutdown-agents)))))
