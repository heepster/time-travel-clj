(ns time-travel-clj.commands.stay
  (:require [time-travel-clj.filesystem :as ttf]))

(defn execute []
  (if (ttf/at-latest-filesystem-version?)
    (println "Already at the latest filesystem version")
    (ttf/set-current-version-as-latest!)))
