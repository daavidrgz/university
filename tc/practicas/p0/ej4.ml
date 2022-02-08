type 'a conjunto = Conjunto of 'a list;;

let conj = Conjunto [2;4;6;8];;
let conj2 = Conjunto [5;2;1];;
let conj3 = Conjunto [1];;

(* ===== PERTENECE ===== *)
let pertenece a = function
  Conjunto l -> List.exists (function x -> x = a) l
;;

pertenece 4 conj;;
(* - : bool = true *)
pertenece 5 conj;;
(* - : bool = false *)


(* ===== AGREGAR ===== *)
let agregar a conj = match conj with
  Conjunto l -> if pertenece a conj then conj
                else Conjunto (a::l)
;;

agregar 6 conj;;
(* - : int conjunto = Conjunto [2; 4; 6; 8] *)
agregar 5 conj;;
(* - : int conjunto = Conjunto [5; 2; 4; 6; 8] *)


(* ===== CONJUNTO DE LISTA ===== *)
let conjunto_of_list l = Conjunto l;;

conjunto_of_list [1;2;3];;
(* - : int conjunto = Conjunto [1; 2; 3] *)


(* ===== SUPRIMIR ===== *)
let suprimir a = function
  Conjunto l -> Conjunto (List.filter (function x -> x != a) l)
;;

suprimir 8 conj;;
(* - : int conjunto = Conjunto [2; 4; 6] *)
suprimir 5 conj;;
(* - : int conjunto = Conjunto [2; 4; 6; 8] *)


(* ===== CARDINAL ===== *)
let cardinal = function
  Conjunto l -> List.length l
;;

cardinal conj;;
(* - : int = 4 *)
cardinal (Conjunto []);;
(* - : int = 0 *)


(* ===== UNION ===== *)
let union conj1 conj2 = match (conj1, conj2) with
  (Conjunto l1, Conjunto l2) ->
    let rec aux res = function
      [] -> res
    | h::t -> if pertenece h conj1 then aux res t
              else aux (h::res) t in
    Conjunto (aux l1 l2)
;;

union conj conj2;;
(* - : int conjunto = Conjunto [1; 5; 2; 4; 6; 8] *)


(* ===== INTERSECCION ===== *)
let interseccion conj1 conj2 = match (conj1, conj2) with
  (Conjunto l1, Conjunto l2) ->
    let rec aux res = function
      [] -> res
    | h::t -> if pertenece h conj1 then aux (h::res) t
              else aux res t in
    Conjunto (aux [] l2)
;;

(* ALTERNATIVA SIN FUNCION AUXILIAR *)
let rec interseccion conj1 conj2 = match (conj1, conj2) with
  (Conjunto l1, Conjunto (h::t)) -> if pertenece h conj1 then Conjunto (h::list_of_conjunto (interseccion conj1 (conjunto_of_list t)))
                                    else Conjunto (list_of_conjunto (interseccion conj1 (conjunto_of_list t)))
| (_, _) -> Conjunto []
;;

interseccion conj conj2;;
(* - : int conjunto = Conjunto [2] *)


(* ===== DIFERENCIA ===== *)
let diferencia (Conjunto l1) (Conjunto l2) = 
  let rec aux res l = function
    [] -> res
  | h::t -> if pertenece h (conjunto_of_list l) then aux res l t
            else aux (h::res) l t in
  Conjunto (aux (aux [] l1 l2) l2 l1)
;;

diferencia conj conj2;;
(* - : int conjunto = Conjunto [8; 6; 4; 1; 5] *)


(* ===== INLUIDO ===== *)
let incluido (Conjunto l1) conj2 = List.for_all (function x -> pertenece x conj2) l1;;

incluido conj3 conj;;
(* - : bool = false *)
incluido conj3 conj2;;
(* - : bool = true *)


(* ===== IGUAL ===== *)
let igual conj1 conj2 = match (conj1, conj2) with
  (Conjunto l1, Conjunto l2) -> if List.compare_lengths l1 l2 != 0 then false
                                else incluido conj1 conj2
;;

igual conj conj2;;
(* - : bool = false *)
igual conj conj;;
(* - : bool = true *)

(* ===== PRODUCTO CARTESIANO ===== *)
let producto_cartesiano conj1 conj2 = match (conj1, conj2) with
  (Conjunto l1, Conjunto l2) ->
    let rec aux a res = function
      [] -> res
    | h::t -> aux a ((a,h)::res) t in
    Conjunto (List.fold_left (fun l x -> (aux x l l2)) [] l1)
;;

producto_cartesiano conj conj2;;
(* - : (int * int) conjunto =
Conjunto [(1, 8); (5, 8); (2, 8); (1, 6); (5, 6); (2, 6); (1, 4); (5, 4); (2, 4); (1, 2); (5, 2); (2, 2)] *)


(* ===== LISTA DE CONJUNTO ===== *)
let list_of_conjunto (Conjunto l) = l;;

list_of_conjunto conj;;
(* - : int list = [2; 4; 6; 8] *)
