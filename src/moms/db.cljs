(ns moms.db
  ;(:require-macros
  ;  [ajax.core :refer [GET POST]])
  (:require
    [ajax.core :refer [GET POST]]
    [moms.util :as u]
   ; [cljs.reader :as reader]
    [moms.facilities :as fac]
    [clojure.set]))


(defonce g-key (str "key=" "AIzaSyAFfMaK609tANRWpajdLjq0bBmyDCCk7ws"))
(def FILTER (atom #{}))
(defn reset-FILTER [] (reset! FILTER #{}))


;;=========VENUES list =========;;
;; This is a list of all venues found by search or all venues (search is empty)
;; when user press search it should make a query to datomic and pulled all fits
;; and return results in appropriate pattern as below


(def venues
  (atom
    #{{:db/id 17592186045422
       :venue/fid "53233f8b11d284e8749cf9c2"
       :venue/title "Villaggio"
       :venue/description "Итальянская семейная пиццерия Villaggio, пицца-шеф из знаменитой семьи Sorbillo, которая готовит пиццу в Италии с 1935 года!"
       :venue/category "Pizza Place"
       :venue/momsrate "2"
       :venue/price "1"
       :venue/hours "10:00 - 22:00"
       :venue/facilities [:art-stuff
                          :baby-chair
                          :indoor-playarea
                          :kids-menu
                          :pets-friendly
                          :stroller-access]}
      {:db/id 17592186045537
       :venue/fid "5423a2e0498e4b6aeeaba589"
       :venue/title "Villaggio Another"
       :venue/description "Итальянская семейная пиццерия Villaggio, пицца-шеф из знаменитой семьи Sorbillo, которая готовит пиццу в Италии с 1935 года!"
       :venue/category "Pizza Place"
       :venue/momsrate "3"
       :venue/price "2"
       :venue/hours "10:00 - 23:00"
       :venue/facilities [:art-stuff
                          :stroller-access]}}))






;===========CURRENT VENUE ==========;
; Current Venue need to be reset by the details pulled from datomic by :id.
; :id is taken when User clicks on venue in the list


(def current-venue
  (atom
    {:db/id 17592186045422
     :venue/fid "53233f8b11d284e8749cf9c2"
     :venue/title "Villaggio from local"
     :venue/description "Итальянская семейная пиццерия Villaggio, пицца-шеф из знаменитой семьи Sorbillo, которая готовит пиццу в Италии с 1935 года!"
     :venue/category "Pizza Place"
     :venue/phone "0442278208"

     :venue/momsrate "2"
     :venue/price "1"

     :venue/furl "http://4sq.com/1eALsf3"

     :venue/lat "50.43749207594986"
     :venue/lng "30.621664038791792"

     :venue/address "Регенераторная, 4"
     :venue/city "Kyiv"

     :venue/facilities [:art-stuff
                        :baby-chair
                        :indoor-playarea
                        :kids-menu
                        :pets-friendly
                        :stroller-access]}))





(defn venue-does-have
  [venue-map]
  (:venue/facilities venue-map))


(defn venue-doesnt-have
  [venue-map]
  (clojure.set/difference  (set fac/keys-FAC) (set (venue-does-have venue-map))))


(defn venue-vs-filter
  [venue-map]
  (clojure.set/intersection (set (venue-does-have venue-map)) (set @FILTER)))



(defn venue-vs-filter-adsent
  [venue-map]
  (clojure.set/intersection (set (venue-doesnt-have venue-map)) (set @FILTER)))



(defn venue-fit
  [venue-map]
  (when (> (count @FILTER) 0)
    (Math/round (* (/ (count (venue-vs-filter venue-map)) (count @FILTER)) 100))))


(defn- staticmap-url
  [lat lng]
  (let [base "https://maps.googleapis.com/maps/api/staticmap?"
        size "size=240x240"
        scale "scale=2"
        maptype "maptype=terrain"
        zoom "zoom=11"
        pin-color "color:pink"
        pin-address (str lat "," lng)
        pin-icon "icon:https://goo.gl/nURJvC"
        markers (str "markers=" pin-color "|" pin-address)]
    (str base "&"
          size "&"
          scale "&"
          maptype "&"
          markers "&"
          zoom "&"
          g-key)))



(defn- dynamicmap-url
  [lat lng]
  (let [base "https://www.google.com/maps/embed/v1/place?"
        q (str "q=" lat "," lng)]
    (str base "&" q "&" g-key)))



;;;;;==========GET DATA FROM FOURSQUARE=========;;;;

(def f-venue
  (atom {}))


(defn f-venue-response
  [f-venue]
  (let [v (get (get f-venue "response") "venue")]
      {:venue/title (get v "name")
       :venue/description (get v "name")
       :venue/fid (get v "id")
       :venue/category (get (first (get v "categories")) "name")
       :venue/phone (get (get v "contact") "phone")
       :venue/momsrate "2"
       :venue/price (or (str (get (get v "price") "tier")) "3")
       :venue/hours (str (get (first (get (get v "hours") "timeframes")) "days")
                         ", "
                         (get (first (get (first (get (get v "hours") "timeframes")) "open")) "renderedTime"))
       :venue/furl (get v "shortUrl")

       :venue/lat (str (get (get v "location") "lat"))
       :venue/lng (str (get (get v "location") "lng"))

       :venue/address (str (get (get v "location") "address")
                           ", "
                           (get (get v "location") "city"))

       :venue/city (get (get v "location") "city")}))





;;;;;==========HANDLERS============;;;;;

;;;=== from Datomic====

(defn get-v
  "Getting ONE venue from Datomic"
  [id]
  (GET (str "/api/venue/" id)
    {;:handler js/console.log
     :handler #(reset! current-venue (u/transit->obj %))
     :error-handler #(js/console.log "error")}))

(defn get-fit-v
  "Getting only those venues which fit"
  [fac]
  (POST "/api/fit-venues/"
    {:params {:p fac}
     ;:format :json
     :handler #(reset! venues (set (u/transit->obj %)))
     :error-handler #(js/console.log "NOK")}))


(defn get-search-v
  "Getting venues by name in Search"
  [search]
  (POST "/api/search-venues/"
    {:params {:s search}
     :handler #(reset! venues (set (u/transit->obj %)))
     :error-handler #(js/console.log "NOK")}))


(defn get-all-v
  "Getting all venues from Datomic"
  []
  (GET "/api/all-venues/"
    {:handler #(reset! venues (set (u/transit->obj %)))
     :error-handle #(js/console.log "error on getting all venues from Datomic")}))



;;;===Foursquare====

(defn get-ve
  "Getting Venue from Foursquare "
  [fid]
  ;(let [id "5423a2e0498e4b6aeeaba589"]
  (GET (str "https://api.foursquare.com/v2/venues/" fid "?oauth_token=DXCJLIL34XTYCKOR5LHNOKTS4TMSBO45X5BSF4UTSM2WMTPT&v=20160807&locale=en")
    {;:handler js/console.log
     :handler #(do
                (reset! f-venue (f-venue-response %)))
     :format :json
     :error-handler #(js/console.log "error")}))


(defn get-all-ve
  "Getting ALL VENUES IDs and shortest info of them from Foursquare"
  []
  (GET "https://api.foursquare.com/v2/venues/search?near=kiev&oauth_token=DXCJLIL34XTYCKOR5LHNOKTS4TMSBO45X5BSF4UTSM2WMTPT&v=20160807"
    {:handler js/console.log
     ;:format :json
     :error-handler #(js/console.log "error")}))




;;;;=====Update DATOMIC with NEW VENUES =======;;;;


(defn post-venue!
  "Adding or updating venue to Datomic"
  [v]
  (POST "/api/add-v/"
    {:params v
     ;:format :json
     :handler #(js/console.log %)
     :error-handler #(js/console.log "NOK")}))


(defn F->D
  ; fids should be
  ; ["4c38774b1a38ef3b8ba29221"
  ;  "501f66b8e4b00e1d09c90ce1"
  ;  "4c5d4e7c9b28d13a2a225970"]
  [fids]
  (for [i fids]
    (do (print i)
        (get-ve (str i))
        (post-venue! @f-venue))))