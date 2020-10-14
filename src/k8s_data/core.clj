(ns k8s-data.core
  (:require [schema.core :as s]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(defn read-api-file []
  (json/read-str (slurp (io/resource "swagger.json"))
                 :key-fn keyword))

(def k8s-api-definitions (get (read-api-file) :definitions))

(def spec-definition? (partial re-matches #".*Spec$"))

(defn filter-keys [pred? coll]
  (filter (fn [[k _]] (pred? (name k))) coll))

(def k8s-resource-specs (filter-keys spec-definition? k8s-api-definitions))
(def specs-ns (sort (keys k8s-resource-specs)))

(defn to-clj-file [api-def-schema]
  (let [full-path (str/split (name api-def-schema) #"\.")]
    [(str (->> full-path drop-last (str/join "/")) ".clj") (last full-path)]))

(defn ref->schema [ref-str]
  (-> (str/replace ref-str #"\#\/definitions/(.*)\.(.*)$" "k8s-data.$1/$2")
      symbol))

(defn type->schema [{:keys [type items] :as property-definition}]
  (let [type->schema-map {"string"  `s/Str
                          "integer" `s/Int
                          "boolean" `s/Bool}]
    (cond
      (#{"array"} type) [(type->schema items)]
      (:$ref property-definition) (ref->schema (:$ref property-definition))
      :else (get type->schema-map type))))

(defn api-property->schema [[property-name property-definition]]
  {property-name {:schema (type->schema property-definition)}})

(defn file-template [ns-name spec-name spec-content]
  (str '(ns ns-name)
       '(def spec-name spec-content)))

(defn create-file [[spec-name spec-props :as k8s-resource-spec]]
  (let [[filepath spec-name] (to-clj-file spec-name)
        fullpath (str "src/k8s_data/" filepath)]
    (io/make-parents fullpath)
    (spit fullpath (file-template filepath spec-name spec-props))))

(comment
  (create-file (nth k8s-resource-specs 9))
  )
