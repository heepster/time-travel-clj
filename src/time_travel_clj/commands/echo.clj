(ns time-travel-clj.commands.echo
  (:require [clojure.string :as str]
  			[time-travel-clj.stdout :as out]))

(defn parse-arg [string]
  (re-seq 
   #"\"[^\"]+\"|\'[^\']+\'|[^\"\']+" 
   string))

(defn generate-output [strarray]
  (let [^:dynamic output (atom[])]
   (dorun
    (for [word strarray]
     (if
      (or
       (= (first word) \")
       (= (first word) \'))
      (reset! output (conj
                      @output
     			      (subs word 1 (- (count word) 1))))
      (reset! output (conj @output word)))))
   (str/join @output)))

(defn execute [stringarr]
  (println
   (generate-output
    (parse-arg
	 (str/join " " stringarr)))))
