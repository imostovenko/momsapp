(ns moms.server
  (:use [compojure.core :only [defroutes GET POST]])
  (:require
    [datomic.api :as d]
    [compojure.route    :as route]
    [moms.datomic :as dat]
    [org.httpkit.server :as httpkit]
    [moms.util :as u]
    [ring.util.response :as response])
  (:gen-class))



;;;=========SEACRH    EXAMPLE=========================
(def lines
  ["aaaaa" "bbbbcccc" "ccccc" "ddddd"])

(defn search [s]
  (filter #(.contains % s) lines))
;;TODO here should be function which queries Datomic
;; to get all venues with "s" typed by User.
;;=====================================================


(defroutes app
  (GET "/" []
    (response/resource-response "public/index.html"))


  ;;;==========GET VENUES ===========


  (GET "/api/venue/:id" [id]
    ;(println (u/obj->transit (dat/get-venue id)))
    (u/obj->transit (dat/get-venue (clojure.core/read-string id))))

  (GET "/api/all-venues/" []
    (u/obj->transit (dat/get-venues-deep)))

  (GET "/api/venues/:filter" [filter])

  ;FIXME
  (POST "/api/fit-venues/" [:as request]
    (let [fac (-> request :body slurp u/transit->obj)]
      ;(println fac)))
      (u/obj->transit (dat/venues-by-facilities (vec (:p fac))))))


  ;(POST "api/one-venue/" [:as request]
  ;  (let [request-body (-> request :body slurp u/transit->obj)]
  ;    (pr-str (:id request-body))
  ;    (u/obj->transit (dat/get-venue (:id request-body)))))



  ;;;;==========ADD/UDPATE VENUE ======;;;;
  (POST "/api/add-v/" [:as request]
    (let [venue-map (-> request :body slurp u/transit->obj)]
      (do
        (dat/add-venue venue-map)
        (str venue-map))))



  ;;;=========SEACRH    EXAMPLE=======


  (GET "/api/search/:q" [q]
    (pr-str (search q)))

  (POST "/api/search/" [:as request]
    ;(println (pr-str r))
    (let [request-body (-> request :body slurp u/transit->obj)]
;; - here we are getting what we sent in body of the POST request and trasfomr it into object
      ;(println (pr-str b))
      ;(println b)
      (u/obj->transit (search (:q request-body)))))
;; - here we are searching for params with search-function and then put into transit
;; to be returned as response to the client
    ;(pr-str (search q)))


  ;(POST "/api/db/save" [:as req]
  ;  (let [db (req->body req)]
  ;    (storage/save-db db)
  ;    (println "saved")
  ;    {:status  200}))

  ;(GET "/api/db/get" [:as req]
  ;  {:status  200
  ;   :body (storage/get-db)})

  (route/resources "/" {:root "public"}))


(defn -main [& {:as args}]
  (let [port (or (get args "--port") "8080")]
    (println "Starting server at port" port)
    (httpkit/run-server #'app {:port (Long/parseLong port)})))


