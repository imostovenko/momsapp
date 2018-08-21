(ns moms.venueslist.ui
  ;(:require-macros
  ;  [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    ;[moms.facilities :as fac]
    [moms.components :as comp]
    [moms.map.ui :as map]
    [moms.db :as db]
    ;[moms.util :as u]
    [moms.text :as tt]
    [rum.core :include-macros true :as rum]))


(def show-sorting? (atom false))
(def sorted-results (atom '()))
(def sortby (atom "fit"))
(def order (atom ">"))


(rum/defc header < rum/reactive
  []
  (rum/react db/venues)
  (let [on-back #(do
                  (reset! map/show-map? false)
                  (reset! moms.core/current-page "home")
                  (println @moms.core/current-page))]
    [:div
     [:nav.navbar.navbar-fixed-top.moms-header
      [:div.container
       [:div.row
        [:div.col-xs-2.no-left-padding
         {:on-click on-back
          :key "back"}
         [:span.glyphicon.glyphicon-menu-left.nav-icons]]

        [:div.col-xs-7.page-title
         {:key "title"}
         [:strong (count @db/venues) " places found"]]

        [:div.col-xs-3.no-right-padding
         {:key "icons"}
         [:span.glyphicon.glyphicon-sort.nav-icons
            {:on-click #(swap! show-sorting? not)
             :key "sort"
             :class (when (rum/react show-sorting?) "pressed")}]

         [:span.glyphicon.glyphicon-map-marker.nav-icons
            {:on-click #(swap! map/show-map? not)
             :key "map"}]]]]]]))





(rum/defcs sorting-section < rum/reactive

  ;(rum/local "" ::sort-query)
  [state]
  (rum/react show-sorting?)
  (rum/react order)
  (rum/react sortby)
  ;(js/console.log (pr-str (rum/react sorted-results)))

  (let [sorting (case @sortby
                  "fit" "Most fit"
                  "rate" "Coolest"
                  "price" "Cheapest"
                  "dist" "Nearest")
        top-bottom (case @order
                     ">" "top"
                     "<" "bottom")
        ;s-query (::search-query state)
        ;on-search-change (fn [e]
        ;                   (reset! s-query (-> e .-target .-value))
        ;set-resutls #(reset! sorted-results (u/transit->obj %))
        ;on-submit #()
        on-enter (fn [e]
                   (println "key press" (.-charCode e))
                   (if (= 13 (.-charCode e))
                     ;(on-submit)
                     (println "NOT ENTER")))]
    (when @show-sorting?
      [:div.moms-sort
        [:div.btn-toolbar {:role "toolbar"
                           :aria-label "sorting"}
         [:div.btn-group.btn-group-sm.sort {:role "group"}
          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! sortby "fit")
              :class (when (= @sortby "fit") "active-btn")}
             "Fit"]
          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! sortby "rate")
              :class (when (= @sortby "rate") "active-btn")}
             "Rating"]
          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! sortby "price")
              :class (when (= @sortby "price") "active-btn")}
             "Price"]
          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! sortby "dist")
              :class (when (= @sortby "dist") "active-btn")}
             "Distance"]]

         [:div.btn-group.btn-group-sm.sort {:role "group"}
          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! order ">")
              :class (when (= @order ">") "active-btn")}
             [:span.glyphicon.glyphicon-arrow-up]]

          [:button.btn.btn-default
             {:type "button"
              :on-click #(reset! order "<")
              :class (when (= @order "<") "active-btn")}
             [:span.glyphicon.glyphicon-arrow-down]]]]


       [:div.ven-attr (str sorting " are at the " top-bottom)]])))



(rum/defc one-card < rum/reactive
  [venue-map]
  (let [{id :db/id
         title :venue/title
         type :venue/category
         hours :venue/hours
         price :venue/price
        ; dist :distance
         descr :venue/fid  ;descr :venue/description
         rate :venue/momsrate
         facs :venue/facilities
         fid  :venue/fid} venue-map
        fit (str (db/venue-fit venue-map) "%")
        price (case (str price)
                "1" "$"
                "2" "$$"
                "3" "$$$"
                "4" "$$$$"
                "?$")
        rate (cljs.reader/read-string rate)
        facs-absent (db/venue-vs-filter-adsent venue-map)]
    (println "Venue map" venue-map)
    (println "Absent facs " facs-absent)
    [:div.card {:key (str "card-" id)
                :on-click #(do
                              (db/get-v id)
                              (reset! moms.core/current-page "venue")
                              (println @moms.core/current-page))}

     [:div.container.card-top {:key (str "card-top-" id)}
      [:div.row {:key (str "row1-" id)}
       [:div.col-md-8.col-sm-8.col-xs-8.ven-title {:key (str "col11-" id)}
         [:p title]]
       [:div.col-md-4.col-sm-4.col-xs-4.ven-rating {:key (str "col12-" id)}
        (for [i (range 0 rate)]
          [:span.glyphicon.glyphicon-heart-empty.rate-icon])]]


      [:div.row {:key (str "row2-" id)}
       [:div.col-md-9.col-sm-9.col-xs-9.ven-type {:key (str "col21-" id)}
        [:span.ven-price price] " " type
        [:br] hours]
       [:div.col-md-3.col-sm-3.col-xs-3.ven-distance {:key (str "col22-" id)}
        "100m" [:span.glyphicon.glyphicon-map-marker]]]

      [:div.row {:key (str "row3-" id)}
       [:div.col-md-12.col-sm-12.col-xs-12.ven-text
         descr " " id]]]


     (when (< 0 (count @db/FILTER))
        [:div.card-bottom {:key (str "card-bottom-" id)}
         (when (< 0 (count facs-absent))
          [:div.col-md-12.col-sm-12.col-xs-12.absent-subsection
           "This place doesn't have the following from your request"
           [:br (:key "br")]
           [:div.icons-absent
            (for [i facs-absent]
              (comp/one-fac i #(println "Venue map" i) "fac-icon-absent"))]])
         [:div.progress
          [:div.progress-bar.progress-bar-warning.progress-bar-striped
                                    {:role "progressbar"
                                      :aria-valuenow "60"
                                      :aria-valuemin "0"
                                      :aria-valuemax "100"
                                      :style {:width fit}}]]])]))



(rum/defc all-cards < rum/reactive
  []
  (rum/react db/venues)
  [:div
   (for [i @db/venues]
       (one-card i))])



(rum/defc page < rum/reactive
  []
  (rum/react db/venues)
  [:div.moms
   (header)
   (if (rum/react map/show-map?)
     (map/g-map)
     [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
      [:div.row.moms-content
       (sorting-section)
       (if (= (count @db/venues) 0)
         [:div.error-message
          [:span.glyphicon.glyphicon-flash]
          [:br]
          (tt/t :t-nothing-found)]
         (all-cards))]])])




