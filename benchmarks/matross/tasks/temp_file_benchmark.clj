(ns matross.tasks.temp-file-benchmark
  (:require [perforate.core :refer :all]
            [matross.tasks.temp-file :refer :all]
            [matross.tasks.command :refer :all]
            [matross.connections.core :refer [connect disconnect]]
            [matross.test.helper :refer [test-connection]]))

(defgoal temp-file-benchmarks
  "let's see how well the temp file task performs"
  :setup (fn [] (let [conn (test-connection)]
                  (connect conn)
                  [conn {:type :temp-file}]))

  :cleanup (fn [conn _] #_(disconnect conn)))


(defcase temp-file-benchmarks :temp-file
  [conn task]
  (temp-file conn task))

(defcase temp-file-benchmarks :with-temp-files-macro
  [conn _]
  (with-temp-files conn [a b c d]))
