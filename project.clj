(defproject moms "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojure/core.async "0.2.374"]
                 [rum "0.6.0"]
                 [rum-reforms "0.4.3"]
                 [figwheel-sidecar "0.5.0"]
                 [com.cognitect/transit-clj  "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.232"]
                 [com.datomic/datomic-free "0.9.5350"]
                 [http-kit "2.1.19"]
                 [compojure "1.4.0" :exclusions [commos-codec]]
                 [cljs-ajax "0.5.8"]
                 [cheshire "5.3.1"]]





  :main moms.server
  ;:aot [moms.server]


  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.16"]]

  :source-paths ["src" "script"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src" "script"]

                :figwheel {:on-jsload "moms.core/on-js-reload"}

                :compiler {:main moms.core
                           :asset-path "/js/compiled/out"
                           :output-to "resources/public/js/compiled/moms.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}

                           ;:optimizations :whitespace}}
               ;; This next build is an compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src" "script"]
                :compiler {:output-to "resources/public/js/compiled/moms.js"
                           :main moms.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
              :server-port 4335 ;; default
             ;; :server-ip "127.0.0.1"

             :repl true
             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
              :ring-handler "moms.server/app"})

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

