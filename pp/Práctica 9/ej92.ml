open Bin_tree;;

let rec fold_tree f b tree = match tree with
    Empty -> b
  | Node (a, h1, h2) -> f a (fold_tree f b h1) (fold_tree f b h2);;

let ord_fun a n1 n2 = List.append n1 (a::n2);;
let in_order tree = fold_tree ord_fun [] tree;;

let rec insert_tree f a t = match t with
    Empty -> Node (a, Empty, Empty)
  | Node (i, n1, n2) -> if f a i then Node (i, insert_tree f a n1, n2)
                        else Node (i, n1, insert_tree f a n2);;

let sort f l =
    in_order (List.fold_left (fun t a -> insert_tree f a t) Empty l);;