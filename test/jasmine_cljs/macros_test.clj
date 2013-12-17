(ns jasmine-cljs.macros-test
  (:require [clojure.test :refer :all]
            [jasmine-cljs.macros :refer :all]))

(deftest matchers-test
  (testing "Each key value pair"
    (is (= '.toEqual (:to-equal matchers)))
    (is (= '.toBe (:to-be matchers)))
    (is (= '.toMatch (:to-match matchers)))
    (is (= '.toBeUndefined (:to-be-undefined matchers)))
    (is (= '.toBeDefined (:to-be-defined matchers)))
    (is (= '.toBeNull (:to-be-nil matchers)))
    (is (= '.toBeTruthy (:to-be-truthy matchers)))
    (is (= '.toBeFalsy (:to-be-falsey matchers)))
    (is (= '.toContain (:to-contain matchers)))
    (is (= '.toBeLessThan (:to-be-less-than matchers)))
    (is (= '.toBeGreaterThan (:to-be-greater-than matchers)))
    (is (= '.toThrow (:to-throw matchers)))
    (is (= '.toBeCloseTo (:to-be-close-to matchers)))))

