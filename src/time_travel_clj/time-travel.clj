(require '[clojure.java.shell :only [sh]])
(require '[clojure.string :as str])

(import 'java.lang.Runtime)
(import 'java.io.InputStream)
(import 'java.io.InputStreamReader)
(import 'java.io.OutputStream)
(import 'java.io.BufferedReader)

;(defn shell-out [cmd] 
;  (let [process (. (Runtime/getRuntime) exec cmd)
;        input-stream (.getInputStream process)
;        input-reader (new InputStreamReader input-stream)
;        buffer-reader (new BufferedReader input-reader)]
;    (loop [output []]
;      (if-let [out (.readLine buffer-reader)]
;        (recur (concat output out))
;        (apply str output)))))
;        
(def promptStr "time-travel-clj => ")

(defn main []
  (while true
    (print promptStr)
    (flush)
    (let [input (read-line)]
      (println "you said " input))))

(main)
