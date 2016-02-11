(ns time-travel-clj.commands.fast-forward-by
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute [i]
  (ttf/fast-forward-by! (read-string (first i))))
