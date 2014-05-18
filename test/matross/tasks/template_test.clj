(ns matross.tasks.template-test
  (:require [matross.tasks.core :refer [run-task]]
            [matross.connections.core :refer [exit-ok?]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [matross.test.helper :refer [task-tests]]
            [matross.tasks.template :refer [template]]
            [validateur.validation :refer [valid? invalid?]]
            [clojure.test :refer :all])
  (:import [java.io StringWriter]))


(deftest template-validator-test
  (testing "the validator works"
    (let [v (:validator (meta #'template))
          good-map {:file "path" :vars {}}
          bad-map {:file "path" :inline "content"}]
      (is v "We got a validator back")
      (is (valid? v good-map))
      (is (invalid? v bad-map)))))

(deftest ^:integration template-test
  (task-tests test-conn
    (with-temp-files test-conn [tmp-file]

      (testing "can write template files"
        (let [template  "Hello, {{name}}."
              write-template {:type :template :inline template :dest tmp-file :vars {:name "derpy"}}]
          (is (:succeeded? (run-task test-conn write-template)) "writes succesfully")))

      (testing "templated files contain correct contents"
        (let [template-result "Hello, derpy."
              result-buffer (new StringWriter)
              read-file {:type :stream-from-file :src tmp-file :dest result-buffer}]
          (is (:succeeded? (run-task test-conn read-file))  "got the file")
          (is (= (.toString result-buffer) template-result) "ran template through mustache engine"))))))
