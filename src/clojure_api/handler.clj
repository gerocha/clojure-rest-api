(ns clojure-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojurewerkz.neocons.rest        :as nr]
            [compojure.handler                :as handler]
            [ring.middleware.json             :as rj]
            [ring.util.response               :as resp]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def conn (nr/connect "http://localhost:7474/db/data/" "neo4j" "test"))

(def task-query "MATCH (task:Task) RETURN task"
  )

(defn get-tasks
  []
  (let [result (cy/tquery conn task-query)]
    (map (fn [x] {:test x}) result))
 )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/tasks" [] (resp/response (get-tasks)))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (handler/site)
      (rj/wrap-json-response)))
