(ns matross.validators)

(defn- only-one-of-messages [keys found]
  (reduce #(assoc %1 %2
                  (str "Expected only one of: "
                       (clojure.string/join ", " keys)
                       ", but found: "
                       (clojure.string/join ", " found))) {} found))
(defn only-one-of [keys]
  (fn [m]
    (let [found-keys (->> keys
                          (map #(if (contains? m %) % false))
                          (filter identity))]
      (if (= (count found-keys) 1)
        [true {}]
        [false (only-one-of-messages keys found-keys)]))))
