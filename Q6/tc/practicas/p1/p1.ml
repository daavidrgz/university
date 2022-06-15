#load "talf.cma";;
open Conj;;
open Auto;;
open Ergo;;
open Graf;;

(* ============  Ej 1  ============ *)

let rec print_states f_states = function
  [] -> ()
| state::t ->
  let (Estado e) = state in
  if pertenece state f_states then Printf.printf "\x1b[4m%s\x1b[0m" e
  else Printf.printf "%s" e;
  if t != [] then print_string ", ";
  print_states f_states t
;;

let rec print_symbols = function
  [] -> ()
| (Terminal s)::[] -> Printf.printf "\"%s\"" s
| (Terminal s)::t -> Printf.printf "\"%s\", " s; print_symbols t
| _ -> ()
;;

let print_traza f_states c_states remaining_symbols =
  print_string "Estados actuales: ";
  print_string "{"; print_states f_states (list_of_conjunto c_states); print_string "}";
  print_newline ();
  
  print_string "Síbolos restantes: ";
  print_string "{"; print_symbols remaining_symbols; print_string "}";
  print_newline ();
  print_string "----------------------"; print_newline ();
;;

let traza_af cadena af =
  let Af (all_states, all_symbols, i_state, arcos, f_states) = af in

  let rec check_arcs symbol (Conjunto arcs) c_states = match arcs with
    [] -> []
  | (Arco_af (e1, e2, s))::t ->
      if s = symbol && pertenece e1 c_states then e2::(check_arcs symbol (Conjunto t) c_states) 
      else check_arcs symbol (Conjunto t) c_states in

  let next_states c_states s =
    let c_states_cierre = epsilon_cierre c_states af in
    let n_states = conjunto_of_list (check_arcs s arcos c_states_cierre) in
    epsilon_cierre n_states af in

  let rec traza_af_aux c_states symbols =
    print_traza f_states c_states symbols;
    match symbols with
    [] ->
      if interseccion c_states f_states = Conjunto [] then false
      else true
  | h::t ->
      traza_af_aux (next_states c_states h) t in

  traza_af_aux (conjunto_of_list [i_state]) cadena
;;


let a1 = af_of_string "0 1; a b; 0; 0; 0 1 a; 0 1 b; 1 0 a; 1 0 b;";;
let a2 = af_of_string "0 1 2 3; a b c; 0; 1 3; 0 1 a; 1 1 b; 1 2 a; 2 0 epsilon; 2 3 epsilon; 2 3 c;";;

traza_af (cadena_of_string "a b b") a1;;
traza_af (cadena_of_string "a b b d") a2;;

(* ============  Ej 2  ============ *)

let create_arcs (Conjunto arcs) = 
  let aux res_arcs a = 
    let ((Arco_af ((Estado from_s),(Estado to_s),s)), (Arco_af ((Estado from_s'),(Estado to_s'),s'))) = a in
    if s = s' then
      let arc = Arco_af ((Estado (from_s^from_s')), (Estado (to_s^to_s')), s) in
      arc::res_arcs
    else res_arcs in
  conjunto_of_list (List.fold_left aux [] arcs)
;;

let create_states (Conjunto states) =
  let concat (Estado e1, Estado e2) = (Estado (e1^e2)) in
  conjunto_of_list (List.map concat states)
;;

let cartesiano_af af1 af2 =
  let Af (all_states1, all_symbols1, (Estado i_state1), arcs1, f_states1) = af1 in
  let Af (all_states2, all_symbols2, (Estado i_state2), arcs2, f_states2) = af2 in

  let all_states = create_states (Conj.cartesiano all_states1 all_states2) in
  let all_symbols = Conj.interseccion all_symbols1 all_symbols2 in
  let f_states = create_states (Conj.cartesiano f_states1 f_states2) in
  let i_state = Estado (i_state1^i_state2) in
  let arcs = create_arcs (Conj.cartesiano arcs1 arcs2) in

  Af (all_states, all_symbols, i_state, arcs, f_states)
;;

let a1 = af_of_string "0 1 2; a b; 0; 2; 0 1 a; 0 0 b; 1 1 a; 1 2 b; 2 2 a; 2 2 b;";;
let a2 = af_of_string "3 4 5; a b; 3; 5; 3 4 b; 3 3 a; 4 4 b; 4 5 a; 5 5 a; 5 5 b;";;

let a = cartesiano_af a1 a2;;
traza_af (cadena_of_string "a b b a a") a;;
traza_af (cadena_of_string "a b b") a;;
dibuja_af a;;

(* ============  Ej 3  ============ *)

(* Sobre el alfabeto E={a,b,c}. El autómata a1 acepta las cadenas que empiezan por "a". El autómata
a2 acepta las cadenas que terminan por "b". El autómata a3 acepta las cadenas que no tienen "c"s consecutivas.*)

let a1 = af_of_string "0 1; a b c; 0; 1; 0 1 a; 1 1 a; 1 1 b; 1 1 c;";;
let a2 = af_of_string "2 3; a b c; 2; 3; 2 2 a; 2 2 c; 2 3 b; 3 3 b; 3 2 a; 3 2 c;";;
let a3 = af_of_string "3 4 5; a b c; 3; 3 4; 3 3 a; 3 3 b; 3 4 c; 4 3 a; 4 3 b; 4 5 c;";;

let a12 = cartesiano_af a1 a2;;
let a = cartesiano_af a12 a3;;
dibuja_af a;;

traza_af (cadena_of_string "") a;;
(* - : bool = false *)
traza_af (cadena_of_string "a b") a;;
(* - : bool = true *)
traza_af (cadena_of_string "c c") a;;
(* - : bool = false *)
traza_af (cadena_of_string "a c c b") a;;
(* - : bool = false *)
traza_af (cadena_of_string "a c a c b") a;;
(* - : bool = true *)
