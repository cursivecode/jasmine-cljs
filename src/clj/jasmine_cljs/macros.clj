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
  "args: description -> body"
  [desc & body]
  `(js/describe ~desc (fn [] ~@body)))

(defmacro it
  "args: title -> body"
  [title & body]
  `(js/it ~title (fn [] ~@body)))

(defmacro xdescribe
  "args: description -> body"
  [desc & body]
  `(js/xdescribe ~desc (fn [] ~@body)))

(defmacro xit
  "args: title -> body"
  [title & body]
  `(js/xit ~title (fn [] ~@body)))

(defmacro expect
  "args: body"
  [value matcher & answer]
  (if answer
    `(-> (js/expect ~value)
         (~(matchers matcher) ~@answer))
    `(-> (js/expect ~value)
         ~(matchers matcher))))

(defmacro dont-expect
  "args: body"
  [value matcher & answer]
  (if answer
    `(-> (js/expect ~value)
         .-not
         (~(matchers matcher) ~@answer))
    `(-> (js/expect ~value)
         .-not
         ~(matchers matcher))))

(defmacro before-each
  "args: body"
  [& body]
  `(js/beforeEach (fn [] ~@body)))

(defmacro after-each
  "args: body"
  [& body]
  `(js/afterEach (fn [] ~@body)))
