(ns matross.model.core)

(defmulti get-connection :type)

(defn prepare [conf]
  (update-in conf [:connections] (fn [conns] (map get-connection conns))))
