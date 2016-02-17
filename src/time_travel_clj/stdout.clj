(ns time-travel-clj.stdout
  (:require [clojure.string :as str]))

(def ^:dynamic buffer (atom []))

(defn write! [input]
  (reset! buffer (conj @buffer input))) 

(defn printout []
  (println (str/join " " @buffer))
  (reset! buffer []))
