open G_tree;;

let t1 = Gt (3, [ Gt (8, []);
                  Gt (2, [Gt (5, []);
                          Gt (4, []);
                          Gt (1, [])])]);;

let rec breadth_first = function
    Gt (x, []) -> [x]
  | Gt (x, (Gt (y, t2))::t1) -> x :: breadth_first (Gt (y, t1@t2));;

(*Terminal function*)
let breadth_first_t tree =
    let rec aux l = function
        Gt (x, []) -> List.rev (x::l)
      | Gt (x, (Gt (y, t2))::t1) -> aux (x::l) (Gt (y, List.rev_append (List.rev t1) t2))
  in aux [] tree;;

(*Tree*)
let tree_init n =
    let rec aux n tree =
      if n = 0 then tree
      else aux (n-1) (Gt (n, [tree]))
  in aux n (Gt (1, []));;

let t = tree_init 100000;;