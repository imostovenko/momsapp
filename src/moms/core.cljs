(ns moms.core
  (:require
    [moms.home.ui :as home]
    [moms.venueslist.ui :as vlist]
    [moms.venue.ui :as v]

    [moms.text :as tt]
    [rum.core :include-macros true :as rum]))

(enable-console-print!)

(def app-div (js/document.getElementById "app"))


(rum/defc footer
  []
  [:div.container
   [:hr]
   [:footer.footer
    [:p "© Mostovenko, 2016"]]])


(defonce current-page (atom "home"))


(rum/defc moms-app < rum/reactive
  []
  (rum/react tt/lang)
  (rum/react current-page)
  [:div.main-view
     (case @current-page
       "home" (home/page)
       "v-list" (vlist/page)
       "venue" (v/page))])

(rum/mount (moms-app) app-div)