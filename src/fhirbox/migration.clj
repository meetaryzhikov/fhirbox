(ns fhirbox.migration
  (:require [next.jdbc :as jdbc]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn get-migration-files []
  (let [dir (io/file "resources/sql")]
    (->> (.listFiles dir)
         (filter #(.endsWith (.getName %) ".sql"))
         (sort-by #(.getName %)))))

(defn execute-migration [ds file]
  (let [version (.getName file)
        content (slurp file)
        statements (str/split content #";")]
    (println (str "Executing migration: " version))
    (doseq [stmt statements]
      (let [trimmed (str/trim stmt)]
        (when-not (str/blank? trimmed)
          (jdbc/execute! ds [trimmed]))))
    (try
      (jdbc/execute! ds ["INSERT INTO schema_migrations (version) VALUES (?)" version])
      (catch Exception _ nil))))

(defn run-migrations [ds]
  (doseq [file (get-migration-files)]
    (execute-migration ds file)))