(ns matross.connections.ssh
  (:require [clj-ssh.ssh :as ssh]
            [matross.connections.core :refer [IConnection]]))

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

(defn get-sudo-user [conn]
  "root")

(defn get-sudo-pass [conn]
  (let [pass "root"]
    (str pass "\n")))

(defn run-as-sudo [env conn args]
  (let [session (create-session conn)
        command (get-command args)
        sudo-user (get-sudo-user conn)
        sudo-pass (get-sudo-pass conn)
        sudo-command (str "sudo -S -u " sudo-user " " command)]
    (ssh/with-connection session
      (format-result (ssh/ssh session {:cmd sudo-command
                                       :in sudo-pass})))))

(deftype SSH
  [conf ssh-session]
  IConnection

  (connect [self]
    (when-not (ssh/connected? ssh-session)
      (ssh/connect ssh-session)))

  (run [self command] (ssh/ssh ssh-session {:cmd "whoami"}))

  (get-file [self file-conf])

  (put-file [self file-conf])

  (disconnect [self] (ssh/disconnect ssh-session)))

(defn ssh-connection [conf] (new SSH conf (create-session conf)))
