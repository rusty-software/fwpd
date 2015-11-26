(ns fwpd.core
  (require [clojure.string :as str]))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  [contents]
  (map #(str/split % #",") (str/split contents #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-indes 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  "Return a seq of vamp names whose glitter-index is at or above the min value."
  [records min-glitter]
  (map :name (filter #(>= (:glitter-index %) min-glitter) records)))

(defn filtered-maps-from-file
  "Converts and filters data from the configured data file"
  []
  (->
    (slurp filename)
    (parse)
    (mapify)
    (glitter-filter 3)))

(def validators
  {:name (fn [n] (not (empty? n)))
   :glitter-index (fn [i] (and (number? i) (> i -1)))})

(defn valid?
  "Returns the validator function result for the given key and value"
  [validators-map vamp-key value]
  ((get validators-map vamp-key) value))

(defn valid-record?
  "Validates that the expected keys are present in the record"
  [validators record]
  (every? true? (for [[vamp-key validator] validators]
         (validator (vamp-key record)))))

(defn append
  "Appends a row to the seq of maps of suspects.  NOTE this does not append to the file."
  [suspects {:keys [name glitter-index]}]
  (conj suspects {:name name :glitter-index glitter-index}))

(defn -main [& args]
  (filtered-maps-from-file))