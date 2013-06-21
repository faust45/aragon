(ns data.test.core
  (:use [data.core])
  (:use [clojure.test]))

(deftest add-movee
  (add-move ['w 'K 'e1 'e2])
  (is false "No tests have been written."))
