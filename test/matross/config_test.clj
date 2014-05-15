(ns matross.config-test
  (:require [clojure.test :refer :all]
            [matross.config :refer :all])
  (:import matross.config.ConfigResolver))


(deftest config-resolver-behaves
  (testing "I can retrieve values"
    (let [cr (config-resolver {:ns {:kwd "foo"}})
          kwd :ns/kwd]
      (is (= (kwd cr) "foo"))
      (is (not (= (kwd cr) (:kwd cr))))))

  (testing "can check contains"
    (let [cr (config-resolver {:ns {:kwd "foo"}} "/" "ns")]
      (is (contains? cr :ns/kwd))
      (is (contains? cr :kwd))
      (is (not (contains? cr :ns)))))

  (testing "Templating occurs properly"
    (let [cr (config-resolver {:test {:foo "{{fact/bar}}" :baz "{{foo}}"}
                               :fact {:bar "herp"}} "/" "test")]
      (is (= (:foo cr) (:fact/bar cr)))
      (is (= (:foo cr) (:baz cr)))))
  
  (testing "Config resolver is iterable"
    (let [cr (ConfigResolver. {:user {:foo "bar"}} "/" "test")
          s (seq cr)]
      (is (= (count s) 1)))))
