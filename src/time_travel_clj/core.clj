(ns time-travel-clj.core
  (:require [clojure.java.shell :only [sh]]
            [clojure.string :as str]))

(def promptStr "time-travel-clj => ")

(defn parse-cmd [cmd]
  (let [arr (str/split cmd #"\s")]
  {:cmd (first arr) :args (rest arr)}))

(defn get-cmd [cmd-map]
  (:cmd cmd-map))

(defn get-args [cmd-map]
  (:args cmd-map))

(defn cmd-echo [string]
  (println string))

(defn not-a-cmd [cmd]
  (println (str " -time-travel-clj: " cmd ": command not found")))

(defn -main [& args]
  (while true
    (print promptStr)
    (flush)
    (let [input (read-line)
          cmd-map (parse-cmd input)]
      (case (get-cmd cmd-map)
        "echo" (cmd-echo (get-args cmd-map))
        (not-a-cmd (get-cmd cmd-map))))))
