(ns time-travel-clj.commands.rewind-by
  (:use [slingshot.slingshot :only [try+]])
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute [i]
  (try+
    (ttf/rewind-by! (read-string (first i)))
    (catch [:type :index-out-of-history-bounds] {:keys [msg]}
      (println "You can't rewind by that amount"))))
