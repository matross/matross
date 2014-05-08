(ns matross.config.template-map
  (:require [clostache.parser :as template])
  (:import clojure.lang.IFn
           clojure.lang.ILookup
           clojure.lang.IPersistentCollection
           clojure.lang.IPersistentMap
           clojure.lang.ISeq
           clojure.lang.MapEntry
           clojure.lang.PersistentList
           clojure.lang.Seqable)
  (:gen-class))

(declare template-map)

(deftype TemplateMap [value cursor lookups root]
  ILookup
  (valAt [this k]
    (.valAt this k nil))
  (valAt [this k not-found]
    (let [lookup-path (conj cursor k)
          v (get-in value lookup-path not-found)
          root (or root this)]
      (cond
       (instance?  String v) (do
                 (if (some #(= lookup-path %1) @lookups)
                   (throw (IllegalArgumentException.
                           (str "Circular Reference: "
                                (clojure.string/join " -> " (conj @lookups lookup-path))))))
                 (swap! lookups #(conj %1 lookup-path))
                 (let [tv (template/render v root)]
                   (swap! lookups pop)
                   tv))

       (instance? IPersistentMap v) (template-map value lookup-path lookups root)

       (empty v) clojure.lang.PersistentList/EMPTY
       (nil? v) ""

       :else (throw (Exception. "zomg")))))

  IFn
  (invoke [this k]
    (.valAt this k))
  (invoke [this k not-found]
    (.valAt this k not-found))

  ; Everything below here is in hopes of making nested maps work
  IPersistentCollection
  (empty [this] (empty (get-in value cursor)))

  (equiv [this o] (and (= (.value o) value) (= (.cursor o) cursor)))

  (cons [this o]
    (if (instance? TemplateMap o)
      (template-map (update-in value (.cursor o) #(cons %1 (get-in (.value o) (.cursor o))))
                    cursor lookups root)
      (template-map (update-in value cursor #(cons o %1)) cursor lookups root)))

  IPersistentMap
  (assoc [this k v]
    (template-map (assoc-in value (conj cursor k) v) cursor lookups root))
  (without [this k]
    (let [target (get-in value cursor)
          new-target (dissoc target k)
          new-value (assoc-in value (conj cursor k) new-target)]
      (template-map new-target cursor lookups root)))

  (count [this] (count (get-in value cursor)))

  (entryAt [this k]
    (MapEntry. k (.valAt this k)))

  (containsKey [this k]
    (contains? (get-in value cursor) k))

  ISeq
  (first [this] (if-let [f (first (get-in value cursor))]
                  (MapEntry. (key f) (.valAt this (key f)))))

  (next [this] (if-let [n (next (get-in value cursor))]
                 (template-map (assoc-in value cursor n) cursor lookups root)))

  (more [this] (let [n (next (get-in value cursor))]
                 (template-map
                  (assoc-in value cursor (or n clojure.lang.PersistentList/EMPTY ))
                  cursor
                  lookups
                  root)))

)

(defn template-map
  ([value] (template-map value [] (atom []) nil))
  ([value cursor lookups root] 
     (TemplateMap. value cursor lookups root)))
