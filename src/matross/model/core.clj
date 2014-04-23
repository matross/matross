(ns matross.model.core
  (:require [matross.connections.core :refer [get-connection]]))

(defn prepare [conf]
  (update-in conf [:connections] (partial map get-connection)))

