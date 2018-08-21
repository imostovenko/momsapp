(ns moms.map.ui
  (:require-macros
    [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    [moms.facilities :as fac]
    [moms.util :as u]
    [moms.text :as tt]
    [rum.core :include-macros true :as rum]))


(def show-map? (atom false))
(defonce g-key (str "key=" "AIzaSyAFfMaK609tANRWpajdLjq0bBmyDCCk7ws"))
(def location (atom "Kiev, Регенераторна 4"))


(defn- map-url
  ;"It should take a list of locations of search results (or a single venue) and construct a URL.
  ;And if it is empty, then show ALL locations.
  ;? URL constructor to be placed inside g-map OR not"
  []
  (let [base "https://www.google.com/maps/embed/v1/search?"
        search @location
        query #(if (empty? search) "q=kiev" (str "q=" search))]

    (str base "&" (query) "&" g-key)))


(rum/defc g-map
  []
  [:div.row
    [:div.col-md-12.col-xs-12.col-sm-12.map
     [:iframe {:width "100%"
               :height "100%"
               :frameBorder "0"
               :src (map-url)}]]])
