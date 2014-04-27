(ns matross.util
  (:require [me.raynes.fs :as fs]
            [me.raynes.conch.low-level :refer [stream-to-string]]
            [cemerick.pomegranate :as pom]
            [matross.connections.core :refer [connect disconnect run]]
            [matross.tasks.core :refer [get-task exec]]))

;; junk namespace... needs re-org
(defn load-plugins [& dirnames]
  "Loads all the clojure files in a directory"
  (doseq [dir dirnames]
    (doseq [file (fs/find-files dir #".*\.clj")]
      (-> file .getAbsolutePath load-file))))

(defn load-plugins! []
  (let [plugins-dir "plugins"
        dir (partial fs/file plugins-dir "matross")]
    (pom/add-classpath plugins-dir)
    (load-plugins (dir "connections") (dir "tasks"))))

(defn get-sensitive-user-input [prompt]
  (let [console (System/console)
        padded-prompt (str prompt " ")]
    (if console
      (-> console (.readPassword padded-prompt nil) String/valueOf)
      (throw (new Exception "Could not find system console.")))))

(defn debug-process [{:keys [exit] :as proc}]
  (prn {:exit @exit
        :out (stream-to-string proc :out)
        :err (stream-to-string proc :err)}))

(defn test-run-connection! [opts conn tasks]
  (connect conn)
  (doseq [task tasks]
    (-> (get-task task)
        (exec conn)
        :data debug-process))
  (disconnect conn))
