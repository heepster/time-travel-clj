(ns time-travel-clj.commands.rm
  (:require [clojure.string :as str]
            [time-travel-clj.filesystem :as ttf])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn execute [path]
  (try+
    (ttf/rm-file! (first path))
    (catch [:type :filesystem-lock] _
      (println "Filesystem has been locked -- probably because you have fast forwarded or rewound time"))
    (catch [:type :path-not-exist] {:keys [msg]}
      (println msg))))
