(ns moms.components
  (:require
    [rum.core :include-macros true :as rum]
    [clojure.string :as str]
    [ajax.core :refer [GET POST]]
    [moms.facilities :as fac]
    [moms.util :as ut]
    [moms.text :as tt]))

(enable-console-print!)

; TODO add this to moms-popup
;$(".div-inner").click(function(event){event.stopPropagation()});


(rum/defc text-popup < rum/reactive
  [react-on header-icon header-text content-text button-text]
  (rum/react react-on)
  (let [on-close-fn #(reset! react-on false)]
    (when @react-on
      [:div.overlay {:on-click on-close-fn}
       [:div.col-md-6.col-md-offset-3.col-sm-6.col-sm-offset-3.col-xs-12
        [:div.moms-popup
         [:div.moms-popup-header

          [:div.glyphicon {:class header-icon}]
          [:div (tt/t header-text)]]

         [:div.moms-popup-content.text-popup
          [:div (tt/t content-text)]]

         [:div.moms-btn#btn-popup
          {:on-click on-close-fn}
          (tt/t button-text)]]]])))



(defn one-fac
  ([fac-key]
   (one-fac fac-key #(println fac-key)))
  ([fac-key on-click-fn]
   (let [icon (fn [fac-key] (str "icon-" (-> fac/FAC fac-key :icon)))]
     [:span.fac-icon {:class (icon fac-key)
                      :on-click on-click-fn
                      :key (str "one-fac-" fac-key)}]))


  ([fac-key on-click-fn class]
   (let [icon (fn [fac-key] (str "icon-" (-> fac/FAC fac-key :icon)))]
    [:span.fac-icon {:class (str (icon fac-key) " " class)
                     :on-click on-click-fn
                     :key (str "one-fac-" fac-key)}])))



;
;(declare alert-error)
;(rum/defc alert-error
;  [alert-message on-alert-dismiss]
;  [:div.form-group
;   [:div.alert.alert-danger.alert-error
;    [:span.glyphicon.glyphicon-exclamation-sign.red]
;    alert-message
;    [:a.close#X {:href "#"
;                 :on-click on-alert-dismiss} "X"]]])
;
;
;(declare warning)
;(rum/defc warning
;  [warning-message]
;  [:div.alert.alert-warning warning-message])
;
;
;(declare popup-header)
;(rum/defc popup-header
;  [title on-close-fn]
;  [:div.popup-header
;   [:h3 title]
;   [:a.close {:on-click on-close-fn} "x"]])
;
;
;(declare popup-footer)
;(rum/defc popup-footer
;  [on-submit-fn on-dismiss-fn on-delete-fn]
;  [:div.popup-footer
;   [:div
;    [:button.btn.btn-danger.pull-left
;     {:type     "button"
;      :on-click #((on-delete-fn) (on-dismiss-fn))}
;     "Delete"]
;    [:button.btn.btn-default
;     {:type     "button"
;      :on-click on-dismiss-fn}
;     "Cancel"]
;    [:button.btn.btn-success
;     {:type     "submit"
;      :on-click #((on-submit-fn) (on-dismiss-fn))}
;     "Save"]]])
;
;
;(declare my-btn-group)
;(rum/defc my-btn-group
;  [btn-label btn-key values-set on-btn-click v]
;  [:div.form-group.required
;   [:label.control-label.col-sm-4
;    {:for btn-label}
;    btn-label]
;   [:div.btn-group.col-sm-6
;    {:id btn-label}
;    (for [i (keys values-set)]
;      [:button.btn.btn-default
;       {:class (if (= i (btn-key @v)) "active" "")
;        :type     "button"
;        :value    (values-set (btn-key @v))
;        :on-click #(on-btn-click i)}
;       (i values-set)])]])
;
;
;(declare filter-group)
;(rum/defc filter-group
;  [filter-settings group-title key-group key]
;  (let [f-settings filter-settings
;        title group-title
;        kk key-group
;        k key]
;    (if (= k :users)
;      [:div.col-md-3
;       [:h5 title]
;       [:ul.list-group
;        (for [i (keys kk)]
;          (let [contains (contains? (k @f-settings) i)
;                f (if contains disj conj)
;                toggle #(swap! f-settings update k f i)]
;            [:li.list-group-item
;             [:label.checkbox-inline
;              [:input {:type     "checkbox"
;                       :value    ""
;                       :on-click toggle
;                       :checked  contains}]
;              ((kk i) :login)]]))]]
;      [:div.col-md-3
;       [:h5 title]
;       [:ul.list-group
;        (for [i (keys kk)]
;          (let [contains (contains? (k @f-settings) i)
;                f (if contains disj conj)
;                toggle #(swap! f-settings update k f i)]
;            [:li.list-group-item
;             [:label.checkbox-inline
;              [:input {:type     "checkbox"
;                       :value    ""
;                       :on-click toggle
;                       :checked  contains}]
;              (i kk)]]))]])))
;
;
;(declare col-sortable)
;(rum/defc col-sortable
;  [column-title column-key s-fn K C]
;  (let [column-title column-title column-key column-key s-fn s-fn K K C C]
;    [:th
;     [:span.column-title column-title]
;     [:span.glyphicon.glyphicon-chevron-up.arrow
;      {:on-click #(s-fn column-key >)
;       :class (when (and (= K column-key) (= C >)) "pressed")}]
;     [:span.glyphicon.glyphicon-chevron-down.arrow
;      {:on-click #(s-fn column-key <)
;       :class (when (and (= K column-key) (= C <)) "pressed")}]]))
;

