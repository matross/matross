(ns matross.config-test
  (:require [clojure.test :refer :all]
            [matross.config :refer :all]))

(deftest config-resolver-behaves
  (testing "I can retrieve values"
    (let [cr (config-resolver {:ns {:kwd "foo"}})
          kwd (keyword "ns/kwd")]
      (is (= (kwd cr) "foo"))
      (is (not (= (kwd cr) (:kwd cr))))))

  (testing "Templating occurs properly"
    (let [cr (config-resolver {:var {:foo "{{fact/bar}}" :baz "{{foo}}"}
                               :fact {:bar "herp"}})]
      (is (= (:foo cr) ((keyword "fact/bar") cr)))
      (is (= (:foo cr) (:baz cr)))))

  (testing "I can use select-keys"
    (let [cr (config-resolver {:var {:a "{{b}}" :b "value"}})
          sub-set (select-keys cr [:a])]
      (println sub-set)
      (is (= (:a sub-set) "value")))))
