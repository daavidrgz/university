let rec mapdoble f g = function
  [] -> []
| h::t -> f h::mapdoble g f t
;;

let mapdoble f1 g1 l1 = (* Tail recursive *)
  let rec aux f g res = function
    [] -> res
  | h::t -> aux g f (f h::res) t
in aux f1 g1 [] (List.rev l1)
;;

mapdoble;;
(* val mapdoble : ('a -> 'b) -> ('a -> 'b) -> 'a list -> 'b list = <fun> *)

mapdoble (function x -> x*2) (function x -> "x") [1;2;3;4;5];;
(* Type error *)

let y = function x -> 5 in mapdoble y;;
(* - : ('_weak1 -> int) -> '_weak1 list -> int list = <fun> 
'_weak1 designa cualquier elemento de cualquier tipo *)


(* ------ PRUEBAS ------ *)
mapdoble (function x -> x) (function x -> -x) [1;2;3;4;5];;
(* - : int list = [1; -2; 3; -4; 5] *)

mapdoble (function x -> 2*x) (function x -> x*x) [2;4;6;8;10];;
(* - : int list = [4; 16; 12; 64; 20] *)
