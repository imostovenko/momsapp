(ns moms.datomic
  (:require
   [datomic.api :as d]))



(def url (str "datomic:free://localhost:4334/moms"))
(def conn (d/connect url))
(defn db [] (d/db conn))


;:venue/fid
;:venue/title
;:venue/description - MOMS
;:venue/city
;:venue/address
;:venue/phone
;:venue/lat
;:venue/lng
;:venue/furl
;:venue/hours - no
;:venue/isOpen - no
;:venue/price
;:venue/category
;:venue/momsrate  - MOMS
;:venue/facilities  - MOMS
; and where are PHOTOS ?????

;{:venue/lat "51.43749207594986",
; :venue/momsrate "2",
; :venue/hours "11:00 - 23:00",
; :venue/facilities [:art-stuff :baby-chair :indoor-playarea :to-go],
; :venue/price "3",
; :venue/fid "53233f8b11d284e8749cf9c3",
; :venue/category "Pizza Place",
; :venue/description "Итальянская семейная пиццерия Villaggio, пицца-шеф из
;                      знаменитой семьи Sorbillo, которая готовит
;                       пиццу в Италии с 1935 года!Итальянская семейная пиццерия Villaggio, пицца-шеф из
;                      знаменитой семьи Sorbillo, которая готовит
;                       пиццу в Италии с 1935 года!",
; :venue/furl "http://4sq.com/1eALsf3",
; :venue/lng "31.621664038791792",
; :db/id 17592186045537,
; :venue/address "Регенераторная, 4",
; :venue/phone "0442278208",
; :venue/city "Kyiv",
; :venue/title "Villaggio from Datomic"}




(def schema [{:db/id #db/id [:db.part/db]
              :db/ident :venue/title
              :db/doc "Title"
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/index true
              :db/fulltext true
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db]
              :db/ident :venue/fid
              :db/doc "Foresquare ID"
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one
              :db/unique :db.unique/identity
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/description,
              :db/doc "Description"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/address,
              :db/doc "Street address"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/phone,
              :db/doc "Contact phone"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/city,
              :db/doc "City"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/lat,
              :db/doc "Latitute"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/lng,
              :db/doc "Longitude"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/furl,
              :db/doc "ShortURL on foursquare, i.e. http://4sq.com/1eALsf3"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/hours,
              :db/doc "Open hours"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/isOpen,
              :db/doc "Is Open or closed now"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/price,
              :db/doc "Cheep or expensive"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/category,
              :db/doc "shortName of Category name"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/momsrate,
              :db/doc "MomsRating from 1 to 3"
              :db/valueType :db.type/string,
              :db/cardinality :db.cardinality/one,
              :db.install/_attribute :db.part/db}

             {:db/id #db/id [:db.part/db],
              :db/ident :venue/facilities,
              :db/doc "Facilities array that this venue has"
              :db/valueType :db.type/keyword,
              :db/cardinality :db.cardinality/many,
              :db.install/_attribute :db.part/db}])


@(d/transact conn schema)


(defn get-venues-deep
  []
  (map #(d/pull (db) '[*] %)
    (d/q '[:find [?e ...]
           :in $
           :where [?e :venue/title]]
      (db))))



(defn venues-by-facilities
  [facilities]
  (->>
    (d/q '[:find [?e ...]
           :in $ [?f ...]
           :where [?e :venue/facilities ?f]]
      (db) facilities)
    (d/pull-many (db) '[*]))) ; all details
    ;(d/pull-many (db) '[:venue/title :venue/facilities]))) ;specific details



(defn search-venues
  "Search venues by search-string in venues titles"
  [search]
  (->>
    (d/q '[:find [?e ...]
           :in $ ?search
           :where [(fulltext $ :venue/title ?search) [[?e]]]]
      (db) search)
    (d/pull-many (db) '[*])))



(defn get-venues-ids
  []
  (d/q '[:find [?e ...]
         :in $
         :where [?e :venue/title]]
    (db)))


(defn get-venue
  "geting venue details by datomic db/id or by fid"
  [id]
  (d/pull (db) '[*] id)) ;all attr
  ;(d/pull (db) '[:venue/title] id)) ;when pattern is needed


(defn get-venues-by-fid
  [fid]
  (d/q '[:find ?e ?fid
         :where [?e :venue/fid ?fid]]
                ;[?e :venue/momsrate "2"]]
    (db)))




(defn add-venue
  [venue-map]
  (let [v (conj {:db/id #db/id[:db.part/user]} venue-map)]
    (d/transact conn [v])))


(defn update-venue
  ;"Example for you, Dummy (moms.datomic/update-venue 17592186045537 {:venue/momsrate "2"})"
  [id attr]
  (let [v (conj {:db/id id} attr)]
    (d/transact conn [v])))




(defn delete-venue
  [id]
  (let [del-v [[:db.fn/retractEntity id]]]
    (d/transact conn del-v)))



;TODO search by name
;You can parameterize full text search. You can also join across fulltext results and other data.
; This query searches for communities of the specified :community/type
; and with a :community/category value containing the specified word.
;;
;[:find ?name ?cat
; :in $ ?type ?search
; :where
; [?c :community/name ?name]
; [?c :community/type ?type]
; [(fulltext $ :community/category ?search) [[?c ?cat]]]]


;TODO check which venues missed the attributes
;missing?
;[(missing? src-var ent attr)]
;The missing? predicate takes a database, entity, and attribute, and returns true if the entity has no value for attribute in the database.
;The following query finds all artists whose start year is not recorded in the database.
;;; query
;[:find ?name
; :where [?artist :artist/name ?name]
; [(missing? $ ?artist :artist/startYear)]]
;;; inputs
;db
;;; result
;#{["Sigmund Snopek III"] ["De Labanda's"] ["Baby Whale"] ...}





;;;;; -------- USERS ----------
;;

;;
;;
;;(defn get-users-ids []
;;  (d/q '[:find [?e ...]
;;         :in $
;;         :where [?e :user/login]]
;;    (db)))
;;
;
;(defn add-user [login pass]
;  (let [user [{:db/id #db/id[:db.part/user]
;               :user/login login
;               :user/pass pass
;               :user/isAdmin false}]]
;    (d/transact conn user)))
;   ; (get-users-deep)))
;
;;
;;(defn delete-user [u-id]
;;  (let [user [[:db.fn/retractEntity u-id]]]
;;    (d/transact conn user))
;;  (get-users-deep))
;;
;;
;;(defn get-u-id [login]
;;  (d/q '[:find [?e ...]
;;         :in $ ?login
;;         :where [?e :user/login ?login]]
;;    (db)
;;    login))
;;
;;
;;;;;;-------- PROJECTS --------
;;
;;(defn get-projects-deep []
;;  (map #(d/pull (db) '[*] %)
;;    (d/q '[:find [?e ...]
;;           :in $
;;           :where [?e :project/title]]
;;      (db))))
;;
;;(defn create-prj [title descr]
;;  (let [prj [{:db/id #db/id[:db.part/user]
;;              :project/title title
;;              :project/description descr}]]
;;    ;:project/author}]]
;;    (d/transact conn prj))
;;  (get-projects-deep))
;;
;;
;;(defn get-projects-ids []
;;  (d/q '[:find [?e ...]
;;         :in $
;;         :where [?e :project/title]]
;;    (db)))
;;
;;
;;(defn login-user [login password]
;;  (d/transact conn login))
;;
;
;
