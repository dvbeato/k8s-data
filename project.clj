(defproject k8s-data "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-cljfmt "0.7.0"]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "1.0.0"]
                 [nubank/matcher-combinators "3.1.4"]
                 [prismatic/schema "1.1.12"]
                 [comb "0.1.1"]]
  :repl-options {:init-ns k8s-data.core})
