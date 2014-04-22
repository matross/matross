(ns matross.model.core
  (:require [matross.connections.ssh :as ssh]))

(defn prepare [conf]
  (update-in conf [:connections :ssh] (fn [cs] (map ssh/ssh-connection cs))))

(defn get-connections [conf]
  (mapcat (fn [[k v]] v) (get conf :connections {})))
