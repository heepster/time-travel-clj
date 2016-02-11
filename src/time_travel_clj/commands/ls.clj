(ns time-travel-clj.commands.ls
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute [args]
  (let [output (ttf/get-filesystem)]
    (println output)))
