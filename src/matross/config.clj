(ns matross.config
  (:require [matross.mapstache :refer [IRender mapstache no-template]]
            [clostache.parser :as mustache])
  (:import clojure.lang.ILookup
           clojure.lang.Seqable
           clojure.lang.SeqIterator
           clojure.lang.IPersistentCollection
           clojure.lang.MapEntry))

(def mustache-renderer (reify IRender
                 (render [this s data] (mustache/render s data))))

(defn- internal-key [k ns-sep default-ns]
  (let [ks (name k)
        ns-pos (. ks indexOf ns-sep)]
    (if (= ns-pos -1)
      (map keyword [default-ns k])
      (map keyword (clojure.string/split ks (re-pattern ns-sep) 2)))))

(defn- external-key [k ns-sep]
  (->> (map name k)
       (clojure.string/join ns-sep)
       keyword))

(deftype ConfigResolver [value ns-sep default-ns]
  ILookup
  (valAt [this k] (. this valAt k nil))
  (valAt [this k not-found]
    (let [[ns-key target-key :as full-key] (internal-key k ns-sep default-ns)]
      (get-in value full-key not-found)))

  IPersistentCollection
  (cons [this o]
    (let [[ns-key target-key] (internal-key (key o) ns-sep default-ns)]
      (ConfigResolver. (update-in value [ns-key] conj (MapEntry. target-key (val o)))
                       ns-sep
                       default-ns)))

  (empty [this] (ConfigResolver. {} ns-sep default-ns))
  (equiv [this o] (= value o))
  (count [this] (apply + (map #(count (val %1)) value)))

  Seqable
  (seq [this]
    (letfn [(ns-entry [ns-key [child-key cv]]
              (let [ns-ckey (external-key [ns-key child-key] ns-sep) ]
                (MapEntry. ns-ckey cv)))
            (ns-maps [[ns-key m]] (map (partial ns-entry ns-key) m))]
      (mapcat ns-maps value)))

  Iterable
  (iterator [this] (SeqIterator. (. this seq))))

(defn config-resolver
  ([m] (config-resolver m "::" "user"))
  ([m ns-sep default-ns]
     (mapstache mustache-renderer (ConfigResolver. m ns-sep default-ns))))
