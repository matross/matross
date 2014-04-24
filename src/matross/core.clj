(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.connections.ssh]
            [matross.connections.local]
            [matross.connections.core :refer [connect disconnect run sudo-runner]]
            [matross.model.core :as model])
  (:gen-class))

(def config {:connections [{:type :ssh :hostname "localhost"}
                           {:type :ssh :hostname "127.0.0.1"}
                           {:type :local}]})

(def cli-opts
  [["-h" "--help"]
   ["-K" "--ask-sudo-pass"]])

(defn get-sensitive-user-input [prompt]
  (let [console (. System console)
        padded-prompt (str prompt " ")]
    (if console
      (-> console (.readPassword padded-prompt nil) String/valueOf)
      (throw (Exception. "Could not find system console.")))))

(defn test-run-connection! [conn test-command pass]
  (connect conn)
  (let [conn (if pass (sudo-runner conn "root" pass) conn)]
    (let [{exit :exit :as result} (run conn {:cmd [test-command]})]
      (prn {:exit @exit
            :out (me.raynes.conch.low-level/stream-to-string result :out)
            :err (me.raynes.conch.low-level/stream-to-string result :err)})))
  (disconnect conn))

(defn -main [& args]
  (let [opts (parse-opts args cli-opts)
        {:keys [connections]} (model/prepare config)
        password (if (get-in opts [:options :ask-sudo-pass]) (get-sensitive-user-input "Sudo Password"))]
    (if (get-in opts [:options :help])
      (prn "matross!")
      (doseq [c connections] (test-run-connection! c "whoami" password)))
    (shutdown-agents)))
