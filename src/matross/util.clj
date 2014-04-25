(ns matross.util
  (:require [me.raynes.fs :as fs]
            [me.raynes.conch.low-level :refer [stream-to-string]]
            [matross.connections.core :refer [connect disconnect run sudo-runner]]
            [matross.tasks.core :refer [get-module exec]]))

;; junk namespace... needs re-org
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
  (load-resource-plugins "plugins/connections" "plugins/tasks"))

(defn get-sensitive-user-input [prompt]
  (let [console (. System console)
        padded-prompt (str prompt " ")]
    (if console
      (-> console (.readPassword padded-prompt nil) String/valueOf)
      (throw (Exception. "Could not find system console.")))))

(defn debug-process [{:keys [exit] :as proc}]
  (prn {:exit @exit
        :out (stream-to-string proc :out)
        :err (stream-to-string proc :err)}))

(def ^:private sudo-password (atom nil))
(defn get-sudo-password []
  (or @sudo-password
      (reset! sudo-password (get-sensitive-user-input "sudo password:"))))

(defn test-run-connection! [cli-opts conn mod-type conf]
  (connect conn)
  (let [pass (if (get-in cli-opts [:options :ask-sudo-pass])
               (get-sudo-password))
        module (get-module mod-type)
        conn (if pass (sudo-runner conn "root" pass) conn)]
    (let [result (exec module {:remote conn} conf)]
      (debug-process result)))
  (disconnect conn))
