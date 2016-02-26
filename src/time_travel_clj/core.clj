(ns time-travel-clj.core
  (:require [clojure.java.shell :only [sh]]
            [clojure.string :as str]
	      		[time-travel-clj.stdout :as out]
	      		[time-travel-clj.commands.echo :as echo]
            [time-travel-clj.commands.fast-forward-by :as fast-forward-by]
            [time-travel-clj.commands.rewind-by :as rewind-by]
            [time-travel-clj.commands.ls :as ls]
            [time-travel-clj.commands.mkdir :as mkdir]
            [time-travel-clj.commands.create-file :as create-file]
            [time-travel-clj.commands.rm :as rm]
            [time-travel-clj.commands.stay :as stay]
            [time-travel-clj.commands.cat :as cat]))

(def prompt-str "time-travel-clj $ ")
(def commands [ "echo" "ls" "cat" "mkdir" "create-file" "rm" "rewind-by" "fast-forward-by" "stay" "exit" ])
(def ^:dynamic prompt-loop (atom true))

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
  (while @prompt-loop
    (print prompt-str)
    (flush)
    (try
      (let [input (read-line)
            cmd-map (parse-cmd input)]
        (case (get-cmd cmd-map)
          "echo" (echo/execute (get-args cmd-map))
          "ls" (ls/execute (get-args cmd-map))
          "cat" (cat/execute (get-args cmd-map))
          "mkdir" (mkdir/execute (get-args cmd-map))
          "create-file" (create-file/execute (get-args cmd-map))
          "rm" (rm/execute (get-args cmd-map))
          "rewind-by" (rewind-by/execute (get-args cmd-map))
          "fast-forward-by" (fast-forward-by/execute (get-args cmd-map))
          "stay" (stay/execute)
          "help" (do 
                   (println "Available commands:\n")
                   (println (reduce str (map #(str % "\n") commands))))
          "exit" (reset! prompt-loop false)
          (not-a-cmd (get-cmd cmd-map))))
      (catch Exception e (println (str "Error: " (.getMessage e)))))))
