(ns matross.model.core
  (:require [matross.connections.ssh :as ssh]
            [matross.connections.local :as local]))

(defmulti get-connection :type)

(defmethod get-connection :ssh [spec]
  (ssh/ssh-connection spec))

(defmethod get-connection :local [spec]
  (local/local-connection spec))

(defn prepare [conf]
  (update-in conf [:connections] (fn [conns] (map get-connection conns))))
