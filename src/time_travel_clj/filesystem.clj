(ns time-travel-clj.filesystem
  (:require [clojure.string :as str]))

(def ^:dynamic filesystem-history (atom [{}]))
(def ^:dyanmic current-position (atom 0))

(defn vectorize-path [path]
  "Converts a file path into a vector
   Ex: /home/bob => [:home :bob]"
  (mapv keyword (filterv not-empty (str/split path #"/+"))))

(defn -add-dir-to-fs-map [fs-map file-vec]
  (assoc-in fs-map file-vec {}))

(defn -del-dir-from-fs-map [fs-map file-vec]
  (update-in fs-map (drop-last file-vec) dissoc (last file-vec)))

(defn at-latest-filesystem-version? []
  (= @current-position (dec (count @filesystem-history))))

(defn -update-filesystem! [filesystem]
  (if (at-latest-filesystem-version?)
    (do 
      (reset! filesystem-history (conj @filesystem-history filesystem))
      (reset! current-position (inc @current-position)))
    (throw (Exception. "Cannot write to filesystem while time is rewinded"))))

(defn get-filesystem []
  (@filesystem-history @current-position))

(defn get-filesystem-history [] @filesystem-history)

(defn get-filesystem-history-size []
  (count @filesystem-history))

(defn within-filesystem-history-bounds? [i]
  (and (>= i 0) (< i (get-filesystem-history-size))))

(defn rewind-by! [i]
  (let [pos (- @current-position i)]
    (if (within-filesystem-history-bounds? pos)
      (reset! current-position pos)
      (throw (Exception. "Can't rewind by that amount")))))

(defn fast-forward-by! [i]
  (let [pos (+ @current-position i)]
    (if (within-filesystem-history-bounds? pos)
      (reset! current-position pos)
      (throw (Exception. "Can't fast forward by that amount")))))

(defn dir-exists? [path]
  (if (get-in (get-filesystem) (vectorize-path path))
    true
    false))

(defn create-dir! [path]
  (if (dir-exists? path)
    (throw (Exception. (str path " already exists")))
    (-update-filesystem! (-add-dir-to-fs-map (get-filesystem) (vectorize-path path)))))

(defn rm-dir! [path])

