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
