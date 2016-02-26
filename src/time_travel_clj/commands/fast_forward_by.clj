(ns time-travel-clj.commands.fast-forward-by
  (:use [slingshot.slingshot :only [try+]])
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute [i]
  (try+
    (ttf/fast-forward-by! (read-string (first i)))
    (catch [:type :index-out-of-history-bounds] {:keys [msg]}
      (println "You can't fast forward by that amount"))))
