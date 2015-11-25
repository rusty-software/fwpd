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