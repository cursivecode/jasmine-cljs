(defproject jasmine-cljs "0.1.1"
  :description "Library for working with jasmine"
  :url "http://github.com/cursivecode/jasmine-cljs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2120"]
                 ]

  :min-lein-version "2.0.0"
  :source-paths ["src/clj"]
  :cljsbuild {:builds {:cljstest {:source-paths ["src/cljs/tests"]
                              :compiler {:output-to "resources/public/js/tests.js"
                                         :optimizations :simple
                                         :pretty-print true}}}
              })
