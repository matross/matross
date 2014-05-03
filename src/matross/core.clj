(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.executor :refer [run!]])
  (:gen-class))

(def cli-opts
  [["-h" "--help"]
   ["-s" "--ask-password"]
   ["-S" "--ask-sudo-pass"]
   ["-d" "--debug"]])

(defn exit [msg]
  (if msg (println msg))
  (System/exit 1))

(defn show-usage []
  (println "usage: matross <config>"))

(defn show-help [{:keys [summary]}]
  (show-usage)
  (println summary))

(defn get-config [{:keys [options arguments] :as opts}]
  (if (:help options)
    (exit (show-help opts))
    (if-let [config-path (first arguments)]
      (load-file config-path)
      (exit (show-usage)))))

(defn -main [& args]
  (let [opts (parse-opts args cli-opts)
        config (get-config opts)]
    (run! opts config)))
