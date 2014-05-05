(ns matross.tasks.file-test
  (:require [matross.test.helper :refer [task-tests]]
            [matross.tasks.core :refer [run-task]]
            [matross.connections.core :refer [exit-ok?]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [clojure.test :refer :all]))

(deftest ^:integration file-test
  (task-tests test-conn

    (testing "can remove files"
      (with-temp-files test-conn [file]
        (let [remove-file {:type :file :state :absent :path file}
              file-exists {:type :command :command (str "test -f " file)}]
          (is (:succeeded? (run-task test-conn file-exists))       "file already exists")
          (is (:succeeded? (run-task test-conn remove-file))       "file is removed")
          (is (not (:succeeded? (run-task test-conn file-exists))) "file doesnt exist anymore"))))

    (testing "can create files"
      (with-temp-files test-conn [file]
        (let [remove-file {:type :file :path file :state :absent}
              create-file {:type :file :path file}
              file-exists {:type :command :command (str "test -f " file)}]
          (is (:succeeded? (run-task test-conn remove-file)) "initial state is empty")
          (is (:succeeded? (run-task test-conn create-file)) "file is created")
          (is (:succeeded? (run-task test-conn file-exists)) "file exists"))))

    (testing "can create directories")
    (testing "can delete directories")
    (testing "can set file permissions")
    (testing "can set ownership")))
