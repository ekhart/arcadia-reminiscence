(import 'System.Linq.Enumerable)
(def r1 (Enumerable/Where [1 2 3 4 5] even?))
(seq r1)

(def ev (sys-func [Int32 Boolean] [x] (even? x)))
(ev 1)

(def r3 (Enumerable/Repeat (type-args Single) 2 5))
(seq r3)
