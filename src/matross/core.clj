(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [me.raynes.fs :as fs]
            [matross.connections.default :refer [debug-process]]
            [matross.connections.core :refer [connect disconnect run sudo-runner]]
            [matross.tasks.core :refer [get-module exec]]
            [matross.tasks.command]
            [matross.model.core :as model])
  (:gen-class))

(defn load-plugins [& dirnames]
  "Loads all the clojure files in a directory"
  (doseq [dir dirnames]
    (doseq [file (fs/find-files dir #".*\.clj")]
      (-> file .getAbsolutePath load-file))))

(defn resource-to-path [path]
  (.getPath (clojure.java.io/resource path)))

(defn load-resource-plugins [& dirnames]
  (apply load-plugins (map resource-to-path dirnames)))

(defn load-plugins! []
  (load-resource-plugins "plugins/connections"))

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

(defn test-run-connection! [conn module conf pass]
  (connect conn)
  (let [conn (if pass (sudo-runner conn "root" pass) conn)]
    (let [{exit :exit :as result} (exec module {:remote conn} conf)]
      (debug-process result)))
  (disconnect conn))

(defn -main [& args]
  (load-plugins!)
  (let [opts (parse-opts args cli-opts)
        {:keys [connections]} (model/prepare config)
        password (if (get-in opts [:options :ask-sudo-pass])
                   (get-sensitive-user-input "Sudo Password"))]
    (if (get-in opts [:options :help])
      (prn "matross!")
      (let [m (get-module :command)
            cmd {:env {:HERP "value"} :command "echo $HERP"}]
      (doseq [c connections] (test-run-connection! c m cmd password))))
    (shutdown-agents)))
