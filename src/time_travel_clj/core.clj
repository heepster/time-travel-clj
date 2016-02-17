(ns time-travel-clj.core
  (:require [clojure.java.shell :only [sh]]
            [clojure.string :as str]
			[time-travel-clj.stdout :as out]
			[time-travel-clj.commands.echo :as echo]
            [time-travel-clj.commands.fast-forward-by :as fast-forward-by]
            [time-travel-clj.commands.rewind-by :as rewind-by]
            [time-travel-clj.commands.ls :as ls]
            [time-travel-clj.commands.mkdir :as mkdir]))

(def promptStr "time-travel-clj => ")

(defn parse-cmd [cmd]
  (let [arr (str/split cmd #"\s")]
  {:cmd (first arr) :args (rest arr)}))

(defn get-cmd [cmd-map]
  (:cmd cmd-map))

(defn get-args [cmd-map]
  (:args cmd-map))

(defn not-a-cmd [cmd]
  (println (str " -time-travel-clj: " cmd ": command not found")))

(defn -main [& args]
  (while true
    (print promptStr)
    (flush)
    (let [input (read-line)
          cmd-map (parse-cmd input)]
      (case (get-cmd cmd-map)
        "echo" (echo/execute (get-args cmd-map))
        "ls" (ls/execute (get-args cmd-map))
        "mkdir" (mkdir/execute (get-args cmd-map))
        "rewind-by" (rewind-by/execute (get-args cmd-map))
        "fast-forward-by" (fast-forward-by/execute (get-args cmd-map))
        (not-a-cmd (get-cmd cmd-map))))))
