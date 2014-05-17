(ns matross.config
  (:require [matross.mapstache :refer [IRender mapstache no-template]]
            [matross.crosshair :refer [crosshair]]
            [stencil.core :as stencil])
  (:import clojure.lang.ILookup
           clojure.lang.Seqable
           clojure.lang.SeqIterator
           clojure.lang.IPersistentCollection
           clojure.lang.MapEntry))

(def mustache-renderer (reify IRender
                 (render [this s data] (stencil/render-string s data))))

(defn config-resolver
  ([m] (config-resolver m "/" "default"))
  ([m ns-sep default-ns]
     (mapstache mustache-renderer (crosshair m ns-sep default-ns))))
