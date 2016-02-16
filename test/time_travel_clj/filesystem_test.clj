(ns time-travel-clj.filesystem-test
  (:require [clojure.test :refer :all]
            [time-travel-clj.filesystem :as ttf]))

(defn before-each-fixture [f]
  (ttf/-reset-filesystem!)
  (f))

(use-fixtures :each before-each-fixture)

(deftest vectorize-path-test
  (testing "vectorize-path"
    (let [filepath1 "/home/bob"
          filepath2 "/home"]
      (is (= (ttf/vectorize-path filepath1) [:home :bob]))
      (is (= (ttf/vectorize-path filepath2) [:home])))))

(deftest add-to-fs-map-test-1
  (testing "add dir to fs map test"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {:home {}}]
      (is (= (ttf/-add-to-fs-map fs-map dir ttf/DIRECTORY-TYPE) {:home {:bob {}}})))))

(deftest add-to-fs-map-test-2
  (testing "add-to-fs-map-test to recursively create directories that don't exist"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {}]
      (is (= (ttf/-add-to-fs-map fs-map dir ttf/DIRECTORY-TYPE) {:home {:bob {}}})))))

(deftest add-to-fs-map-test-3
  (testing "Adds a file to fs map"
    (let [file-vec (ttf/vectorize-path "/home/bob/his_file.txt")
          contents "test"
          fs-map {}]
      (is (= (ttf/-add-to-fs-map fs-map file-vec ttf/FILE-TYPE :contents contents)
             {:home {:bob { :his_file.txt contents}}})))))

(deftest del-dir-from-fs-map-test-1
  (testing "del-dir-from-fs-map-test"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {:home {:bob {}}}]
      (is (= (ttf/-del-from-fs-map fs-map dir) {:home {}})))))

(deftest dir-exists-test-1
  (testing "dir exists test"
    (let [fs-map (ttf/get-filesystem)
          dir "/home/bob"
          before (ttf/exists? dir)
          _ (ttf/create-dir! dir)
          after (ttf/exists? dir)]
      (is (= before false))
      (is (= after true)))))
          
(deftest create-dir-test-1
  (testing "create-dir! creates a directory on the filesystem"
    (let [fs-map (ttf/get-filesystem)
          dir    "/home/bob"
          _      (is (= (ttf/exists? dir) false))
          _      (ttf/create-dir! dir)]
      (is (= (ttf/exists? dir) true)))))
          
(deftest rm-dir-test-1
  (testing "rm-dir! removes a directory from the filesystem"
    (let [fs-map (ttf/get-filesystem)
          dir "/home/bob"
          _   (ttf/create-dir! dir)
          _   (is (= (ttf/exists? dir) true))
          _   (ttf/rm-dir! dir)]
      (is (= (ttf/exists? dir) false)))))

(deftest create-file-test-1
  (testing "create-file! creates a file on the filesystem"
    (let [file     "/home/bob/file.txt"
          contents "test test"
          _        (is (= (ttf/exists? file) false))
          _        (ttf/create-file! file contents)
          fs-map-after (ttf/get-filesystem)]
      (is (= (ttf/exists? file) true))
      (is (= fs-map-after {:home {:bob {:file.txt contents}}})))))
 
(deftest rm-file-test-1
  (testing "rm-file! removes a file from the filesystem"
    (let [file    "/home/bob/file.txt"
          contents "test test"
          _      (is (= (ttf/exists? file) false))
          _      (ttf/create-file! file contents)
          _      (is (= (ttf/exists? file) true))
          _      (ttf/rm-file! file)]
      (is (= (ttf/exists? file) false)))))
 
