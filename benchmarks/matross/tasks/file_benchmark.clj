(ns matross.tasks.file-benchmark
  (:require [perforate.core :refer :all]
            [matross.tasks.file :refer :all]
            [matross.tasks.command :refer :all]
            [matross.tasks.script]
            [matross.tasks.stream-to-file]
            [matross.connections.core :refer [connect disconnect]]
            [matross.test.helper :refer [test-connection]]))

(defgoal file-benchmarks
  "let's see how well the file task performs"
  :setup (fn [] (let [conn (test-connection)]
                  (connect conn)
                  [conn {:type :file :state :abset :path "/herp"}]))

  :cleanup (fn [conn _] #_(disconnect conn)))


(defcase file-benchmarks :file
  [conn task]
  (file conn task))
