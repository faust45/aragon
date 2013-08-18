(ns data.utils
  (:require [ring.util.response :as resp]
            [clojure.data.json :as json])
  (:use [clojure.string :only (join split)]))

(defn mfun
  [alist blist]
  (for [a alist b blist]
    (comp a b)))

(defn abs 
  [n]
  (cond
    (-> n not number?) (throw (IllegalArgumentException.  "abs requires a number"))
    (neg? n) (- n)
    :else n))

(defmacro s
  [body]
  (try `(~@body)
        (catch Exception e (str "Caught exception: " (clojure.stacktrace/print-stack-trace e)))))

(def a-concat (partial apply concat))

(def drop-nil
  (partial remove nil?))

(defmacro juxt-h
  [& lines]
  `(comp (partial apply merge) (juxt ~@(map #(list comp `(partial assoc {} '~%) %) lines))))

(defmacro juxt-h-with
  [f & lines]
  `(comp (partial apply merge) (juxt ~@(map #(list comp `(partial assoc {} '~%) f %) lines))))

(defn send-file [path] 
  (resp/file-response path {:root "public/"}))

;(defn wrap-reload
;  [handler]
;  (fn [req]
;    (require 'data.core :reload)
;    (require 'data.game :reload)
;    (handler req)))

(defn is-json?
  [req]
  (= (:content-type req) "application/json; charset=UTF-8"))

(defn try-parse-json
  [req]
  (if (is-json? req) 
      (assoc req :data (json/read-str (slurp (:body req))))
      req))

(defn wrap-parse-json
  [handler]
  (fn [req]
    (-> req try-parse-json handler)))

(defn is-chess-move?
  [req]
  (= (:content-type req) "application/chess; charset=UTF-8"))

(defn parse-move
  [req]
    (split (slurp (:body req)) #" "))

(defn try-parse-chess-proto
  [req]
  (if (is-chess-move? req) 
      (assoc req :move (parse-move req))
      req))

(defn wrap-parse-chess-protocol
  [handler]
  (fn [req]
    (-> req
        try-parse-chess-proto
        handler)))


