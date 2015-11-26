(ns fwpd.core-test
  (:require [clojure.test :refer :all]
            [fwpd.core :refer :all]))

(deftest convert-tests
  (testing "given a name key, returns value of type string"
    (is (string? (convert :name "test name"))))
  (testing "given a glitter-index value, returns value of type Integer"
    (is (number? (convert :glitter-index "10")))))

(deftest parse-tests
  (testing "given a string, returns a seq of vector entries"
    (is (= [["Edward Cullen" "10"] ["Bella Swan" "0"]] (parse "Edward Cullen,10\nBella Swan,0\n")))))

(deftest mapify-tests
  (testing "given a seq of row vectors, return a seq of maps"
    (let [rows [["Edward Cullen" "10"] ["Bella Swan" "0"] ["George Buttz" "5"]]
          expected [{:name "Edward Cullen" :glitter-index 10}
                    {:name "Bella Swan" :glitter-index 0}
                    {:name "George Buttz" :glitter-index 5}]]
      (is (= expected (mapify rows))))))

(deftest glitter-filter-tests
  (testing "given a minimum glitter-index, returns seq of maps passing filter"
    (let [maps [{:name "Edward Cullen" :glitter-index 10}
                {:name "Bella Swan" :glitter-index 0}
                {:name "George Buttz" :glitter-index 5}]]
      (is (= ["Edward Cullen"]
             (glitter-filter maps 6)))
      (is (= ["Edward Cullen" "George Buttz"]
             (glitter-filter maps 5))))))

(deftest append-tests
  (testing "given a seq of maps of suspects, adds another to the seq"
    (let [suspects [{:name "s1" :glitter-index 3}]]
      (is (= [{:name "s1" :glitter-index 3}
              {:name "added" :glitter-index 2}]
             (append suspects {:name "added" :glitter-index 2}))))))

(deftest valid?-tests
  (is (valid? validators :name "n1"))
  (is (valid? validators :glitter-index 9))
  (is (not (valid? validators :name nil)))
  (is (not (valid? validators :name "")))
  (is (not (valid? validators :glitter-index nil)))
  (is (not (valid? validators :glitter-index -1)))
  (is (not (valid? validators :glitter-index "harry"))))

(deftest valid-record?-tests
  (testing "given a good map, returns true"
    (is (valid-record? validators {:name "n1" :glitter-index 0})))
  (testing "given a bad map, returns false"
    (is (not (valid-record? validators {:name nil :glitter-index 1})))))
