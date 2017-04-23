(ns game.core
  (use arcadia.core
       arcadia.linear)
  (import [UnityEngine 
           Resources 
           Quaternion
           Animator]))


(log "Hello, from game.core")

;(def cards-parent (UnityEngine.GameObject. "Cards"))
(def canvas (object-named "Canvas"))
(def card-prefab (UnityEngine.Resources/Load "card"))

(child+ canvas
        (instantiate card-prefab (v3 -100 0 0)))
(child+ canvas
        (instantiate card-prefab (v3 100 0 0)))

(def card (object-tagged "Card"))
(defn cards [] (objects-tagged "Card"))

(defn do-cards [do-card]
  (doseq [card (cards)]
    (do-card card)))

;(remove-state! card-prefab :rotate?)
(do-cards #(do
             (set-state! % :flipped? false)
             (set-state! % :type :default)))

(do-cards #(println (state %)))
;(set-state! card-prefab :flipped? false)
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

(defn set-rotate?-card [go]
  (let [animator (cmpt go UnityEngine.Animator)
        flipped? (state go :flipped?)
        trigger (if flipped? "flipBack" "flip")]
    (update-state! go :flipped? #(not %))
    (.SetTrigger animator trigger)))

(defn cards-flipped []
  (filter #(state % :flipped?) (cards)))

(defn cards-flip-back! [cards]
  (do-cards set-rotate?-card))

(defn cards-same []
  (filter #(state % :type) 
          (cards-flipped)))

(defn cards-are-same? []
  (= (count (cards-same)) 2))

(defn cards-destroy [cards]
  (doseq [card cards]
    (destroy card)))

;; (cards-destroy (cards-flipped))

(defn cards-flip-back!-when-2 []
  (if (= (count (cards-flipped)) 2)
      (if (cards-are-same?)
        (cards-destroy (cards-same))
        (cards-flip-back! (cards-flipped)))))

(count cards)

(cards-flip-back!-when-2)

;; (doseq [card cards] 
;;   (hook+ card
;;          :update 
;;          #'game.core/rotate-card))

(doseq [card cards]
  (hook+ card
         :on-mouse-down 
         #'game.core/set-rotate?-card))
