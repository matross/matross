(ns matross.validators-test
  (:require [matross.validators :refer :all]
            [validateur.validation :refer [valid? invalid? validation-set]]
            [clojure.test :refer :all]))


(deftest only-one-of-test
  (testing "Valid maps are valid"
    (let [test-fn (only-one-of [:a :b :c])
          test-map {:a "foo" :d "bar"}
          result (test-fn test-map)]
      (is (= (count result) 2))
      (is (first result))
      (is (empty? (second result)))))

  (testing "Invalid maps are invalid"
    (let [test-fn (only-one-of [:a :b :c])
          test-map {:a "foo" :b "bar"}
          result (test-fn test-map)]
      (is (= (count result) 2))
      (is (not (first result)))
      (is (not (empty? (second result))))))

  (testing "Validateur integration"
    (let [vs (validation-set (only-one-of [:a :b :c]))
          good-map {:a "foo" :d "bar"}
          bad-map {:a "foo" :b "bar"}]
      (is (valid? vs good-map))
      (is (invalid? vs bad-map)))))

(deftest if-set-file-exists-test
  (testing "A hopefully non-existant file fails"
    (let [m {:foo "/tmp/afjlfjasljsdfjlsfjks"}
          v-foo (if-set-file-exists :foo)
          v-bar (if-set-file-exists :bar)]
      (is (= (v-bar m) [true {}]))
      (is (= (first (v-foo m)) false)))))
