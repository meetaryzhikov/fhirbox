(ns fhirbox.resource
  (:require [next.jdbc :as jdbc]
            [cheshire.core :as json]))

(defn create-resource! [ds resource-type resource-json]
  (let [logical-id (or (get resource-json "id") (str (java.util.UUID/randomUUID)))
        internal-id (java.util.UUID/randomUUID)
        now (.toString (java.time.Instant/now))
        meta {"versionId" "1" "lastUpdated" now}
        updated-resource (assoc resource-json "id" logical-id "meta" meta)
        data (json/generate-string updated-resource)]
    (jdbc/execute! ds ["INSERT INTO resource (id, resource_type, logical_id, version_id, data) VALUES (?, ?, ?, 1, ?::jsonb)" internal-id resource-type logical-id data])
    {:status 201
     :headers {"Location" (str "/" resource-type "/" logical-id)}
     :body updated-resource}))

(defn search-resources [ds resource-type query-params]
  (let [query (str "SELECT data FROM resource WHERE resource_type = ? AND status = 'active'")
        results (jdbc/execute! ds (into [query] [resource-type]))]
    {:status 200 :body {:resourceType "Bundle"
                        :type "searchset"
                        :entry (map #(hash-map :resource (json/parse-string (:resource/data %) true)) results)}}))

(defn get-resource-by-id [ds resource-type id]
  (let [result (jdbc/execute-one! ds ["SELECT data FROM resource WHERE resource_type = ? AND (data->>'id') = ? AND status = 'active'" resource-type id])]
    (if result
      {:status 200 :body (json/parse-string (:resource/data result) true)}
      {:status 404 :body {"resourceType" "OperationOutcome"
                          "issue" [{"severity" "error"
                                    "code" "not-found"
                                    "diagnostics" "Resource not found"}]}})))