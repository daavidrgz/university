type 'a arbol_binario =
  Vacio
| Nodo of 'a * 'a arbol_binario * 'a arbol_binario
;;

let tree = Nodo (1, Nodo (3, Nodo (9, Vacio, Vacio),
                             Nodo (10, Vacio, Vacio)),
                    Nodo (5, Vacio, Vacio));;


(* ========== IN ORDEN ========== *)
let rec in_orden = function
  Vacio -> []
| Nodo(n, t1, t2) -> in_orden t1 @ (n::in_orden t2)
;;

in_orden tree;;
(* - : int list = [9; 3; 10; 1; 5] *)


(* ========== PRE ORDEN ========== *)
let rec pre_orden = function
  Vacio -> []
| Nodo(n, t1, t2) -> n::(pre_orden t1 @ pre_orden t2)
;;

pre_orden tree;;
(* - : int list = [1; 3; 9; 10; 5] *)


(* ========== POST ORDEN ========== *)
let rec post_orden = function
  Vacio -> []
| Nodo(n, t1, t2) -> post_orden t2 @ (n::post_orden t1)
;;

post_orden tree;;
(* - : int list = [5; 1; 10; 3; 9] *)


(* ========== ANCHURA ========== *)
let anchura =
  let rec aux res queue = match queue with
    [] -> List.rev res
  | Nodo(n, t1, t2)::t -> aux (n::res) (t @ [t1;t2])
  | Vacio::t -> aux res t in
  function tree -> aux [] [tree]
;;

anchura tree;;
(* - : int list = [1; 3; 5; 9; 10] *)
