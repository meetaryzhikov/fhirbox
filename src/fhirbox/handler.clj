(ns fhirbox.handler
  (:require [cheshire.core :as json]
            [reitit.ring :as ring]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :as cors]
            [fhirbox.resource :as resource]))

(def capability-statement
  {:resourceType "CapabilityStatement"
   :status "active"
   :date "2023-01-01"
   :kind "instance"
   :software {:name "fhirbox" :version "0.1.0"}
   :fhirVersion "4.0.1"
   :format ["json"]
   :rest [{:mode "server"
           :resource [{:type "Patient" :interaction [{:code "read"} {:code "create"}]}]}]})

(def routes
  [["/" {:get {:handler (fn [_] {:status 200
                                 :headers {"Content-Type" "text/plain"}
                                 :body "FHIR server is running"})}}]
   ["/metadata" {:get {:handler (fn [_] {:status 200
                                         :headers {"Content-Type" "application/fhir+json"}
                                         :body capability-statement})}}]
   ["/:resourceType" {:post {:handler (fn [request]
                                        (let [resource-type (get-in request [:path-params :resourceType])
                                              resource (:body-params request)]
                                          (resource/create-resource! (:db request) resource-type resource)))}}]])

(defn app [db request]
  (let [handler (-> (ring/ring-handler (ring/router routes {:conflicts nil}))
                    (middleware/wrap-json-body {:keywords? false})
                    (middleware/wrap-json-response)
                    (cors/wrap-cors :access-control-allow-origin [#".*"]
                                    :access-control-allow-methods [:get :post :put :delete :options]
                                    :access-control-allow-headers ["Content-Type" "Authorization"]))]
    (handler (assoc request :db db))))