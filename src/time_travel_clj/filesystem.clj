(ns time-travel-clj.filesystem
  (:use [slingshot.slingshot :only [throw+]])
  (:require [clojure.string :as str]))

(def ^:dynamic filesystem-history (atom [{}]))
(def ^:dyanmic current-position (atom 0))
(def DIRECTORY-TYPE "DIRECTORY")
(def FILE-TYPE "FILE")

(defn throw-exception! [exception-type & {:keys [msg] :or {msg ""}}]
  (throw+ {:type exception-type :msg msg}))

(defn vectorize-path [path]
  "Converts a file path into a vector
   Ex: /home/bob => [:home :bob]"
  (mapv keyword (filterv not-empty (str/split path #"/+"))))

(defn pathify-vector [vect]
  "Converts a path vector into a file path
   Ex: [:home :bob] => /home/bob"
   (str "/" (str/join "/" (map name vect))))

(defn base-path [path]
  "Given a full path like /home/bob/test, 
   returns a base path like /home/bob"
   (let [vec-path (vectorize-path path)
         without-last (drop-last vec-path)]
     (pathify-vector without-last)))

(defn base-name [path]
  "Returns the name of the file or directory
   given it's full path
   Ex: /home/bob/file.txt => file.txt
   Ex: /home/bob/another_dir => another_dir"
   (let [vec-path (vectorize-path path)]
     (name (last vec-path))))

(defn -add-to-fs-map [fs-map path-vec type-of & {:keys [contents] :or {contents ""}}]
  "Given a fs-map and a file path as a vector,
  adds contents to the fs-map using the vector
  as the destination.

  Defaults to adding an empty map if no contents are passed in."
  (if (= type-of DIRECTORY-TYPE)
    (assoc-in fs-map path-vec (with-meta {} {:type DIRECTORY-TYPE}))
    (assoc-in fs-map path-vec contents)))

(defn -del-from-fs-map [fs-map path-vec]
  "Given a fs-map and a file path as a vector,
   removes the contents from the fs-map at the vector destination"
  (update-in fs-map (drop-last path-vec) dissoc (last path-vec)))

(defn at-latest-filesystem-version? []
  (= @current-position (dec (count @filesystem-history))))

(defn -update-filesystem! [filesystem]
  (if (at-latest-filesystem-version?)
    (do 
      (reset! filesystem-history (conj @filesystem-history filesystem))
      (reset! current-position (inc @current-position)))
    (throw-exception! :filesystem-lock)))

(defn get-filesystem []
  (@filesystem-history @current-position))

(defn get-filesystem-history [] @filesystem-history)

(defn get-filesystem-history-size []
  (count @filesystem-history))

(defn within-filesystem-history-bounds? [i]
  "Given an integer, checks to see if that integer
   is within the filesystem history array size"
  (and (>= i 0) (< i (get-filesystem-history-size))))

(defn rewind-by! [i]
  "Moves back the current position by integer i
   Will raise an error if i is not within filesystem
   history bounds"
  (let [pos (- @current-position i)]
    (if (within-filesystem-history-bounds? pos)
      (reset! current-position pos)
      (throw-exception! :index-out-of-history-bounds))))

(defn fast-forward-by! [i]
  "Moves forward the current position by integer i
   Will raise an error if i is not within filesystem
   history bounds"
  (let [pos (+ @current-position i)]
    (if (within-filesystem-history-bounds? pos)
      (reset! current-position pos)
      (throw-exception! :index-out-of-history-bounds))))

(defn exists? [path]
  (if (get-in (get-filesystem) (vectorize-path path))
    true
    false))

(defn is-dir? [path]
  (=
    (:type (meta (get-in (get-filesystem) (vectorize-path path))))
    DIRECTORY-TYPE))
  
(defn is-file? [path]
  (not (is-dir? path)))
 
(defmacro path-exists-or-throw! [path form]
  "A macro that checks to see if path doesn't exists
   If so, an error is thrown
   if not, the form is executed"
  (list 'if (list not (list exists? path))
    (list throw-exception! :path-not-exist :msg (list 'str path " doesn't exist"))
    form))

(defmacro path-not-exists-or-throw! [path form]
  "A macro that checks to see if path exists
   If so, an error is thrown
   if not, the form is executed"
  (list 'if (list exists? path)
    (list throw-exception! :path-exists :msg (list 'str path " already exists"))
    form))

(defn create-dir! [path]
  (path-not-exists-or-throw! path
    (-update-filesystem! (-add-to-fs-map (get-filesystem) (vectorize-path path) DIRECTORY-TYPE))))

(defn rm-dir! [path]
  (path-exists-or-throw! path
    (-update-filesystem! (-del-from-fs-map (get-filesystem) (vectorize-path path)))))

(defn create-file! [path contents]
  (path-not-exists-or-throw! path
    (-update-filesystem! (-add-to-fs-map (get-filesystem) (vectorize-path path) FILE-TYPE :contents contents))))

(defn rm-file! [path]
  (path-exists-or-throw! path
    (-update-filesystem! (-del-from-fs-map (get-filesystem) (vectorize-path path)))))

(defn read-file [path]
  (path-exists-or-throw! path
    (get-in (get-filesystem) (vectorize-path path))))
            
(defn read-dir [path]
  (map name (keys (read-file path))))
            
(defn -reset-filesystem! []
  "Resets the filesystem to be empty
   Use with CAUTION.
   Useful for testing purposes"
   (reset! filesystem-history [{}])
   (reset! current-position 0))
