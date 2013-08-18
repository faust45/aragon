(ns data.game
  (use data.utils))

(def oposite (partial map -))

(def black oposite)
(def white identity)

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

(defn attack
  [n]
  #(take n %))

(def back 
  oposite)

(def front
  identity)

(defn mfun
  [alist blist]
  (for [a alist b blist]
    (comp a b)))

(defmacro deff
  [fname lines front-back & filters]
  `(def ~fname
     (apply juxt (mfun (map #(comp ~@filters %) (list ~@lines)) (list ~@front-back)))))

(deff p 
  (d1 d2) (front) (attack 1))

(deff q
  (d1 d2 v h) (front back) (attack 8))

(deff k
  (d1 d2 v h) (front back) (attack 1))

(deff r
  (v h) (front back) (attack 8))

(deff b
  (d1 d2) (front back) (attack 8))

(defn ef
  [[xy color fig]]
  (let [l (list '-> xy (symbol (str "data.game/" color)) (symbol (str "data.game/" fig)))]
    (eval l)))

(defn any?
  [v coll]
  ((comp not empty?) (filter (partial = v) coll)))

(defn find-d
  [lines xy]
  (first (filter (fn [l] (if (any? xy l) l)) lines)))

(defn intersects-with
  [[xy _ _]]
  (fn [f]
    (if (not (= (first f) xy))
      (find-d (ef f) xy))))

(defn find-king 
  [p]
  (first (filter (fn [[_ _ f]] (= f "k")) p)))

(defn oposite-figs 
  [p [_ color _]]
  (filter (fn [[_ c _]] (not (= c color))) p))

(defn game 
  [p king]
  (map (intersects-with king) (oposite-figs p king)))

(defn between
  [[xy _ _] line]
  (take-while #(not (= xy %)) line))

(defn game2
  [p king d]
  (map #(between king %) d))

(defn collect-xy
  [p]
  (map first p))

(defn blank-on-board 
  [p line]
  (empty? (clojure.set/intersection (set (collect-xy p)) (set line))))

(defn game3
  [p d]
  (filter #(blank-on-board p %) d))

(defn to-xy 
  [xy]
  (let [x (-> xy first str)
        y (-> xy last str)]
    [(Integer. x) (Integer. y)]))

(defn coords
  [[color fig xy]]
  (list (to-xy (str xy)) (if (= "w" (str color)) 'white 'black) fig))

(defn to-coords
  [pos]
  (map coords pos))

(defn to-js
  [xy]
  (apply str xy))

(defn analyze
  [pos]
    (println pos)
  (let [p (to-coords pos)
        king (find-king p)
        d  (game p king)
        d1 (game2 p king d)
        d2 (game3 p d1)]
    (map (partial map to-js) (drop-nil d2))))

