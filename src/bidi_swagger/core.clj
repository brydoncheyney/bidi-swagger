(ns bidi-swagger.core
  (:require [clojure.tools.logging :as log]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response header]]
            [bidi.ring :refer [make-handler]]
            [scenic.routes :refer [scenic-handler load-routes-from-file]]
            [bidi-swagger.data :as data])
  (:gen-class))

(defn home-handler
  [_request]
  (response {:route "home"}))

(defn list-handler
  [_request]
  (response {:route "list" :items data/items}))

(defn item [id] (->> data/items (filter #(= id (:id %))) first))

(defn id
  [request]
  (when-let [id-str (-> request :params :id)]
    (try (Integer. id-str) (catch Exception e))))

(defn item-handler
  [request]
  (when-let [item (item (id request))]
    (response (assoc {:route "item"} :item item))))

(defn wrap-json-response-headers
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (header response "Content-Type" "application/json")
        response))))

(def handler-map
  {:home home-handler
   :list list-handler
   :item item-handler})

(def routes (load-routes-from-file "api.routes"))
(def handler (scenic-handler routes handler-map))

(defn log-handler-map
  [handler]
  (clojure.pprint/pprint routes)
  (fn [request] (handler request)))

(def api
  (-> handler
      (log-handler-map)
      (wrap-defaults site-defaults)
      (wrap-json-response-headers)
      (wrap-json-response)))

(defn -main [& args] (run-jetty api {:port 4000 :join? false}))
