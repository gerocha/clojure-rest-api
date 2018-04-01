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

(def insert-query
  [desc, status]
  (nr/create conn {:desc desc, :status status})
 )

(defn get-tasks
  []
  (let [result (cy/tquery conn task-query)]
    (map (fn [x] {:test x}) result))
 )

(defn insert-task
  [description,
   status]
  (let [result (cy/tquery conn insert-query {:desc description, :status status})]
   )
 )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/tasks" [] (resp/response (get-tasks)))
  (POST "/tasks" [description status :as request] (resp/response (insert-task (:params request))))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (handler/site)
      (rj/wrap-json-response)))
