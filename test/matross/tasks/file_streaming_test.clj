(ns matross.tasks.file-streaming-test
  (:require [matross.tasks.core :refer [run-task]]
            [matross.test.helper :refer [task-tests]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [clojure.test :refer :all])
  (:import [java.io StringWriter]))

(def file-contents "This is some test content")

(deftest ^:integration file-stream-tests
  (task-tests test-conn

    (testing "File Read and Write Capabilities")
      (with-temp-files test-conn [target]
        (let [write-file {:type :stream-to-file :src file-contents :dest target}
              send-result (run-task test-conn write-file)]
          (is (:succeeded? send-result) "stream to file succeeded"))
        
        (let [result-buffer (new StringWriter)
              read-file {:type :stream-from-file :src target :dest result-buffer}
              get-result (run-task test-conn read-file)]
          (is (:succeeded? get-result) "retrieved file")
          (is (= (.toString result-buffer) file-contents))))))
