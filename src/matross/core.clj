(ns matross.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [matross.executor :refer [run!]])
  (:gen-class))

(def cli-opts
  [["-h" "--help"]
   ["-s" "--ask-password"]
   ["-S" "--ask-sudo-pass"]
   ["-d" "--debug"]
   ["-e" "--extra-var KEY=VALUE" "Extra variables to set at run time"
    :id :extra-vars
    :assoc-fn (fn [m k v]
                (let [=idx (. v indexOf "=")
                      =present (not (= =idx -1))
                      vk (subs v 0 (if =present =idx (count v)))
                      vkwds (map keyword (clojure.string/split vk #"\."))
                      vv (if =present (subs v (inc =idx)) true)]
                  (update-in m (concat [k] vkwds) (constantly vv))))]])

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
