(ns matross.input)

(defn get-sensitive-user-input [prompt]
  (let [console (System/console)
        padded-prompt (str prompt " ")]
    (if console
      (-> console (.readPassword padded-prompt nil) String/valueOf)
      (throw (new Exception "Could not find system console.")))))

;;;; previously in model.core
;;;; runner logic?
(defn get-passwords [opts]
  ;; this needs to happen before we make our connections
  ;; since it can be used to configure our connections
  (let [sudo (get-in opts [:options :ask-sudo-pass])
        pass  (get-in opts [:options :ask-password])]
    {:password (if pass (get-sensitive-user-input "password:"))
     :sudo-pass (if sudo (get-sensitive-user-input "sudo password:"))}))
