(ns fhirbox.core
  (:require [integrant.repl :as ig-repl]
            [fhirbox.system :as system]))

(ig-repl/set-prep! (constantly system/config))

(defn -main
  "Start the FHIR server"
  [& args]
  (ig-repl/go))
