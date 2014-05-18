(ns matross.plugins
  (:require [cemerick.pomegranate :as pom]
            [me.raynes.fs :as fs]))

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
