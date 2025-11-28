(ns fhirbox.handler
  (:require [cheshire.core :as json]))

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

(defn app [request]
  (case (:uri request)
    "/" {:status 200
         :headers {"Content-Type" "text/plain"}
         :body "FHIR server is running"}
    "/metadata" {:status 200
                 :headers {"Content-Type" "application/fhir+json"}
                 :body (json/generate-string capability-statement)}
    {:status 404 :body "Not found"}))