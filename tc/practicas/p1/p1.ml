#load "talf.cma";;
open Conj;;
open Auto;;
open Ergo;;
open Graf;;

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
  print_newline (); print_string "----------------------"; print_newline (); print_newline ();
  print_string "Estados actuales:"; print_newline ();
  print_string "{"; print_states f_states (list_of_conjunto c_states); print_string "}";
  print_newline ();

  print_string "Síbolos restantes:"; print_newline ();
  print_string "{"; print_symbols remaining_symbols; print_string "}";
  print_newline ();
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

(*
let a1 = af_of_string "0 1; a b; 0; 0; 0 1 a; 0 1 b; 1 0 a; 1 0 b;";;
let a2 = af_of_string "0 1 2 3; a b c; 0; 1 3; 0 1 a; 1 1 b; 1 2 a; 2 0 epsilon; 2 3 epsilon; 2 3 c;";;

traza_af (cadena_of_string "a b b") a1;;
traza_af (cadena_of_string "a b b d") a2;;
*)
