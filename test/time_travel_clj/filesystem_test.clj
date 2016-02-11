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

(deftest add-dir-to-fs-map-test-1
  (testing "add dir to fs map test"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {:home {}}]
      (is (= (ttf/-add-dir-to-fs-map fs-map dir) {:home {:bob {}}})))))

(deftest add-dir-to-fs-map-test-2
  (testing "add-dir-to-fs-map-test to recursively create directories that don't exist"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {}]
      (is (= (ttf/-add-dir-to-fs-map fs-map dir) {:home {:bob {}}})))))

(deftest del-dir-from-fs-map-test-1
  (testing "del-dir-from-fs-map-test"
    (let [dir (ttf/vectorize-path "/home/bob")
          fs-map {:home {:bob {}}}]
      (is (= (ttf/-del-dir-from-fs-map fs-map dir) {:home {}})))))

(deftest dir-exists-test-1
  (testing "dir exists test"
    (let [fs-map (ttf/get-filesystem)
          dir "/home/bob"
          before (ttf/dir-exists? dir)
          _ (ttf/create-dir! dir)
          after (ttf/dir-exists? dir)]
      (is (= before false))
      (is (= after true)))))
          
(deftest create-dir-test-1
  (testing "create-dir! creates a directory on the filesystem"
    (let [fs-map (ttf/get-filesystem)
          dir    "/home/bob"
          _      (is (= (ttf/dir-exists? dir) false))
          _      (ttf/create-dir! dir)]
      (is (= (ttf/dir-exists? dir) true)))))
          
(deftest rm-dir-test-1
  (testing "rm-dir! removes a directory from the filesystem"
    (let [fs-map (ttf/get-filesystem)
          dir "/home/bob"
          _   (ttf/create-dir! dir)
          _   (is (= (ttf/dir-exists? dir) true))
          _   (ttf/rm-dir! dir)]
      (is (= (ttf/dir-exists? dir) false)))))
          



