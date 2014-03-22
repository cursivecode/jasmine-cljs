(ns jasmine-cljs.macros
  (:require [clojure.string :as s]))

(defn key->method [k] 
  (let [words (s/split (name k) #"[\s_-]+")] 
    (symbol (s/join "" (cons (s/lower-case (str "." (first words)))
                             (map s/capitalize (rest words)))))))

(defmacro cljs-matchers []
  `'(cljs.core/js-obj
      "cljsToEqual" (fn [expected#]
                      (cljs.core/this-as t# (= (.-actual t#) expected#)))
      "cljsToBeTruthy" (fn [expected#]
                         (cljs.core/this-as t# (boolean (.-actual t#))))
      "cljsToBeFalsy" (fn [expected#]
                        (cljs.core/this-as t# (not (boolean (.-actual t#)))))))

(defn matchers [matcher]
  (let [m {:to-be-nil '.toBeNull
           :to-equal '.cljsToEqual
           :to-equal-js '.toEqual
           :to-be-truthy '.cljsToBeTruthy
           :to-be-truthy-js '.toBeTruthy
           :to-be-falsy '.cljsToBeFalsy
           :to-be-falsy-js '.toBeFalsy}]
    (or (m matcher)
        (key->method matcher))))

(defmacro describe
  [desc & body]
  `(js/describe
     ~desc
     (fn []
       (js/beforeEach
         (fn []
           (cljs.core/this-as t# (.addMatchers t# ~(cljs-matchers)))))
       ~@body)))

(defmacro it
  [title & body]
  (if (vector? (first body))
    `(js/it ~title (js/inject (fn ~(first body) ~@(rest body))))
    `(js/it ~title (fn [] ~@body))))

(defmacro xdescribe
  [desc & body]
  `(js/xdescribe ~desc (fn [] ~@body)))

(defmacro xit
  [title & body]
  `(js/xit ~title (fn [] ~@body)))

(defmacro expect
  [value matcher & answer]
  (if answer
    `(-> (js/expect ~value)
         (~(matchers matcher) ~@answer))
    `(-> (js/expect ~value)
         ~(matchers matcher))))

(defmacro dont-expect
  [value matcher & answer]
  (if answer
    `(-> (js/expect ~value)
         .-not
         (~(matchers matcher) ~@answer))
    `(-> (js/expect ~value)
         .-not
         ~(matchers matcher))))

(defmacro before-each
  [& body]
  (if (vector? (first body))
    `(js/beforeEach (js/inject (fn ~(first body) ~@(rest body))))
    `(js/beforeEach (fn [] ~@body))))

(defmacro after-each
  [& body]
  `(js/afterEach (fn [] ~@body)))

(defmacro runs
  [& body]
  `(js/runs (fn [] ~@body)))

(defmacro waits-for
  [& body]
  `(js/waitsFor (fn [] ~@body)))

(defmacro set-timeout
  [& body]
  `(js/setTimeout (fn [] ~@body)))
