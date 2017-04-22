(ns game.core
  (use arcadia.core
       arcadia.linear)
  (import [UnityEngine 
           Resources 
           Quaternion
           Animator]))


; (log "Hello, from game.core")

(def cards-parent (UnityEngine.GameObject. "Cards"))
(def card-prefab (UnityEngine.Resources/Load "card"))

;(remove-state! card-prefab :rotate?)
;(set-state! card-prefab :flipped? false)
;(set-state! card-prefab :rotate? false)
;(update-state! card :rotate? (constantly true))

(child+ cards-parent 
        (instantiate card-prefab))
(child+ cards-parent
        (instantiate card-prefab (v3 2 0 0)))

(def card (object-tagged "Card"))
(def cards (objects-tagged "Card"))

(defn rotate-card [go]
  (if (state go :rotate?)
    (.. go transform (Rotate 0 -1 0))))

(defn set-rotate?-card [go]
  (let [animator (cmpt go UnityEngine.Animator)
        flipped? (state go :flipped?)
        trigger (if flipped? "flipBack" "flip")]
    (update-state! go :flipped? #(not %))
    (.SetTrigger animator trigger)))


(doseq [card cards] 
  (hook+ card
         :update 
         #'game.core/rotate-card))

(doseq [card cards]
  (hook+ card
         :on-mouse-down 
         #'game.core/set-rotate?-card))
