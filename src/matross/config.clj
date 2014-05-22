(ns matross.config
  (:require [matross.mapstache :refer [IRender mapstache no-template]]
            [matross.crosshair :refer [crosshair]]
            [stencil.core :as stencil]))

(def mustache-renderer
  (reify IRender
    (render [this s data] (stencil/render-string s data))))

(defn config-resolver
  ([m] (mapstache mustache-renderer (crosshair m "/" "var"))))
