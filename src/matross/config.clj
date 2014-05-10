(ns matross.config
  (:require [matross.mapstache :refer [IRender mapstache]]
            [clostache.parser :as mustache]))

(def mustache-renderer (reify IRender
                 (render [this s data] (mustache/render s data))))

(defn template-map [m] (mapstache mustache-renderer m))
