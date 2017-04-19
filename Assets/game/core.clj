(ns game.core
  (use arcadia.core
       arcadia.linear)
  (import [UnityEngine 
           Resources 
           Quaternion]))


; (log "Hello, from game.core")

(def card-prefab (UnityEngine.Resources/Load "card"))
(instantiate card-prefab)
(instantiate card-prefab (v3 2 0 0))

; (def card (object-named "Card"))
(def cards (objects-tagged "Card"))

; (set-state! card :rotate? false)
; (state card)
; (update-state! card :rotate? (constantly true))

(defn rotate-card [go]
  (if (state go :rotate?)
    (.. go transform (Rotate 0 -1 0))))

(defn set-rotate?-card [go]
  (update-state! go :rotate? #(not %)))

(doseq [card cards] 
  (hook+ card
         :update 
         #'game.core/rotate-card))

(doseq [card cards]
  (hook+ card
         :on-mouse-down 
         #'game.core/set-rotate?-card))
