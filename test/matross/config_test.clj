(ns matross.config-test
  (:require [clojure.test :refer :all]
            [matross.config :refer :all])
  (:import matross.config.ConfigResolver))


(deftest config-resolver-behaves
  (testing "I can retrieve values"
    (let [cr (config-resolver {:ns {:kwd "foo"}})
          kwd (keyword "ns::kwd")]
      (is (= (kwd cr) "foo"))
      (is (not (= (kwd cr) (:kwd cr))))))

  (testing "Templating occurs properly"
    (let [cr (config-resolver {:user {:foo "{{fact::bar}}" :baz "{{foo}}"}
                               :fact {:bar "herp"}})]
      (println (:baz cr))
      (is (= (:foo cr) ((keyword "fact::bar") cr)))
      (is (= (:foo cr) (:baz cr)))))

  (comment "not working yet" testing "Seq looks ok"
    (let [cr (ConfigResolver. {:user {:foo "bar"}} "::" "user")
          s (seq cr)]
      (is (= (count s) 1))
)))
