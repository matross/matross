(defproject matross/matross "0.1.0-SNAPSHOT"
  :aot :all
  :url "http://github.com/matross/matross"
  :license {:name "MIT"
            :url "https://github.com/matross/matross/blob/master/LICENSE"}
  :main matross.core
  :test-paths ["test" "plugins"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.clojure/tools.reader "0.8.4"]
                 [matross/crosshair "0.1.0-SNAPSHOT"]
                 [matross/mapstache "0.2.0-SNAPSHOT"]
                 [matross/strata "0.1.0-SNAPSHOT"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [com.novemberain/validateur "2.1.0"]
                 [me.raynes/conch "0.6.0"]
                 [me.raynes/fs "1.4.5"]
                 [clj-ssh "0.5.7"]
                 [stencil "0.3.3"]]
  :plugins [[perforate "0.3.3"]]
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)})
