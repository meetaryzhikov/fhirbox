(ns fhirbox.system
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [fhirbox.migration :as migration]
            [fhirbox.handler :as handler]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key :fhirbox/db [_ {:keys [jdbc-url]}]
  (let [ds (jdbc/get-datasource jdbc-url)]
    (migration/run-migrations ds)
    ds))

(defmethod ig/halt-key! :fhirbox/db [_ ds]
  ;; HikariCP will close automatically, but if needed
  )

(defmethod ig/init-key :fhirbox/server [_ {:keys [port] :as opts}]
  (println "Starting server on port" port)
  (let [handler handler/app]
    (jetty/run-jetty handler {:port port :join? false})))

(defmethod ig/halt-key! :fhirbox/server [_ server]
  (.stop server))

(def config
  (ig/read-string (slurp "resources/config.edn")))