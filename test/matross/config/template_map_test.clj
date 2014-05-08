(ns matross.config.template-map-test
  (:require [matross.config.template-map]
            [matross.config.template-map :refer [template-map]]
            [clojure.test :refer :all])
  (:import matross.config.template_map.TemplateMap))

(deftest template-map-test
  (testing "various ways of lookups"
    (let [tm (template-map {:key "value"})]
      (is (= (:key tm) "value"))
      (is (= (:not-key tm "default") "default"))
      (is (= (tm :key) "value"))
      (is (= (tm :not-key "default") "default"))))

  (testing "Retrieving a string with a variable gets evaluated"
    (let [tm (template-map {:key "{{ lookup }}" :lookup "value"})]
      (is (= (:key tm) "value"))))

  (testing "Circular template reference throws an exception"
    (let [tm (template-map {:key "{{ key }}"})]
      (is (thrown? IllegalArgumentException (:key tm))))
    (let [tm (template-map {:key "{{ lookup }}" :lookup "{{ key }}"})]
      (is (thrown? IllegalArgumentException (:key tm)))))
  (testing "A retrieved map is wrapped in a TemplateMap"
    (let [tm (template-map {:key {:key "value"}})]
      (is (= (type (:key tm)) TemplateMap))))

  (comment "Map lookups are currently borked"
    (testing "Complex keys can be templated"
      (let [tm (template-map {:key "{{a.b}}" :a {:b "value"}})]
        (is (= (:key tm) "value"))))
    (testing "Circular lookup fails in maps too"
      (let [tm (template-map {:key {:key "{{key.key}}"}})]
        (is (thrown? IllegalArgumentException (get-in tm [:key :key])))))))
