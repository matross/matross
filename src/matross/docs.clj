(ns matross.docs
  (:require [matross.util :refer [load-plugins!]]
            [matross.tasks.core :refer [run-task]]
            [matross.connections.core :refer [get-connection]]))

(defn doc-path [{:keys [type name]}]
  "Find the documentation path from the doc data"
  (let [doc-root (case type
                   :task "docs/task_plugins"
                   :connection "docs/connection_plugins")]
    (str doc-root "/" name ".rst")))

(defn template-path [{:keys [type]}]
  "Find the template for the documentation type"
  (case type
    :task "resources/templates/task_doc.rst.mustache"
    :connection "resources/templates/connection_doc.rst.mustache"))

(defn prepare-task-documentation [{:keys [name description examples options url defaults] :as doc}]
  {:name name
   :description description
   :url url
   :examples (map (fn [ex] {:example (str ex)}) examples)
   :options (map (fn [[k v]]
                   (let [default (if (k defaults) (str " - default: ``" (k defaults) "``"))]
                     {:key (clojure.core/name k)
                      :description (str v default)})) options)}) 

(defn prepare-documentation [doc]
  (case (:type doc)
    :task (prepare-task-documentation doc)
    :connection doc))

(def documentation (atom {}))
(defn defdocs [key docs]
  (let [name (name key)
        type (or (:type docs) :task)
        docs (assoc docs :name name :type type)]
    (swap! documentation assoc key docs)))

(defn template-doc! [conn doc-key]
  (let [doc (-> documentation deref doc-key)]
    (run-task conn {:type :template 
                    :dest (doc-path doc)
                    :file (template-path doc)
                    :vars (prepare-documentation doc)})
    (println "wrote:" (doc-path doc))))

(defn -main []
  "write out all docs registered in the system"
  (load-plugins!)
  (doseq [k (keys @documentation)]
    (template-doc! (get-connection {:type :local}) k))
  (System/exit 0))
