open Bin_tree;;

let rec fold_tree f b tree = match tree with
    Empty -> b
  | Node (a, h1, h2) -> f a (fold_tree f b h1) (fold_tree f b h2);;

let t1 = Node (3, Node (8, Empty, Empty),
                  Node (2, Node (5, Empty, Empty),
                           Node (1, Empty, Empty)));;

let t2 = Node (1., Node (4., Empty, Empty),
                   Node (0., Empty, Empty));;

(*Sum & Prod*)
let sum_fun a n1 n2 = a + n1 + n2;;
let prod_fun a n1 n2 = a *. n1 *. n2;;

let sum tree = fold_tree sum_fun 0 tree;;
let prod tree = fold_tree prod_fun 1.0 tree;;

(*Num nodes*)
let num_fun a n1 n2 = 1 + n1 + n2;;
let num_nodes tree = fold_tree num_fun 0 tree;;

(*In-order*)
let ord_fun a n1 n2 = List.append n1 (a::n2);;
let in_order tree = fold_tree ord_fun [] tree;;

(*Mirror*)
let mirror_fun a n1 n2 = Node (a, n2, n1);;
let mirror tree = fold_tree mirror_fun Empty tree;;

(*Prod2*)
exception Mult_by_0;; (*Visto en el st_tree.ml ;)*)
let prod_fun2 a n1 n2 = 
  if a = 0. then raise Mult_by_0
  else a *. n1 *. n2;;

let prod2 tree = try fold_tree prod_fun2 1.0 tree with
    Mult_by_0 -> 0.;;