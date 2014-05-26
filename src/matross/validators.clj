(ns matross.validators
  (:require [me.raynes.fs :as fs]))

(defn- only-one-of-messages [keys found]
  (reduce #(assoc %1 %2
                  #{(str "Expected only one of: "
                          (clojure.string/join ", " keys)
                          ", but found: "
                          (clojure.string/join ", " found))}) {} found))
(defn only-one-of
  ([keys] (only-one-of keys nil))
  ([keys message]
   (fn [m]
     (let [found-keys (->> keys
                           (map #(if (contains? m %) % false))
                           (filter identity))]
       (if (= (count found-keys) 1)
         [true {}]
         [false (if message message (only-one-of-messages keys found-keys))])))))

(defn if-set-file-exists [k]
  (fn [m]
    (if-let [v (get m k)]
      (let [f (fs/file v)]
        (if (fs/file? f)
          [true {}]
          [false {k #{(str "The path `" v "` is not a file.")}}]
          ))
      [true {}])))
