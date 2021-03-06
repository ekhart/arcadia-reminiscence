(ns game.core
  (use arcadia.core
       arcadia.linear)
  (import [UnityEngine 
           Resources 
           Quaternion
           Animator
           Sprite]
          [UnityEngine.UI Image RawImage]))


(log "Hello, from game.core")

(defn resource [name] 
  (UnityEngine.Resources/Load name))

;(def cards-parent (UnityEngine.GameObject. "Cards"))

;; (UnityEngine.GameObject.)

(def canvas (object-named "Canvas"))
(def card-prefab (resource "card"))

(defn card-add [i]
  (let [position (v3 (+ (- 300) (* i 100)) 0 0)
        card (instantiate card-prefab position)]
         (child+ canvas card)))

(defn generate-level [no]
  (dotimes [i (* 2 no)]
    (card-add i)))

(generate-level 2)

(defn card [] (object-tagged "Card"))
(defn cards [] (objects-tagged "Card"))

(defn card-update-type [card res-name]
  (update-state! card 
                 :type 
                 (fn [type] 
                   (keyword res-name))))

(defn card-set-face-texture! [card res-name]
  (let [face (second (children card))
        rawimage (cmpt face UnityEngine.UI.RawImage)]
    (set! (.texture rawimage)
          (resource res-name))
    (card-update-type card res-name)))

(def card-faces ["robot" "roboty-drogowe"])
(defn rand-card-face 
  ([] (rand-nth card-faces))
  ([_] (rand-card-face))
  ([coll face] ))

;; (shuffle (flatten (map #(repeat 2 (keyword %)) card-faces)))
(doseq [[card face] ; ~foreach, destruction
        (->> card-faces ; ->> pipe operator
             (map #(repeat 2 %))
             flatten
             shuffle
             (zipmap (cards)))]
  (card-set-face-texture! card face))

;; (map rand-card-face 
;;      (range (* 2 (count card-faces))))
;; (reduce conj 
;;         []
;;         (range (* 2 (count card-faces))))

;; (card-set-face-texture! (card) (rand-card-face))

;; (UnityEngine.Resources/Load "roboty-drogowe")
;; (UnityEngine.Resources/Load 
;;  (type-args UnityEngine.Sprite)
;;  "roboty-drogowe")
;; => return the same as without type-args

(defn do-cards [do-card]
  (doseq [card (cards)]
    (do-card card)))

;(remove-state! card-prefab :rotate?)
;; (do-cards #(do
;;              (set-state! % :flipped? false)
;;              (set-state! % :type :robot)))

;; (do-cards #(println (state %)))
;; ;(set-state! card-prefab :flipped? false)
;(set-state! card-prefab :rotate? false)
;(update-state! card :rotate? (constantly true))


;; (defn rotate-card [go]
;;   (let [face (second (children card))
;;         flipped? (state go :flipped?)]
;;     (.SetActive face flipped?)))
;;   ;; (second (children card))
  ;; (if (state go :rotate?)
  ;;   (.. go transform (Rotate 0 -1 0))))

;; (def face (second (children card)))
;; (.name face)
;; (.activeSelf face)
;; (.SetActive face false)

;; (def animator (cmpt card UnityEngine.Animator))
;; (.snapShot animator)

(defn card-flip! [go]
  (let [animator (cmpt go UnityEngine.Animator)
        flipped? (state go :flipped?)
        trigger (if flipped? "flipBack" "flip")]
    (update-state! go :flipped? #(not %))
    (.SetTrigger animator trigger)))

(defn cards-flipped []
  (filter #(state % :flipped?) (cards)))

(defn cards-destroy [cards]
  (doseq [card cards]
    (destroy card)))

(defn cards-flip-back! [cards]
  (doseq [card cards] 
    (card-flip! card)))

(defn cards-same []
  (map #(state % :type) (cards-flipped)))

(defn list-all-elements-same? [coll]
  (apply = coll))

(defn cards-are-same? []
  (let [cards (cards-same)]
    (and (= (count cards) 2)
         (list-all-elements-same? cards))))

(defn cards-flip-back!-when-2 []
  (if (= (count (cards-flipped)) 
         2)
      (if (cards-are-same?)
        (cards-destroy (cards-flipped))
        (cards-flip-back! (cards-flipped)))))

(defn set-rotate?-card [go]
  (card-flip! go)
  (cards-flip-back!-when-2)
  ;; (if (empty? (cards))
  ;;   (gene))
)

;; (doseq [card cards] 
;;   (hook+ card
;;          :update 
;;          #'game.core/rotate-card))

(do-cards #(hook+ %
                  :on-mouse-down 
                  #'game.core/set-rotate?-card))
