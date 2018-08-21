(ns moms.text)
  ;(:require
  ;  [rum.core :include-macros true :as rum]))


(defonce lang (atom :EN))

;
;
;(rum/defc change-lang
;  []
;  (let [l {:EN "en"
;           :UA "ua"}
;        set-lang! #(reset! lang %)]
;
;    [:div.btn-group-vertical.btn-group-xs.col-sm-6
;     (for [i (keys l)]
;       [:button.btn.btn-default#lang
;        {:class (if (= i @lang) "active" "")
;         :type "button"
;         :value i
;         :on-click (set-lang! i)}
;        (i l)])]))


(def copies
  {;===== SIDE MENU ========
   :t-shop
     {:EN "Shop-Shop"
      :UA "Shop-Shop"}
   :t-recommend-new-place
    {:EN "Recommend new place"
     :UA "Додай нове місце"}

   :t-about-moms
     {:EN "About Happy Moms"
      :UA "Про Happy Moms"}
   :t-about-moms-descr
     {:EN "ЕИВ"
      :UA "TBD"}


   :t-partnership
     {:EN "Partnership and Sponsorship"
      :UA "Партнерство та cпонсорство"}
   :t-partnership-descr
     {:EN "We are offering various options for partenrship. You can also become a sponsor."
      :UA "TBD"}



   :t-privacy
     {:EN "Privacy Policy"
      :UA "Privacy Policy"}
   :t-privacy-descr
     {:EN "Our policy to save your privacy"
      :UA "TBD"}

   ;;; ==== HOME screen ========
   :t-search-hint
     {:EN "Search for kids-friendly places..."
      :UA "Пошук за назвою закладів.."}

   :t-kids-facilities
     {:EN "Kids Facilities"
      :UA "Зручності"}
   :t-moms-rating
     {:EN "Moms's rating"
      :UA "Рейтинг"}
   :t-price
     {:EN "Price"
      :UA "Ціна"}
   :t-makemomhappy
     {:EN "Make Mom Happy"
      :UA "Потіш Матусю"}
   :t-view-all-places
     {:EN "View all places"
      :UA "Всі Заклади"}
   :t-view-all-fac
     {:EN "View detail.."
      :UA "деталі.."}
   :t-select-facilities
     {:EN "Select facilites"
      :UA "Вибери Зручності"}
   :t-select
     {:EN "Select"
      :UA "Вибрати"}

   :t-do-you-like
     {:EN "Tell us do you like this place?"
      :UA "Чи сподобалось тобі тут?"}

   :t-tell-owner-what-missed
     {:EN "Tell the Owner what is missed here"
      :UA "Розкажи Власнику закладу, чого не вистачає ... "}
   :t-tell-owner-which
     {:EN "Tell the Owner which facilities will make You happier"
      :UA "Розкажи власнику закладу, які ще зручності для малечі були б корисними"}
   :t-tell-owner
     {:EN "Tell Owner"
      :UA "Розказати Власнику"}

   :t-be-happy
     {:EN "Be Happy"
      :UA "TBD"}

   :t-make-better
   {:EN "Together For Better World"
    :UA "Разом За кращий світ"}

   :t-nothing-found
   {:EN "Sorry, no places found for your request. Try to change some, or tap View all venues"
    :UA "Дуже прикро, але жодного місця, яке б задовольняло ваш запит, не знайдено. Спробуйте переглянути Всі заклади"}})





(defn t
  "get all copies, except facilities (ft)"
  ([text]
   (t text @lang))
  ([text la]
   (-> copies text la)))


(def facilities-copies
  {:change-table
     {:title
        {:EN "Changing Table"
         :UA "Пеленальний Столик"}
      :description
         {:EN "Place where you can change your baby"
          :UA "Місце для зміни підгузка немовляти"}}
   :baby-chair
     {:title
        {:EN "Baby Chair"
         :UA "Cтільчик для Годування"}
      :description
        {:EN "Baby feeding chair"
         :UA "Високий стільчик для годування дитини"}}
   :vegan
     {:title
        {:EN "Vegetarian Food"
         :UA "Вегетеріанська Їжа"}
      :description
        {:EN "Food options for vegetarians"
         :UA "В наявності страви без мяса"}}
   :meds
     {:title
        {:EN "Emergency Kit"
         :UA "Аптечка"}
      :description
        {:EN "Ask stuff for meds if needed"
         :UA "Запитайте персонал про аптечка в разі необхідності"}}
   :art-stuff
     {:title
        {:EN "Art Stuff"
         :UA "Розмальовки-Олвці"}
      :description
        {:EN "Pencils, coloring books, art materials"
         :UA "Розмальовки, олівці та інше для творчості"}}
   :baby-siter
     {:title
        {:EN "Baby Siter"
         :UA "Няня"}
      :description
        {:EN "Baby siter for your kid"
         :UA "Можливо поручити дитину під догляд няні"}}
   :kid-toilet
     {:title
        {:EN "Children Toilet-Seat"
         :UA "Дитячий Туалет"}
      :description
        {:EN "Toilet accommodated for small kids"
         :UA "Туалет обладнаний дитячим унітазом чи накладкою"}}
   :to-go
     {:title
        {:EN "Take-Away"
         :UA "Їжа з Собою"}
      :description
        {:EN "Food available for take-away"
         :UA "Можна замовляти їжу на виніс"}}
   :indoor-playarea
     {:title
        {:EN "Play Room"
         :UA "Ігрова Кімната"}
      :description
        {:EN "Indoor play area"
         :UA "Ігрова зона чи кімната в приміщенні"}}
   :lactation
     {:title
        {:EN "Nursery"
         :UA "Годування Груддю"}
      :description
        {:EN "lactation place where you can feed your baby"
         :UA "Є місце, де можливо погодувати немовля груддю"}}
   :sofa
     {:title
        {:EN "Sofas"
         :UA "Диванчики"}
      :description
        {:EN "Sofas or poufs available"
         :UA "Диванчики чи пуфи для зручного відпочинку"}}
   :terrase
     {:title
        {:EN "Terrase"
         :UA "Тераса"}
      :description
        {:EN "Place to sit outdoor"
         :UA "Місце зі столиками на вулиці"}}
   :video-games
     {:title
        {:EN "Video Games"
         :UA "Відео Ігри"}
      :description
        {:EN "Video games"
         :UA "Ігрова приставка чи відео ігри"}}
   :pets-friendly
     {:title
        {:EN "Pets Friendly"
         :UA "Можна з Тваринами"}
      :description
        {:EN "You can take your dogs with you"
         :UA "Беріть свого улюленця з собою"}}
   :baby-bed
     {:title
        {:EN "Baby Bed"
         :UA "Ліжечко"}
      :description
        {:EN "Here is a baby cot"
         :UA "Дитяче ліжечко для відпочинку вашого малюка"}}
   :outdoor-playarea
     {:title
        {:EN "Playgarden"
         :UA "Дитячий Майданчик"}
      :description
        {:EN "Outdoor playgarden nearby"
         :UA "Дитячий майданчик на вулиці"}}
   :kids-parties
     {:title
        {:EN "Kids Parties"
         :UA "Дитячі Свята"}
      :description
        {:EN "Possible to organaize kids parties here"
         :UA "Можливе проведення чи організація дитячих свят"}}
   :stroller-access
     {:title
        {:EN "Stroller Access"
         :UA "Доступ з Візочком"}
      :description
        {:EN "Easy access with stroller"
         :UA "Легкий доступ з дитячим візочком, пандус"}}
   :parking
     {:title
        {:EN "Parking"
         :UA "Парковка"}
      :description
        {:EN "Car parking nearby"
         :UA "Автомобільна парковка біля закладу"}}
   :animators
     {:title
        {:EN "Animators"
         :UA "Аніматори"}
      :description
        {:EN "Animators work for your kids"
         :UA "Аніматори розважать вашу дитину"}}
   :kids-menu
     {:title
        {:EN "Kids Menu"
         :UA "Дитяче меню"}
      :description
        {:EN "Food for kids"
         :UA "Дієтичні страви для дітей"}}
   :mini-zoo
     {:title
        {:EN "Mini-Zoo"
         :UA "Міні-Зоо"}
      :description
        {:EN "Pets or other animals"
         :UA "Зоо-куточок з тваринками"}}})


(defn ft
  "get facilities copies"
  ([facility-key line-key]
   (ft facility-key line-key @lang))
  ([facility-key line-key la]
   (-> facilities-copies facility-key line-key la)))


