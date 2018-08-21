(ns moms.storage
  (:require
    [clojure.java.io :as io]))

(def db-file (io/file "./DB.edn"))


(defn spit-mkdirs [f content]
  (io/make-parents f)
  (spit f content))

(defn save-db [s]
  (spit-mkdirs db-file s))

(defn get-db []
  (slurp db-file))


