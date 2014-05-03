(ns matross.tasks.template-test
  (:require [matross.tasks.core :refer [run-task]]
            [matross.connections.core :refer [exit-ok?]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [matross.test.helper :refer [task-tests]]
            [clojure.test :refer :all])
  (:import [java.io StringWriter]))

(def template "Hello, {{name}}.")

(deftest ^:integration template-test
  (task-tests
   test-conn

   (with-temp-files test-conn [tmp-file]
     (let [template-result (run-task test-conn
                                     {:type :template
                                      :inline template
                                      :dest tmp-file
                                      :vars {:name "derpy"}})]
       (is (:succeeded? template-result)))
     (let [result-buffer (new StringWriter)
           get-result (run-task test-conn
                                {:type :stream-from-file :src tmp-file :dest result-buffer})]
       (is (:succeeded? get-result) "got the file")
       (is (= (.toString result-buffer) "Hello, derpy."))))))
