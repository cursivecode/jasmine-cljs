# jasmine-cljs

jasmine-cljs is a library for working with the jasmine testing framework

## Installation

Add the following dependency to your `project.clj` file:

```
  [jasmine-cljs "0.1.4"]
```  

## Usage

```clojure
(ns yourapp
  (:require-macros [jasmine-cljs.macros :refer [describe it expect dont-expect
                                                before-each after-each xit xdescribe
                                                runs waits-for set-timeout]]))
```    

## jasmine-cljs - Jasmine Homepage
```clojure
(describe "A suite"
  (it "contains spec with an expectation"
      (expect true :to-be true)))

(describe "A suite is just a function"
  (it "and so is a spec"
      (let [a true]
        (expect a :to-be true))))

(describe "The toBe matcher compares with ==="
  (it "and has a positive case"
      (expect true :to-be true))
  (it "and can have a negative case"
      (dont-expect false :to-be true)))

(describe "Included matchers:"
  (it "The 'toBe' matcher compares with ==="
      (let [a 12
            b a]
        (expect a :to-be b)
        (dont-expect a :to-be nil)))

  (describe "The 'toEqual' matcher"
    (it "works for simple literals and variables"
        (let [a 12]
          (expect a :to-equal 12)))
    (it "should work for objects"
        (let [foo (js-obj "a" 12 "b" 34)
              bar (js-obj "a" 12 "b" 34)]
          (expect foo :to-equal bar))))

  (it "The 'toMatch' matcher is for regular expressions"
      (let [message "foo bar baz"]
        (expect message :to-match (re-pattern "bar"))
        (expect message :to-match "bar")
        (dont-expect message :to-match (re-pattern "quux"))))

  (it "The 'toBeDefined' matcher compares against 'undefined'"
      (let [a (js-obj "foo" "foo")]
        (expect (aget a "foo") :to-be-defined)
        (expect (aget a "bar") :to-be-undefined)))

  (it "The 'toBeNull' matcher compares against null"
      (let [a nil
            foo "foo"]
        (expect nil :to-be-nil)
        (expect a :to-be-nil)
        (dont-expect foo :to-be-nil)))

  (it "The 'toBeTruthy' matcher is for boolean casting testing"
      (let [foo "foo"
            a nil]
        (expect foo :to-be-truthy)
        (dont-expect a :to-be-truthy)))

  (it "The 'toBeFalsy' matcher is for boolean casting testing"
      (let [a nil
            foo "foo"]
        (expect a :to-be-falsy)
        (dont-expect foo :to-be-falsy)))

  (it "The 'toContain' matcher is for finding an item in an Array"
      (let [a (array "foo" "bar" "baz")]
        (expect a :to-contain "bar")
        (dont-expect a :to-contain "quux")))

  (it "The 'toBeLessThan' matcher is for mathematical comparisons"
      (let [pi 3.1415926
            e 2.78]
        (expect e :to-be-less-than pi)
        (dont-expect pi :to-be-less-than e)))

  (it "The 'toBeGreaterThan' is for mathematical comparisons"
      (let [pi 3.1415926
            e 2.78]
        (expect pi :to-be-greater-than e)
        (dont-expect e :to-be-greater-than pi)))

  (it "The 'toBeCloseTo' matcher is for precision math comparison"
      (let [pi 3.1415926
            e 2.78]
        (dont-expect pi :to-be-close-to e 2)
        (expect pi :to-be-close-to e 0)))

  (it "The 'toThrow' matcher is for testing if a function throws an expection"
      (let [foo (fn [] (+ 1 2))
            bar (fn [] (throw "exception"))]
        (dont-expect foo :to-throw)
        (expect bar :to-throw))))

(describe "A spec (with setup and tear-down)"
  (let [foo (atom 0)]
    (before-each 
     (swap! foo inc))
    (after-each
     (reset! foo 0))
    (it "is just a function, so it can contain any code"
        (expect @foo :to-equal 1))
    (it "can have more than one expectation"
        (expect @foo :to-equal 1)
        (expect true :to-equal true))))

(xdescribe "A disabled spec"
  (let [foo (atom 0)]
    (before-each
     (swap! foo inc))
    (xit "is just a function, so it can contain any code"
         (expect @foo :to-equal 1))))

(describe "Testing angular inject feature with it"
  (before-each (js/module "myApp"))

  (it "should see scope value" [$rootScope $controller]
      (let [scope (.$new $rootScope)
            ctrl ($controller "MyController" (js-obj "$scope" scope))]
        (expect (.-spice scope) :to-be "habanero"))))

(describe "Testing angular inject feature with before-each"
  (let [scope (atom 0)]
    (before-each (js/module "myApp"))

    (before-each [$rootScope $controller]
      (let [scope (swap! scope (fn [_] (.$new $rootScope)))
            ctrl ($controller "MyController" (js-obj "$scope" scope))]))

    (it "should see scope value"
        (expect (.-spice @scope) :to-be "habanero"))))

(describe "Asynchronous specs"
  (it "should support async execution of test preparation and expectations"
      (let [flag (atom false)
            value (atom 0)]
        (runs
         (set-timeout (reset! flag true) 500))
        (waits-for
         (reset! value 1) @flag "The Value should be incremented" 750)
        (runs 
         (expect @value :to-be-greater-than 0)))))
```

## Differences

* Matchers are keywords
* .not is (dont-expect)

## TODOS

* Port Spies
* Port Jasmine.any
* Port Jasmine.Clock

## License

Copyright © 2013 Michael Doaty

Distributed under the Eclipse Public License, same as Clojure.

 