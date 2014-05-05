(ns matross.tasks.template-test
  (:require [matross.tasks.core :refer [run-task]]
            [matross.connections.core :refer [exit-ok?]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [matross.test.helper :refer [task-tests]]
            [clojure.test :refer :all])
  (:import [java.io StringWriter]))

(deftest ^:integration template-test
  (task-tests test-conn
   (with-temp-files test-conn [tmp-file]
     (let [template  "Hello, {{name}}."
           conf {:type :template :inline template :dest tmp-file :vars {:name "derpy"}}
           result (run-task test-conn conf)]
       (is (:succeeded? result)))
     (let [result-buffer (new StringWriter)
           conf {:type :stream-from-file :src tmp-file :dest result-buffer}
           result (run-task test-conn conf)]
       (is (:succeeded? result) "got the file")
       (is (= (.toString result-buffer) "Hello, derpy."))))))
