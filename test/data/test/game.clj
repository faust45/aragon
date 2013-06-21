(ns data.test.game
  (:use [data.game] :reload)
  (:use clojure.test)
  (:require [clojure.data.json :as json]))


(defn not-nil?
  [value]
  (not (nil? value)))

(def positions 
  (-> "test/fixtures/pos1.json" slurp json/read-str))

;(deftest test-add-move
;  (add-move ['w 'K 'e1 'e2])
;  (is (= (@position 'e2) '(w K)) "assign position correctly"))

;(deftest should-replace-with-nil
;  (add-move ['w 'K 'e1 'e2])
;  (is (not-nil? (@position 'e2)) "assign position correctly")
;  (add-move ['w 'K 'e2 'f2])
;  (is (= (@position 'e2) nil) "assign position to nil, when move out"))

(deftest should-identify-queen-make-ckeck
  (is (make-check? '(w q) (positions "simple-check"))))

(deftest should-detect-mate
  (is (mate? (positions "mate"))))

(deftest should-not-detect-mate
  (is (not (mate? (positions "not-mate")))))
