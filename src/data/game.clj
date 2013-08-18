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

(defn expand-fig
  [[xy color fig]]
  (eval (list '-> xy (symbol (str "data.game/" color)) (symbol (str "data.game/" fig)))))

(defn line-with 
  [xy lines]
  (first (filter #(any? xy %) lines)))

(defn intersects-with
  [[kxy _ _ :as king] [fxy _ _ :as fig]]
    (println "debug: " fig)
    (if (not (= fxy kxy))
      (line-with kxy (expand-fig fig))))

(defn find-king 
  [p]
  (first (filter (fn [[_ _ f]] (= f "k")) p)))

(defn oposite-figs 
  [[_ color _] position]
  (filter (fn [[_ c _]] (not (= c color))) position))

(defn attack-lines
  [king figs]
  (map (partial intersects-with king) figs))

(defn between
  [[xy _ _] line]
  (take-while #(not (= xy %)) line))

(defn attack-cells
  [king attack-lines]
  (map #(between king %) attack-lines))

(defn collect-xy
  [p]
  (map first p))

(defn direct-attack? 
  [position line]
  (empty? (clojure.set/intersection (set (collect-xy position)) (set line))))

(defn direct-attack-lines
  [position lines]
  (filter #(direct-attack? position %) lines))

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
  (println "Analyse position: " pos)
  (let [position (to-coords pos)
        king     (find-king position)
        lines    (attack-lines king (oposite-figs king position))
        d1       (attack-cells king lines)
        d2       (direct-attack-lines position d1)]
    (println "debug: " d1)
    (map (partial map to-js) (drop-nil d2))))

