(ns strokes.examples.circle-pack
  (:require [clojure.string :refer [join]]
            [mrhyde :refer [repersist]]
            [strokes :refer [d3]]))

; demo-guard - this is only needed because the demo is packaged with the library
(if (and d3 (this-as ct (aget ct "strokes_demo")) (= js/strokes_demo "circle-pack")) (do


(def diameter 960)
(def formatfn (d3/format ",d"))

(def pack (-> d3 .-layout .pack
  (.size [(- diameter 4), (- diameter 4)])
  (.value :size)))

(def svg (-> d3 (.select "body") (.append "svg")
    (.attr "width" diameter)
    (.attr "height" diameter)
  (.append "g")
    (.attr "transform" "translate(2,2)")))

; (-> d3 (.json "flare-full.json" (fn [error, jsroot]
;    (let [root (js->clj jsroot :keywordize-keys true)
;          node (-> svg (.datum root) (.selectAll ".node")

  (-> d3 (.edn "flare.edn" (fn [error, root]
    (let [node (-> svg (.datum root) (.selectAll ".node")
                  (.data (repersist (.-nodes pack)))
                .enter (.append "g")
                  (.attr "class" #(if (contains? % :children) "node" "leaf node"))
                  (.attr "transform" #(str "translate(" (:x %) "," (:y %) ")")))]

    (-> node (.append "title")
      (.text #(str (:name %) (if (contains? % :children) "" (formatfn (:size %))))))

    (-> node (.append "circle")
      (.attr "r" :r))

    (-> node (.filter #(not (:children %))) (.append "text")
      (.attr "dy" ".3em")
      (.style "text-anchor" "middle")
      (.text #(subs (:name %) 0 (/ (:r %) 3))))

  ))))

(-> d3 (.select (.-frameElement js/self)) (.style "height" (str diameter "px")))

)) ; end demo-guard