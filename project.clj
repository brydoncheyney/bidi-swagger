(defproject bidi-swagger "0.1.0-SNAPSHOT"
  :description "Playground for using swagger with bidi"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [bidi "1.23.1"]
                 [scenic "0.2.5"]
                 [metosin/ring-swagger "0.22.1"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler bidi-swagger.core/api}
  :main ^:skip-aot bidi-swagger.core
  :target-path "target/%s"
  :profiles {:dev     {:plugins [[lein-midje "3.1.3"]
                                 [lein-ancient "0.6.7"]
                                 [lein-kibit "0.1.2"]
                                 [jonase/eastwood "0.2.1"]]}
             :uberjar {:aot :all}})
