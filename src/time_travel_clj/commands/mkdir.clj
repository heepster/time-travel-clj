(ns time-travel-clj.commands.mkdir
  (:require [clojure.string :as str]
            [time-travel-clj.filesystem :as ttf]))

(defn execute [path]
  (ttf/create-dir! (first path)))
