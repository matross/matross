(defproject matross "0.1.0-SNAPSHOT"
  :aot :all
  :url "http://github.com/matross/matross"
  :license {:name "MIT"
            :url "https://github.com/matross/matross/blob/master/LICENSE"}
  :main matross.core
  :test-paths ["test" "plugins"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [me.raynes/conch "0.6.0"]
                 [me.raynes/fs "1.4.5"]
                 [clj-ssh "0.5.7"]
                 [de.ubercode.clostache/clostache "1.3.1"]])
