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

(defn -main [& args]
  (filtered-maps-from-file))