(ns matross.core
  (:require [matross.connections.ssh :as s])
  (:gen-class))

(defn -main [& args]
  (s/run nil nil nil))
