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
                   (println "vamp-key" vamp-key "value" value)
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  "Return a seq of maps whose glitter-index is at or above the min value."
  [records min-glitter]
  (filter #(>= (:glitter-index %) min-glitter) records))

(defn -main [& args]
  (->
    (slurp filename)
    (parse)
    (mapify)
    (glitter-filter 3)))