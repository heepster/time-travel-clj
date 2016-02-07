(ns time-travel-clj.core-test
  (:require [clojure.test :refer :all]
            [time-travel-clj.core :as ttc]))

(deftest parse-cmd-test
  (testing "parse-cmd"
    (let [cmd "echo 'hey'"
          actual-cmd "echo"
          actual-args "'hey'"
          output (ttc/parse-cmd cmd)]
    (is (:cmd output) actual-cmd)
    (is (:args output) actual-args))))

(deftest get-cmd-test
  (testing "get-cmd"
    (let [cmd-map {:cmd "echo" :args "'hello'"}]
      (is (:cmd cmd-map) (ttc/get-cmd cmd-map)))))

(deftest get-args-test
  (testing "get-args"
    (let [cmd-map {:cmd "echo" :args "'hello'"}]
      (is (:args cmd-map) (ttc/get-args cmd-map)))))

