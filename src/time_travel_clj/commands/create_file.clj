(ns time-travel-clj.commands.create-file
  (:require [time-travel-clj.filesystem :as ttf])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn get-args [args]
  {:path (first args) :contents (second args)})

(defn execute [args]
  (try+ 
    (let [{:keys [path contents]} (get-args args)]
      (ttf/create-file! path contents))
    (catch [:type :path-exists] {:keys [msg]}
      (println msg))))
