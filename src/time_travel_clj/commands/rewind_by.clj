(ns time-travel-clj.commands.rewind-by
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute [i]
  (ttf/rewind-by! (read-string (first i))))
