(ns matross.tasks.command
  (:require [matross.tasks.core :refer [get-task ITask]]
            [matross.connections.core :refer [run]]))

(defn key=val [[key val]]
  (str (name key) "=" val))

(defn format-env [env]
  (map key=val env))

(deftype Command [spec]
  ITask
  (exec [self conn]
    (let [{:keys [command shell env]} spec
          shell (or shell "/bin/sh")
          cmd (concat ["/usr/bin/env" "-i"] (format-env env)
                      [shell "-c" command])]
      (run conn {:cmd cmd}))))

(defmethod get-task :command [spec]
  (new Command spec))
