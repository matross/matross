(ns matross.tasks.file-test
  (:require [matross.test.helper :refer [task-tests]]
            [matross.connections.core :refer [exit-ok? run]]
            [clojure.test :refer :all]))

(deftest ^:integration file-test
  (task-tests test-conn
    (testing "can create files")
    (testing "can remove files")
    (testing "can create directories")
    (testing "can delete directories")))
