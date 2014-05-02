(ns jasmine-cljs.main-test
  (:require-macros [jasmine-cljs.macros :refer [describe xit expect dont-expect
                                                before-each after-each waits-for
                                                xdescribe create-spy set-timeout
                                                it runs use-mock-clock after]]))

(describe "A suite"
  (it "contains spec with an expectation"
      (expect true :to-be true)))

(describe "A suite is just a function"
  (it "and so is a spec"
      (let [a true]
        (expect a :to-be true))))

(describe "expect"
  (it "has a positive case"
      (expect true :to-be true))
  (it "and a negative case"
      (dont-expect false :to-be true)))

(describe "Included matchers:"
  (it "The :to-be matcher compares with ==="
      (let [a 12
            b a]
        (expect a :to-be b)
        (dont-expect a :to-be nil)))

  (describe "The :to-equal matcher"
    (it "works for simple literals and variables"
        (let [a 12]
          (expect a :to-equal 12)))
    (it "works based on clojure equality semantics"
        (let [foo {"a" 12 "b" 34}
              bar (conj {"a" 12} {"b" 34})]
          (expect foo :to-equal bar))))

  (describe "The :to-equal-js matcher"
    (it "should work for js objects"
      (let [foo (js-obj "a" 12 "b" 34)
            bar (js-obj "a" 12 "b" 34)]
        (expect foo :to-equal-js bar))))

  (it "The :to-match matcher is for regular expressions"
      (let [message "foo bar baz"]
        (expect message :to-match (re-pattern "bar"))
        (expect message :to-match "bar")
        (dont-expect message :to-match (re-pattern "quux"))))

  (it "The :to-be-defined matcher compares against 'undefined'"
      (let [a (js-obj "foo" "foo")]
        (expect (aget a "foo") :to-be-defined)
        (expect (aget a "bar") :to-be-undefined)))

  (it "The :to-be-nil matcher compares against null"
      (let [a nil
            foo "foo"]
        (expect nil :to-be-nil)
        (expect a :to-be-nil)
        (dont-expect foo :to-be-nil)))

  (it "The :to-be-truthy matcher tests for clojure truthiness"
      (let [foo "foo"
            a nil]
        (expect foo :to-be-truthy)
        (dont-expect a :to-be-truthy)
        (expect 0 :to-be-truthy)))

  (it "The :to-be-truthy-js matcher test for javascript truthiness"
    (dont-expect 0 :to-be-truthy-js))

  (it "The :to-be-falsy matcher tests for clojure falsiness"
      (let [a nil
            foo "foo"]
        (expect a :to-be-falsy)
        (dont-expect foo :to-be-falsy)
        (dont-expect 0 :to-be-falsy)))

  (it "The :to-be-falsy-js matcher tests for javascript falsiness"
    (expect 0 :to-be-falsy-js))

  (it "The :to-contain matcher is for finding an item in an Array"
      (let [a (array "foo" "bar" "baz")]
        (expect a :to-contain "bar")
        (dont-expect a :to-contain "quux")))

  (it "The :to-be-less-than matcher is for mathematical comparisons"
      (let [pi 3.1415926
            e 2.78]
        (expect e :to-be-less-than pi)
        (dont-expect pi :to-be-less-than e)))

  (it "The 'toBeGreaterThan' is for mathematical comparisons"
      (let [pi 3.1415926
            e 2.78]
        (expect pi :to-be-greater-than e)
        (dont-expect e :to-be-greater-than pi)))

  (it "The :to-be-close-to matcher is for precision math comparison"
      (let [pi 3.1415926
            e 2.78]
        (dont-expect pi :to-be-close-to e 2)
        (expect pi :to-be-close-to e 0)))

  (it "The :to-throw matcher is for testing if a function throws an expection"
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

(describe "A spy, when created manually"
  (let [whatAmI (atom nil)]
    (before-each
      (reset! whatAmI (create-spy "whatAmI"))
      (@whatAmI "I" "am" "a" "spy"))

    (it "is named, which helps in error reporting"
      (expect (.-identity @whatAmI) :to-equal "whatAmI"))
    (it "tracks that the spy was called"
      (expect @whatAmI :to-have-been-called))
    (it "tracks its number of calls"
      (expect (.. @whatAmI -calls -length) :to-equal 1))
    (it "tracks all the arguments of its calls"
      (expect @whatAmI :to-have-been-called-with "I" "am" "a" "spy"))
    (it "allows access to the most recent call"
      (expect (-> @whatAmI (.. -mostRecentCall -args) (aget 0))
              :to-equal
              "I"))))

(describe "Multiple spies, when created manually"
  (let [tape (create-spy "tape" ["play" "pause" "stop" "rewind"])]
    (.play tape)
    (.pause tape)
    (.rewind tape 0)

    (it "creates spies for each requested function"
      (expect (.-play tape) :to-be-defined)
      (expect (.-pause tape) :to-be-defined)
      (expect (.-stop tape) :to-be-defined)
      (expect (.-rewind tape) :to-be-defined))

    (it "tracks that the spies were called"
      (expect (.-play tape) :to-have-been-called)
      (expect (.-pause tape) :to-have-been-called)
      (expect (.-rewind tape) :to-have-been-called)
      (dont-expect (.-stop tape) :to-have-been-called))

    (it "tracks all the arguments of its calls"
      (expect (.-rewind tape) :to-have-been-called-with 0))))

(describe "Manually ticking the Jasmine Mock Clock"
  (use-mock-clock)
  (it "causes a timeout to be called synchronously"
    (let [spy (create-spy)]
      (js/setTimeout spy 100)
      (dont-expect spy :to-have-been-called)
      (after 101
        (expect spy :to-have-been-called))))

  (it "causes an interval to be called synchronously"
    (let [spy (create-spy)]
      (js/setInterval spy 100)
      (dont-expect spy :to-have-been-called)
      (after 101
        (expect (.-callCount spy) :to-equal 1))
      (after 50
        (expect (.-callCount spy) :to-equal 1))
      (after 50
        (expect (.-callCount spy) :to-equal 2)))))
