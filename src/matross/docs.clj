(ns matross.docs
  (:require [matross.util :refer [load-plugins!]]
            [matross.connections.core :refer [get-connection]]
            [clostache.parser :as template]))

(defn name-to-filename 
  ([n] (name-to-filename n "clj"))
  ([n ext] (str (clojure.string/replace n "-" "_") "." ext)))

(defn name-to-url [n]
  (str "https://github.com/matross/matross/blob/master/plugins/matross/tasks/" (name-to-filename n)))

(defn doc-path [{:keys [doc-type name]}]
  "Find the documentation path from the doc data"
  (let [doc-root (case doc-type
                   :task "docs/task_plugins"
                   :connection "docs/connection_plugins")]
    (str doc-root "/" (name-to-filename name "rst"))))

(defn template-path [{:keys [doc-type]}]
  "Find the template for the documentation type"
  (case doc-type
    :task "resources/templates/task_doc.rst.mustache"
    :connection "resources/templates/connection_doc.rst.mustache"))

(defn prepare-task-documentation [{:keys [name doc examples options url defaults] :as doc}]
  {:name name
   :doc doc
   :example "{{ example }}"
   :url (fn [_] (name-to-url name))
   :examples (map (fn [ex] {:example (str ex)}) examples)
   :options (map (fn [[k v]]
                   (let [default (if (k defaults) (str " - default: ``" (k defaults) "``"))]
                     {:key (clojure.core/name k)
                      :description (str v default)})) options)}) 

(defn prepare-documentation [doc]
  (case (:doc-type doc)
    :task (prepare-task-documentation doc)
    :connection doc))

(def documentation (atom {}))

(defn defdocs [md]
  (swap! documentation assoc (keyword (:name md)) md))

(defn template-doc! [conn doc-key]
  (let [doc (-> documentation deref doc-key)]
    (spit
     (doc-path doc)
     (template/render (slurp (template-path doc)) (prepare-documentation doc)))
    (println "wrote:" (doc-path doc))))

(defn -main []
  "write out all docs registered in the system"
  (load-plugins!)
  (doseq [k (keys @documentation)]
    (template-doc! (get-connection {:type :local}) k))
  (System/exit 0))
