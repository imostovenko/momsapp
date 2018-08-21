(ns moms.venue.ui
  (:require-macros
    [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    [moms.facilities :as fac]
    [moms.map.ui :as map]
    [moms.util :as u]
    [moms.text :as tt]
    [moms.components :as comp]
    [moms.db :as db]
    [rum.core :include-macros true :as rum]))


(def show-rate? (atom false))  ;popup with rating
(def show-tell-owner? (atom false)) ;popup with missing facilities
(def show-fac-all? (atom false)) ;all facilities and their descr


;;;=======POPUPS ===========

(rum/defc rate-popup
  "cool popup to rate the venue"
  []
  (let [on-close-fn #(reset! show-rate? false)
        update-v! #(db/post-venue! (merge @db/current-venue {:venue/momsrate %}))]

    [:div.overlay {:key "rate-popup"}
     [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
      [:div.moms-popup
       [:div.moms-popup-header {:on-click on-close-fn}
        [:div.glyphicon.glyphicon-heart-empty]
        [:div(tt/t :t-do-you-like)]]

       [:div.moms-popup-content
        [:div.btn-group {:role "group"}
         [:button.btn.btn-default {:type "button"
                                   :on-click #(do (update-v! "1")
                                                  (on-close-fn))}
                                  "good"]

         [:button.btn.btn-default {:type "button"
                                   :on-click #(do (update-v! "2")
                                                  (on-close-fn))}
                                  "nice"]
         [:button.btn.btn-default {:type "button"
                                   :on-click #(do (update-v! "3")
                                                  (on-close-fn))}
                                  "cool"]]]]]]))



(rum/defc tellowner-popup
  []
  (let [on-close-fn #(reset! show-tell-owner? false)
        facs (db/venue-doesnt-have @db/current-venue)]
    [:div.overlay {:key "tellowner-popup"}
     [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
      [:div.moms-popup
       [:div.moms-popup-header {:on-click on-close-fn}
        [:div.glyphicon.glyphicon-pencil]
        [:div(tt/t :t-tell-owner-which)]]

       [:div.moms-popup-content#facilities
        (for [i facs]
          [:div.col-md-2.col-sm-3.col-xs-3.moms-popup-one-fas
           (comp/one-fac i #(println i) "fac-icon-in-popup")
           [:p.fac-title (tt/ft i :title)]])]

       [:div.moms-btn
        {:type     "submit"
         :on-click on-close-fn}
        (tt/t :t-tell-owner)]]]]))


;;;=======SECTIONS===========


(rum/defc header
  [title]
  (let [on-back #(do
                  (reset! map/show-map? false)
                  (reset! show-fac-all? false)
                  (reset! moms.core/current-page "v-list")
                  (println @moms.core/current-page))]
    [:div
     [:nav.navbar.navbar-fixed-top.moms-header
      [:div.container
       [:div.row
        [:div.col-xs-2.no-left-padding {:on-click on-back} [:span.glyphicon.glyphicon-menu-left.nav-icons]]

        [:div.col-xs-8.page-title [:strong title]]

        [:div.col-xs-2.no-right-padding
         ; [:span.glyphicon.glyphicon-sort-by-attributes.nav-icons {:on-click #(swap! show-filter? not)}]
           [:span.glyphicon.glyphicon-heart.nav-icons
            {:on-click #(reset! show-rate? true)}]]]]]]))



(rum/defc v-info-section
  "main facility info for venue ID"
  [title price category address dist phone hours descr rate]
  (let [price (case price
                "1" "$"
                "2" "$$"
                "3" "$$$"
                "4" "$$$$"
                "$")]
        ;rate (cljs.reader/read-string rate)]

    [:div.v-info-section
     [:div.container-fluid
      [:div.row
       [:div.col-md-10.col-sm-10.col-xs-9.ven-title [:p title]]
       [:div.col-md-2.col-sm-2.col-xs-3.ven-rating
        (for [i (range 0 rate)]
          [:span.glyphicon.glyphicon-heart-empty.rate-icon])]]

      [:div.row
       [:div.col-md-8.col-sm-8.col-xs-8.ven-attr [:span.ven-price price] " " category]]

      [:div.row
       [:div.col-md-8.col-sm-8.col-xs-8.ven-attr address]
       [:div.col-md-4.col-sm-4.col-xs-4.ven-attr.right dist [:span.glyphicon.glyphicon-map-marker]]]

      [:div.row
       [:div.col-md-8.col-sm-8.col-xs-8.ven-attr hours]
       [:div.col-md-4.col-sm-4.col-xs-4.ven-attr.right [:a {:href (str "tel:" phone)} phone]]]

      [:div.row
       [:div.col-md-12.col-sm-12.col-xs-12.ven-text descr]]]]))



(rum/defc v-facilities-section < rum/reactive
  "what this place have and what doesn't"
  [facs]
  (rum/react show-fac-all?)
  (let [one-fac-details (fn [fac-key]
                          [:div.media
                           [:div.media-left.media-top
                            (comp/one-fac fac-key #(println fac-key) "fac-icon-in-venue")]
                           [:div.media-body
                            [:p.ven-fac-title (tt/ft fac-key :title)]
                            [:p.ven-fac-description  (tt/ft fac-key :description)]]])
        fit (db/venue-fit @db/current-venue)]

    [:div.v-facilities-section {:on-click #(swap! show-fac-all? not)}

     [:div.container-fluid
      [:div.row
       [:div.col-xs-12
        [:div.sub-title  (tt/t :t-kids-facilities) " - fit is " fit "%"
         [:a.links.pull-right
          (if @show-fac-all?
              [:span.glyphicon.glyphicon-menu-up]
              [:span.glyphicon.glyphicon-menu-down])]]]

       [:div.col-xs-12
        [:p.ven-fac-description "Tap to see more details about each facility"]]]

      [:div.row
       [:div.col-xs-12.fac-list-in-venue
        (for [i facs]
             (if @show-fac-all?
                 (one-fac-details i)
                 (comp/one-fac i #(println i) "fac-icon-in-venue")))


        (when @show-fac-all?
          [:div.row
           [:div.col-md-12.col-sm-12.col-xs-12.pull-left
            [:p.ven-fac-description
             {:on-click #(do
                            (reset! show-tell-owner? true)
                            (println @show-tell-owner?))}
             [:span.glyphicon.glyphicon-pencil.nav-icons]
             (tt/t :t-tell-owner-what-missed)]]])]]]]))




(rum/defc v-gallery-section
  "scrollable static map and few photos"
  [lat lng]
  (let [one-img [:img.img-responsive {:src "https://irs1.4sqi.net/img/general/width960/41461156_ISrK-8_xmYlfzANm_XtkEAbwwnWQEYG8Vhp_MMq4b8k.jpg"}]
        map-png [:a {:href (db/dynamicmap-url lat lng)
                     :target "_blank"}
                 [:img.img-responsive {:src (db/staticmap-url lat lng)
                                       :alt "Venue Google Map"}]]]

    [:div.container-fluid.ven-gallery {:key "gallery"}
       [:div.ven-one-photo {:key "map"} map-png]
       [:div.ven-one-photo {:key "1"} one-img]
       [:div.ven-one-photo {:key "2"} one-img]]))





;
;(rum/defc v-comments-section
;  "TBD in future"
;  []
;  [:div])
;


(rum/defc v-foursquare-section
  "view on Foresquare link with icon"
  [link]
  [:div.v-foursquare-section {:key "foursquare"}
   [:span.socicon-foursquare.social-icons]
   [:a.links.links-primary {:href link
                            :target "_blank"}
     "view details on Foursquare"]])


;;;=======PAGE ===========

(rum/defc page < rum/reactive
  []
  (rum/react db/current-venue)
  (let [{title :venue/title
         category :venue/category
         address :venue/address
         phone :venue/phone
         price :venue/price
         rate :venue/momsrate
         hours :venue/hours
         descr :venue/description
         link :venue/furl
         facs :venue/facilities
         lat :venue/lat
         lng :venue/lng} @db/current-venue
         dist "100m"]
    [:div.moms
     (header title)
     [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
      [:div.row.moms-content
       (v-info-section title price category address dist phone hours descr rate)
       (v-facilities-section facs)
       (v-gallery-section lat lng)
       (v-foursquare-section link)]]

     (when (rum/react show-rate?)
       (rate-popup))
     (when (rum/react show-tell-owner?)
       (tellowner-popup))]))
