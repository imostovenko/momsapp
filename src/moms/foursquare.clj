(ns moms.foursquare
  (:require
    [ajax.core :refer [GET POST]]
    [cheshire.core :as json]
    [org.httpkit.client :as http])
  (:refer-clojure :exclude [get]))



(defn get-venues-compact
  []
  (let [uri (str "https://api.foursquare.com/v2/venues/search?near=kiev&oauth_token=DXCJLIL34XTYCKOR5LHNOKTS4TMSBO45X5BSF4UTSM2WMTPT&v=20160807")]
    ;(io! (json/decode  (:body @(GET url
    ;                             {:handler #(js/console.log %)
    ;                              :error-handler #(js/console.log %)}))))
    (io! (json/decode (:body @(http/get uri {:accept :json :throw-exceptions false})) true))))

