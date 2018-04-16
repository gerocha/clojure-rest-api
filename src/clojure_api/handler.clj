(ns clojure-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [clojurewerkz.neocons.rest.labels :as nl]
            [compojure.handler                :as handler]
            [ring.middleware.json             :as rj]
            [ring.util.response               :as resp]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def conn (nr/connect "http://localhost:7474/db/data/" "neo4j" "teste"))

(def task-query "MATCH (task:Task) RETURN task"
  )

(defn insert-query
  [ description status ]
  (let [node (nn/create conn {:description description :status status})]
    (nl/add conn node "Task")
   node)
 )

(defn get-tasks
  []
  (let [result (cy/tquery conn task-query)]
    (map (fn [x] {:test x}) result))
 )

(defn insert-task
  [ description status]
  (let [result (insert-query description status)]
   result)
 )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/tasks" [] (resp/response (get-tasks)))
  (POST "/tasks" [description status] (resp/response (insert-task description status)))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (handler/site)
      (rj/wrap-json-response)))
