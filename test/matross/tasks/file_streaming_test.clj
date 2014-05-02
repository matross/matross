(ns matross.tasks.file-streaming-test
  (:require [matross.tasks.core :refer [run-task]]
            [matross.test.helper :refer [task-tests]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [clojure.test :refer :all])
  (:import [java.io StringWriter]))

(def file-contents "This is some test content")

(deftest ^:integration file-stream-tests
  (task-tests
   test-conn
   (testing "File Read and Write Capabilities")
   (with-temp-files test-conn [target]
     (let [send-result (run-task test-conn
                                 {:type :stream-to-file :src file-contents :dest target})]
       (is (:succeeded? send-result) "stream to file succeeded"))

     (let [result-buffer (new StringWriter)
           get-result (run-task test-conn
                                {:type :stream-from-file :src target :dest result-buffer})]
       (is (= (.toString result-buffer) file-contents))))))
