(ns jasmine-cljs.macros
  (:require [clojure.string :as s]))

(defn key->method [k] 
  (let [words (s/split (name k) #"[\s_-]+")] 
    (symbol (s/join "" (cons (s/lower-case (str "." (first words)))
                             (map s/capitalize (rest words)))))))

(defn matchers [matcher]
  (let [m {:to-be-nil '.toBeNull}]
    (or (m matcher)
        (key->method matcher))))

(defmacro describe
  [desc & body]
  `(js/describe ~desc (fn [] ~@body)))

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
