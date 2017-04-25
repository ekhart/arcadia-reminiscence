;; what return f(x)?
; f(x) = x + 2 - pure function - use only arguments
; we know that would be always x+2
; referential transparency

; a = 2
; f(x) = x + a
; we dont know anything!



;; clojure
(def a 1) ; immutable by default
(set! a 2)
; (def ^:dynamic aa 1)
; aa

;; (def a 2)
;; a

(def b 2)
(def my-list [-1 0 1])

(defn square [x]
  (* x x))

(square b)

(defn my-zero? [x]
  (= x 0))

; higer order function
(filter my-zero? my-list)
(filter (fn [x] (= 0 x)) my-list) ; anonymous function
(filter #(= 0 %) my-list)

; partial create new curried function
(def plus-one (partial + 1))
(plus-one 1)
(map plus-one my-list)
; my-list

; comp compose functions called in reverse order
(comp square plus-one)
(def square-plus-one (comp square plus-one))
(map square-plus-one my-list)

;; lazy list
identity
;; (identity 1)
(take 1 my-list)
;; (take 3 (range))
;; in LightTable
; (range)
; (range 2)


;; Lisp macro
(defmacro infix [e]
  (list (second e) (first e) (last e)))

(infix (1 + 1))
(macroexpand '(infix (1 + 1)))


(defmacro when [test & body]
  (list 'if test (cons 'do body)))

(when (= 1 1) 1)
(when (= 1 0) 1)
(macroexpand '(when (= 1 1) 1))
(macroexpand '(when (= 1 0) 1))
