(ns data.game
  (use data.utils))

(defn mark
  [mark items]
  (cons mark items))

(defn as-attack
  [n]
  (comp (partial cons 'attack) (partial take n)))

(defn as-move
  [n]
  (comp (partial cons 'move) (partial take n)))

(def attack-first 
   (comp as-attack list first))

;(defprotocol Line
;  (k [this] this)
;  (p [this] this))
;
;(deftype LineV [line]
;  Line
;  (k [this]
;      (juxt (comp attack-first line front) (comp attack-first line back)))
;  (p [this]
;    (comp as-move list first line front)))
;
;(deftype LineH [line]
;  Line
;  )
;
;(deftype LineD [line]
;  clojure.lang.IFn
;    (invoke [this n] (line n))
;  Line
;    (k [this] 
;      (comp (partial mark 'attack) (partial take 1) line front))
;    (p [this] 
;      (comp (partial mark 'attack) (partial take 1) line front)))


(def oposite (partial map -))

(def black oposite)
(def white identity)

(def front identity)
(def back oposite)

(defn in-bounds?
  [n]
  (and (> n 0) (< n 9)))

(defn xy-in-bounds?
  [[x y]]
  (and (in-bounds? x) (in-bounds? y)))

(defn inc-x
  [[x y]]
  [(inc x) y])

(defn inc-y
  [[x y]]
  [x (inc y)])

(defn dec-x
  [[x y]]
  [(dec x) y])

(defn dec-y
  [[x y]]
  [x (dec y)])

(def only-in-bounds (partial take-while xy-in-bounds?))

(defn abs-xy
  [[x y]]
  [(abs x) (abs y)])

(def as-abs (partial map abs-xy))


(def cells-seq #(comp rest only-in-bounds as-abs (partial iterate %)))

(def h-seq (cells-seq inc-x))
(def h h-seq)

(def v-seq (cells-seq inc-y))
(def v v-seq)

(def d1-seq (cells-seq (comp dec-x inc-y)))
(def d1 d1-seq)

(def d2-seq (cells-seq (comp inc-x inc-y)))
(def d2 d2-seq)


(def lines (list v h d1 d2))

(def front-and-back
  #(juxt (comp % front) (comp % back)))

;(defmacro def-fig
;  [name & args]
;  `(let [a# (reverse '~@args)
;         d# (first a#)
;         [front# back#] (reverse (rest a#))
;         [mark# & lines#] d#
;         expr# (first lines#)]
;     (def ~name expr#)))

;(def-fig q
;  (front back
;    (as-attack 8 d1 d2 v h)))
;
;(def-fig k
;  (front back
;    (as-attack 1 d1 d2 v h)))
;

(defmacro def-fig
  [name & args]
   `(def ~name `(list first args)))


(def-fig p
  (front
    ((attack 1) d1 d2)))
    

(defn df []
  (macroexpand-1 '(def-fig p (front ((attack 1) d1 d2)))))

;(def p
;  (juxt (comp (as-attack 1) d1 front) (comp (as-attack 1) d2 front) (comp (as-move 1) v front)))


;(def pos '((list [1 1] black k) (list [1 3] white k) (list [2 8] white q)))
(def pos (list '([2 7] white k) '([2 3] black p)))


(def only-xy (partial map first))

(defn include?
  [value coll]
  (some (partial = value) coll))

(defn x-pos?
  [xy pos]
  (->> pos only-xy (include? xy)))

(defn x
  [pos xy]
  (if (x-pos? xy pos)
    (list xy 'x)
    xy))

(defn x-position
  [[line-type front back] pos]
  (let [f (partial map (partial x pos))]
    (list line-type (f front) (f back))))

(defn base-calc
  [[xy color fig] line]
  (-> xy color (fig line)))

(defn expand
  [fig]
  (let [fig-fun (map eval fig)]
    (cons fig (map (partial base-calc fig-fun) lines))))

(def rr
  (partial map expand))

(defn attack?
  [[mark _]] 
  (= mark 'attack))

(defn only-attack
  [[line-type & lines]]
  (cons line-type (filter attack? lines)))

(defn only-with
  [[line-type & lines] xy]
  (filter (partial include? xy) lines))

;(defn select-attack
  ;[xy [fig & lines]]
  ;(-> line only-attack (only-with xy)))

;(x (fig line) (fig1 line)) xy ->
;(attack? f f1)

;(defn ee
;  [[fig & lines]]
;  (map (comp (partial ) attack-lines) lines))

(defn is-check?
  [[xy color fig]]
  (filter (partial attack? xy) (rr pos)))

