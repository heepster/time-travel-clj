(ns time-travel-clj.core)
(require '[clojure.java.shell :only [sh]]
         '[clojure.string :as str])

(def promptStr "time-travel-clj => ")

(defn -main [& args]
  (while true
    (print promptStr)
    (flush)
    (let [input (read-line)]
      (println "you said " input))))
