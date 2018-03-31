(ns clojure-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler                :as handler]
            [ring.middleware.json             :as rj]
            [ring.util.response               :as resp]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def conn (nr/connect "http://localhost:7474/db/data/"))

(def task-query "MATCH (task:Task) RETURN task"
  )

(defn get-tasks
  []
  (let [result cy/tquery conn task-query]
    tasks)
 )

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/tasks" [] (resp/response (get-tasks)))
  (route/not-found "Not Found"))


(def app
  (-> app-routes
      (handler/site)
      (rj/wrap-json-response)))
