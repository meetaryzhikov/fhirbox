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