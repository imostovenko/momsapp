(ns moms.home.menu
  (:require-macros
    [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    [moms.components :as comp]
    [moms.text :as tt]
    [rum.core :include-macros true :as rum]))


(def show-menu? (atom false))
(def show-about? (atom false))
(def show-partnership? (atom false))
(def show-privacy? (atom false))
(def contact (atom "fb"))


(rum/defc side-menu < rum/reactive
  []
  (rum/react show-menu?)
  (rum/react contact)
  (let [close-menu  #(reset! show-menu? false)]
    (when @show-menu?
      [:div.container
       [:div.overlay {:on-click close-menu}]
       [:div.menu
        [:ul.nav.nav-stacked

         [:li.menu-item.first
          [:div
           (if (= @tt/lang :UA)
              [:span.badge.la
               {:on-click #(reset! tt/lang :EN)}
               (str "in English")]
              [:span.badge.la
               {:on-click #(reset! tt/lang :UA)}
               (str "Українською")])]

          [:div.col-md-1.col-sm-1.col-xs-2.pull-right
           [:span.glyphicon.glyphicon-remove.nav-icons
            {:on-click close-menu}]]]

         ; [:li.menu-item (tt/t :t-shop)]
         [:li.menu-item (tt/t :t-recommend-new-place)]

         [:li.menu-item {:on-click #(do
                                     (close-menu)
                                     (reset! show-about? true))}
          (tt/t :t-about-moms)]

         [:li.menu-item {:on-click #(do
                                     (close-menu)
                                     (reset! show-partnership? true))}
          (tt/t :t-partnership)]

         [:li.menu-item {:on-click #(do
                                     (close-menu)
                                     (reset! show-privacy? true))}
          (tt/t :t-privacy)]

         [:li.menu-item.contact
          [:img.social-icons {:src "../images/Facebook.png"
                              :on-click #(reset! contact "fb")}]
          [:img.social-icons {:src "../images/Twitter.png"
                              :on-click #(reset! contact "tw")}]
          [:img.social-icons {:src "../images/Google.png"
                              :on-click #(reset! contact "google")}]
          [:img.social-icons {:src "../images/SMS.png"
                              :on-click #(reset! contact "sms")}]
          [:img.social-icons {:src "../images/Email.png"
                              :on-click #(reset! contact "email")}]
          (case @contact
            "fb" [:a.links.contact
                  {:href "https://www.facebook.com/makemomshappy"
                   :target "_blank"}
                  "www.facebook.com/makemomshappy"]

            "tw" [:a.links.contact
                  {:href "https://twitter.com/happy.moms"
                   :target "_blank"}
                  "https://twitter.com/happy.moms"]

            "google" [:a.links.contact
                      {:href "https://twitter.com/happy.moms"
                       :target "_blank"}
                      "Happy Moms"]
            "sms" [:a.links.contact
                   {:href "https://twitter.com/happy.moms"
                    :target "_blank"}
                   "Happy Moms (Viber)"]
            "email"  [:a.links.contact
                      {:href "https://twitter.com/happy.moms"
                       :target "_blank"}
                      "happy.moms@gmail.com"]
            [:p "We love you! Stay Happy!"])]]]])))


(defn- about-moms-popup
  []
  (comp/text-popup show-about? "glyphicon-sunglasses" :t-about-moms :t-about-moms-descr :t-be-happy))

(defn- partnership-popup
  []
  (comp/text-popup show-partnership? "glyphicon-comment" :t-partnership :t-partnership-descr :t-make-better))

(defn- privacy-popup
  []
  (comp/text-popup show-privacy? "glyphicon-sunglasses" :t-privacy :t-privacy-descr :t-be-happy))



(rum/defc menu-popups
  []
  [:div.moms
   (side-menu)
   (about-moms-popup)
   (partnership-popup)
   (privacy-popup)])
