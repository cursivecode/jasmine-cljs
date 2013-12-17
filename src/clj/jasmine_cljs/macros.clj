(ns jasmine-cljs.macros)

(def matchers {:to-equal '.toEqual
               :to-be '.toBe
               :to-match '.toMatch
               :to-be-undefined '.toBeUndefined
               :to-be-defined '.toBeDefined
               :to-be-nil '.toBeNull
               :to-be-truthy '.toBeTruthy
               :to-be-falsey '.toBeFalsy
               :to-contain '.toContain
               :to-be-less-than '.toBeLessThan
               :to-be-greater-than '.toBeGreaterThan
               :to-throw '.toThrow
               :to-be-close-to '.toBeCloseTo})

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
  [value matcher & [answer]]
  (if answer
    `(-> (js/expect ~value)
         .-not
         (~(matchers matcher) ~answer))
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
