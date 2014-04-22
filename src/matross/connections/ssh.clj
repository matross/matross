(ns matross.connections.ssh
  (:require [clj-ssh.ssh :as ssh]))

(defn get-hostname [conn]
  "localhost")

(defn get-command [args]
  "whoami")

(defn get-ssh-config [conn]
  ;; supported all configuration supported by `ssh -o`
  ;; also supports: :port :username :password
  (let [defaults {:strict-host-key-checking :no}]
    (merge defaults conn)))

(defn get-identity [conn]
  ;; supported keys
  ;; https://github.com/hugoduncan/clj-ssh/blob/0.5.7/src/clj_ssh/ssh.clj#L190
  (let [defaults {:private-key-path "~/.ssh/id_rsa"
                  :public-key-path "~/.ssh/id_rsa.pub"}]
    (merge defaults conn)))

(defn validate [env conn args]
  true)

(defn format-result [result]
  (println result))

(defn create-session [conn]
  "Use the connection definition to create a jsch session"
  (let [agent  (ssh/ssh-agent {})
        host   (get-hostname conn)
        config (get-ssh-config conn)]
    (ssh/add-identity agent (get-identity conn))
    (ssh/session agent host config)))

(defn run [env conn args]
  (if (validate env conn args)
    (let [session (create-session conn)
          command (get-command args)]
      (ssh/with-connection session
        (format-result (ssh/ssh session {:cmd command}))))))


