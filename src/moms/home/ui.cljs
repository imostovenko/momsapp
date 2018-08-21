(ns moms.home.ui
  (:require-macros
    [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    [moms.facilities :as fac]
    [moms.components :as comp]
    [moms.util :as u]
    [moms.db :as db]
    [moms.text :as tt]
    [moms.map.ui :as map]
    [moms.home.menu :as menu]
    [rum.core :include-macros true :as rum]))


(def fac-all (atom false))
(def search-results (atom '()))
(def show-search? (atom false))



(rum/defc header < rum/reactive
  []
  [:div
   [:nav.navbar.navbar-fixed-top.moms-header
    [:div.container
     [:div.row
      [:div.col-xs-3.no-left-padding
       [:span.glyphicon.glyphicon-menu-hamburger.nav-icons
        {:on-click #(do
                     (reset! menu/show-menu? true)
                     (println "Menu " @menu/show-menu?))}]]

      [:div.col-xs-6.page-title
       {:on-click #(do
                    (reset! map/show-map? false)
                    (reset! show-search? false))}
       [:strong "HAPPY MOMS"]]

      [:div.col-xs-3.no-right-padding
       [:span.glyphicon.glyphicon-search.nav-icons
        {:on-click #(do
                     (swap! show-search? not)
                     (reset! map/show-map? false))
         :class (when (rum/react show-search?) "pressed")}]
       [:span.glyphicon.glyphicon-map-marker.nav-icons
        {:on-click #(do
                     (swap! map/show-map? not)
                     (reset! show-search? false))
         :class (when (rum/react map/show-map?) "pressed")}]]]]]])


;;-- here we are sending GET request with search params to the server,
;; and then transform the response from server with on-success hnalder-function
(defn search! [s on-success]
  (GET (str "/api/search/" s)
    {:handler on-success
     :error-handler #(js/console.log %)}))

(defn post-search! [s on-success]
  (POST "/api/search/"
    {:params {:q s}
     :format :transit
     :handler on-success
     :error-handler #(js/console.log %)}))



(rum/defcs search-section < rum/reactive
  (rum/local "" ::search-query)
  [state]
  (rum/react show-search?)
 ; (js/console.log (pr-str (rum/react search-results)))

  (let [s-query (::search-query state)
        on-search-change (fn [e]
                           (reset! s-query (-> e .-target .-value)))
        set-resutls #(reset! search-results (u/transit->obj %))
        on-submit #(post-search! @s-query set-resutls)
        on-enter (fn [e]
                   (println "key press" (.-charCode e))
                   (if (= 13 (.-charCode e))
                    (on-submit)
                    (println "NOT ENTER")))
        on-focus #(reset! show-search? true)
        on-focus-out #(reset! show-search? false)]
    (when @show-search?
      [:div.input-group.moms-search
       [:input.form-control {:type "text"
                             :placeholder (tt/t :t-search-hint)
                             :value       @s-query
                             :on-change   on-search-change
                             :on-key-press on-enter
                             :auto-focus "true"}]
                             ;:on-focus on-focus
                             ;:on-blur on-focus-out}]
       [:span.input-group-btn
        [:button.btn.btn-default {:type "button"
                                  :on-click on-submit}
         [:span.glyphicon.glyphicon-search]]]])))



(rum/defc facilities-all-popup < rum/reactive
  []
  (rum/react fac-all)
  (rum/react db/FILTER)
  (let [on-close-fn #(reset! fac-all false)
        facs (keys fac/FAC)]

    (when @fac-all
      [:div.overlay
        [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
         [:div.moms-popup
            ;[:span.glyphicon.glyphicon-remove-circle {:on-click on-close-fn}]

            [:div.moms-popup-header
             [:div.glyphicon.glyphicon-star-empty]
             [:div (tt/t :t-select-facilities)]]

            [:div.moms-popup-content#facilities
             (for [i facs]
               (let [contains (contains? @db/FILTER i) ;check if fac is already pressed
                     f (if contains disj conj)
                     toggle #(do (swap! db/FILTER f i)
                                 (println "Filter " @db/FILTER))]
                 (if contains
                   [:div.col-md-2.col-sm-3.col-xs-3.moms-popup-one-fas
                    (comp/one-fac i toggle "fac-icon-in-popup fac-icon-pressed")
                    [:p.fac-title (tt/ft i :title)]]
                   [:div.col-md-2.col-sm-3.col-xs-3.moms-popup-one-fas
                    (comp/one-fac i toggle "fac-icon-in-popup")
                    [:p.fac-title (tt/ft i :title)]])))]

            [:div.moms-btn
               {:type     "submit"
                :on-click on-close-fn}
               (tt/t :t-select)]]]])))



(rum/defc facilities-filter < rum/reactive
  []
  (rum/react db/FILTER)
  (let [;facs (keys fac/FAC)
        facs (take 22 (keys fac/FAC))
        n-selected #(when (> (count @db/FILTER) 0)
                     [:span.n-selected {:on-click db/reset-FILTER}
                      (str (count @db/FILTER) " selected ")
                      [:span.glyphicon.glyphicon-remove]])]

    [:div.container-fluid.moms-filter
     [:div.row
      [:div.col-xs-12
       [:div.sub-title  (str (tt/t :t-kids-facilities)) (n-selected)
        [:a.links.pull-right
         {:on-click #(do (reset! fac-all true)
                         (println "Fac popup" @fac-all))}
         (tt/t :t-view-all-fac)]
        (facilities-all-popup)]

       (for [i facs]
          (let [contains (contains? @db/FILTER i)
                f (if contains disj conj)
                toggle #(do (swap! db/FILTER f i)
                            (println "Filter" @db/FILTER))]
            (if contains
              (comp/one-fac i toggle "fac-icon-pressed")
              (comp/one-fac i toggle))))]]]))



(rum/defc rating-price
  []
  [:div.container-fluid.moms-rating
   [:div.row
     [:div.col-xs-7
        [:div.sub-title (tt/t :t-moms-rating)]
        [:div
          [:img.rating {:style {:background-image "url(../images/icons/1.png)"}}]
          [:img.rating {:style {:background-image "url(../images/icons/2.png)"}}]
          [:img.rating {:style {:background-image "url(../images/icons/3.png)"}}]]]
     [:div.col-xs-5
        [:div.sub-title (tt/t :t-price)]
        [:div
         [:img.rating {:style {:background-image "url(../images/icons/US-Dollar.png)"}}]
         [:img.rating {:style {:background-image "url(../images/icons/US-Dollar.png)"}}]]]]])



(rum/defc filter-section < rum/reactive
  []
  (when-not (rum/react show-search?)
    [:div.filter-section
     (facilities-filter)]))
     ;(rating-price)])


(rum/defc action-section < rum/reactive
  []
  [:div.action-section
   (when-not (rum/react show-search?)
     [:div.moms-btn
      {:on-click #(do
                   (if (empty? @db/FILTER)
                     (db/get-all-v)
                     (db/get-fit-v @db/FILTER))
                   (reset! moms.core/current-page "v-list")
                   (println "load" @moms.core/current-page))}
      (tt/t :t-makemomhappy)])
   [:div
    [:a.links.links-primary
     {:on-click #(do
                  (db/get-all-v)
                  (reset! moms.core/current-page "v-list")
                  (println "load" @moms.core/current-page))}
     (tt/t :t-view-all-places)]]])



(rum/defc page < rum/reactive
  []
  [:div.moms
   (header)
   (menu/menu-popups)
   (if (rum/react map/show-map?)
     (map/g-map)
     [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
      [:div.row.moms-content
       (search-section)
       (filter-section)
       (action-section)]])])




