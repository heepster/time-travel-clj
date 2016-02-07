(ns time-travel-clj.filesystem-test
  (:require [clojure.test :refer :all]
            [time-travel-clj.filesystem :as ttf]))

(deftest vectorize-path-test
  (testing "vectorize-path"
    (let [filepath "/home/bob"]
      (is (= (ttf/vectorize-path filepath) [:home :bob])))))

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
          resp (ttf/dir-exists? dir)]
      (is (= resp false)))))
          
(deftest create-dir-test-1
  (testing "create dir test 1"
    (let [fs-map (ttf/get-filesystem)
          dir    "/home/bob"
          _      (is (= (ttf/dir-exists? dir) false))
          _      (ttf/create-dir! dir)]
      (is (= (ttf/dir-exists? dir) true)))))
          


