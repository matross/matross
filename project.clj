(defproject matross "0.1.0-SNAPSHOT"
  :aot :all
  :url "http://github.com/matross/matross"
  :license {:name "MIT"
            :url "https://github.com/matross/matross/blob/master/LICENSE"}
  :main matross.core
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [me.raynes/conch "0.6.0"]
                 [clj-ssh "0.5.7"]])
