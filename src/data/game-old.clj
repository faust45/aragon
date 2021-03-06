(ns data.game)

(def position (ref {}))

(defn abs "(abs n) is the absolute value of n" [n]
  (cond
   (not (number? n)) (throw (IllegalArgumentException.
                 "abs requires a number"))
   (neg? n) (- n)
   :else n))

(defn call [^String nm & args]
    (when-let [fun (ns-resolve *ns* (symbol nm))]
        (apply fun args)))

(defn in-bounds?
  [n]
  (and (> (abs n) 0) (< (abs n) 9)))

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

(def inc-xy (comp inc-x inc-y))
(def dec-xy (comp dec-x dec-y))

(def only-in-bounds #(take-while xy-in-bounds? %))

(defn abs-xy
  [[x y]]
  [(abs x) (abs y)])

(defn oposite 
  [coll]
  (map - coll))

(defn cells 
  [fun xy]
  (->> [xy (oposite xy)] 
       (map #(only-in-bounds (iterate fun %)))))

(defn abs-cells 
  [fun xy]
  (->> xy (cells fun) (apply concat) (map abs-xy) set))

(def h  #(abs-cells inc-x %))

(def v  #(abs-cells inc-y %))

(def d1 #(abs-cells (comp inc-x dec-y) %))

(def d2 #(abs-cells (comp inc-x inc-y) %))




;(defn k
;  [color x y]
;  (map #((comp map-first %) x y) [diagonal-l diagonal-r vertical horizontal]))
;
;(defn q
;   [color x y]
;   (map #(% x y) [diagonal-l diagonal-r vertical horizontal]))
;
;(defn p
;  [color x y]
;  (if (= color "w")
;    (map #((comp first first %) x y) [diagonal-l diagonal-r])
;    (map #((comp first last %)  x y) [diagonal-l diagonal-r])))
;
;
;(defn resolve-pos
;  [[color fig pos]]
;  (call fig color (pos)))
;
;(defn make-check?
;  [[color fig] position]
;  (map figs position))
;
;
;;["w","q","a8"]
;;
;(defn board-lines
;  [fig]
;  )
;
;(defn relations
;  [fig position]
;  (board-lines fig))
;
;(defn build-relations
;  [position]
;  (map #(relations % position) position))

(defn mate?
  [position]
  true)

(defn who-make-check
  [position]
  (map position))

(defn analyze 
  [position]
  position)
