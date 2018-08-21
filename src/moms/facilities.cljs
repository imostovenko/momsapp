(ns moms.facilities
  (:require
    [moms.text :as tt]))
   ; [rum.core :include-macros true :as rum]))



(def FAC {:change-table
            {:icon "change-table"
             :title (tt/ft :change-table :title)
             :default-descr (tt/ft :change-table :description)}
          :baby-chair
            {:icon "baby-chair"
             :title (tt/ft :baby-chair :title)
             :default-descr (tt/ft :baby-chair :description)}
          :kids-menu
            {:icon "kids-menu"
             :title (tt/ft :kids-menu :title)
             :default-descr (tt/ft :kids-menu :description)}
          :art-stuff
            {:icon "art-stuff"
             :title (tt/ft :art-stuff :title)
             :default-descr (tt/ft :art-stuff :description)}
          :indoor-playarea
            {:icon "indoor-playarea"
             :title (tt/ft :indoor-playarea :title)
             :default-descr (tt/ft :indoor-playarea :description)}
          :outdoor-playarea
            {:icon "outdoor-playarea"
             :title (tt/ft :outdoor-playarea :title)
             :default-descr (tt/ft :outdoor-playarea :description)}
          :kids-parties
            {:icon "kids-parties"
             :title (tt/ft :kids-parties :title)
             :default-descr (tt/ft :kids-parties :description)}
          :animators
            {:icon "animators"
             :title (tt/ft :animators :title)
             :default-descr (tt/ft :animators :description)}
          :baby-siter
            {:icon "baby-siter"
             :title (tt/ft :baby-siter :title)
             :default-descr (tt/ft :baby-siter :description)}
          :lactation
            {:icon "lactation"
             :title (tt/ft :lactation :title)
             :default-descr (tt/ft :lactation :description)}
          :sofa
            {:icon "sofa"
             :title (tt/ft :sofa :title)
             :default-descr (tt/ft :sofa :description)}
          :stroller-access
            {:icon "stroller-access"
             :title (tt/ft :stroller-access :title)
             :default-descr (tt/ft :stroller-access :description)}
          :kid-toilet
            {:icon "kid-toilet"
             :title (tt/ft :kid-toilet :title)
             :default-descr (tt/ft :kid-toilet :description)}
          :baby-bed
            {:icon "baby-bed"
             :title (tt/ft :baby-bed :title)
             :default-descr (tt/ft :baby-bed :description)}
          :video-games
            {:icon "video-games"
             :title (tt/ft :video-games :title)
             :default-descr (tt/ft :video-games :description)}
          :terrase
            {:icon "terrase"
             :title (tt/ft :terrase :title)
             :default-descr (tt/ft :terrase :description)}
          :mini-zoo
            {:icon "mini-zoo"
             :title (tt/ft :mini-zoo :title)
             :default-descr (tt/ft :mini-zoo :description)}
          :pets-friendly
            {:icon "pets-friendly"
             :title (tt/ft :pets-friendly :title)
             :default-descr (tt/ft :pets-friendly :description)}
          :parking
            {:icon "parking"
             :title (tt/ft :parking :title)
             :default-descr (tt/ft :parking :description)}
          :meds
            {:icon "meds"
             :title (tt/ft :meds :title)
             :default-descr (tt/ft :meds :description)}
          :vegan
            {:icon "vegan"
             :title (tt/ft :vegan :title)
             :default-descr (tt/ft :vegan :description)}
          :to-go
            {:icon "to-go"
             :title (tt/ft :to-go :title)
             :default-descr (tt/ft :to-go :description)}})

(def keys-FAC
  (keys FAC))