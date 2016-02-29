(ns time-travel-clj.commands.create-file
  (:require [time-travel-clj.filesystem :as ttf])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn get-args [args]
  {:path (first args) :contents (apply str (map #(str % " ") (rest args)))})

(defn execute [args]
  (try+ 
    (let [{:keys [path contents]} (get-args args)]
      (ttf/create-file! path contents))
    (catch [:type :filesystem-lock] _
      (println "Filesystem has been locked -- probably because you have fast forwarded or rewound time"))
    (catch [:type :path-exists] {:keys [msg]}
      (println msg))))
