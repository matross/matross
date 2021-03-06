(defproject matross/matross "0.1.0-SNAPSHOT"
  :aot :all
  :url "http://github.com/matross/matross"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main matross.core

  :test-paths ["test" "plugins"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.clojure/tools.reader "0.8.4"]
                 [matross/crosshair "0.1.2"]
                 [matross/mapstache "0.2.1"]
                 [matross/strata "0.1.1"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [com.novemberain/validateur "2.1.0"]
                 [me.raynes/conch "0.6.0"]
                 [me.raynes/fs "1.4.5"]
                 [clj-ssh "0.5.7"]
                 [stencil "0.3.3"]]

  :pom-addition [:developers 
                 [:developer
                  [:id "zeroem"]
                  [:name "Darrell Hamilton"]
                  [:url "https://github.com/zeroem"]]
                 [:developer
                  [:id "eggsby"]
                  [:name "Thomas Omans"]
                  [:url "https://github.com/eggsby"]]]

  :plugins [[perforate "0.3.3"]]

  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)})
