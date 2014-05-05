(ns matross.tasks.temp-file-test
  (:require [matross.test.helper :refer [task-tests]]
            [matross.connections.core :refer [exit-ok? run]]
            [matross.tasks.temp-file :refer [with-temp-files]]
            [clojure.test :refer :all]))

(deftest ^:integration temp-file-test
  (task-tests test-conn

    (testing "with-temp-files macro"
      (let [tmp-file* (atom nil)]
        (with-temp-files test-conn [tmp-file]
          (reset! tmp-file* tmp-file)
          (is (not (clojure.string/blank? tmp-file)) "Got a file path back")
          (let [proc (run test-conn {:cmd ["test" "-f" tmp-file]})]
            (is (exit-ok? proc) "file exists")))
        (let [proc (run test-conn {:cmd ["test" "-f" @tmp-file*]} )]
          (is (not (exit-ok? proc)) "file is cleaned up"))))))
