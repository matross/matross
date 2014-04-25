(ns matross.tasks.command
  (:require [matross.tasks.core :refer [get-module ITask]]
            [matross.connections.core :refer [run]]))

(defn format-env [m]
  (map (fn [[k v]] (str (name k) "=" v)) m))

(defmethod get-module :command [t]
  (reify ITask
    (exec [self {:keys [remote]} conf]
      (let [cmd (concat ["/usr/bin/env" "-i"]
                        (format-env (:env conf))
                        [(get conf :shell "/bin/sh") "-c" (:command conf)])]
        (run remote {:cmd cmd})))))
