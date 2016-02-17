(ns time-travel-clj.commands.ls
  (:require [time-travel-clj.filesystem :as ttf]
            [clojure.string :as str])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(defn -format-output [full-paths]
  (map #(if (ttf/is-dir? %1)
          (str (ttf/base-name %1) "/") 
          (ttf/base-name %1))
    full-paths))

(defn execute [args]
  (try+ 
    (let [path (if (first args) (first args) "/")
          out-list (ttf/read-dir path)
          out-list-full-paths (map #(str path "/" %1) out-list)
          out-list (-format-output out-list-full-paths)]
      (dorun
        (for [f out-list]
          (do 
            (print (str f " ")))))
      (println ""))
    (catch [:type :path-not-exist] {:keys [msg]}
      (println msg))))
