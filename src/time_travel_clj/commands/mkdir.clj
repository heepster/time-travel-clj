(ns time-travel-clj.commands.mkdir
  (:require [clojure.string :as str]
            [time-travel-clj.filesystem :as ttf])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn execute [path]
  (try+
    (ttf/create-dir! (first path))
    (catch [:type :filesystem-lock] _
      (println "Filesystem has been locked -- probably because you have fast forwarded or rewound time"))))
