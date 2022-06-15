let rec primero_que_cumple pred = function
  [] -> raise Not_found
| h::t -> if pred h then h
          else primero_que_cumple pred t
;;

primero_que_cumple;;
(* - : ('a -> bool) -> 'a list -> 'a = <fun> *)

(* ----- PRUEBAS ----- *)
primero_que_cumple (function x -> x>5) [1;2;3;8;4];;
(* - : int = 8 *)
primero_que_cumple (function x -> x>5) [1;2;100;200;5];;
(* - : int = 100 *)
primero_que_cumple (function x -> x>5) [1;2;1;1;1];;
(* Exception: Not_found *)

let existe pred l =
  let aux pred l =
    try Some (primero_que_cumple pred l) with
      Not_found -> None in
  aux pred l != None
;;

(* ----- PRUEBAS ----- *)
existe (function x -> x>5) [1;2;1;1;1];;
(* - : bool = false *)
existe (function x -> x>5) [1;2;1;1;8];;
(* - : bool = true *)

let asociado l key =
  let pred = function (k,_) -> k = key in
  match primero_que_cumple pred l with
    (_,b) -> b
;;

(* PRUEBAS *)
asociado [("a",1);("b",2);("c",3);("d",4)] "d";;
(* - : int = 4 *)
asociado [("a",1);("b",2);("c",3);("d",4)] "a";;
(* - : int = 1 *)
asociado [("a",1);("b",2);("c",3);("d",4)] "t";;
(* Exception: Not_found *)



(* ALTERNATIVA SIN EXCEPCIONES (Más sencilla pero creo que no se adapta del todo al enunciado) *)
let rec primero_que_cumple pred = function
  [] -> None
| h::t -> if pred h then Some h
          else primero_que_cumple pred t
;;

primero_que_cumple;;
(* - : ('a -> bool) -> 'a list -> 'a option = <fun> *)

let existe pred l = primero_que_cumple pred l != None;;

let asociado l key =
  let pred = function (k,_) -> k = key in
  match primero_que_cumple pred l with
    Some(_,b) -> b
  | None -> raise Not_found
;;
