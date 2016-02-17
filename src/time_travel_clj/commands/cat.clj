(ns time-travel-clj.commands.cat
  (:require [time-travel-clj.filesystem :as ttf])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn execute [args]
  (try+ 
    (let [path (first args)
          output (ttf/read-file path)]
      (println output))
    (catch [:type :path-not-exist] {:keys [msg]}
      (println msg))))
