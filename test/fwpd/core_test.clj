(ns fwpd.core-test
  (:require [clojure.test :refer :all]
            [fwpd.core :refer :all]))

(deftest convert-tests
  (testing "given a name key, returns value of type string"
    (is (string? (convert :name "test name"))))
  (testing "given a glitter-index value, returns value of type Integer"
    (is (number? (convert :glitter-index "10")))))
