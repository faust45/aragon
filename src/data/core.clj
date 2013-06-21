(ns data.core
  (:require [ring.util.response :as resp]
            [clojure.java.io :as io]
            [data.game :as game]
            [compojure.handler :as handler]
            [clojure.data.json :as json])
  (:import java.util.UUID)
  (:use ring.adapter.jetty
        [clojure.string :only (join split)]
        [compojure.core]
        data.utils
        ring.middleware.multipart-params
        ring.middleware.params))


(defn think 
  [position]
  (-> position game/analyze json/write-str))

(def my-routes
  (routes (POST "/analyzePosition" {pos :data} (think pos))
          (GET "/public/*" {{resource-path :*} :route-params} (send-file resource-path))))

(def app
  (-> my-routes
      wrap-parse-json
      wrap-multipart-params
      wrap-reload))
    
(defn main [& args]
  (run-jetty #'app {:port 8080}))
