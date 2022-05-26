#load "talf.cma";;
open Conj;;
open Auto;;
open Ergo;;
open Graf;;

(* ============  Ej 1  ============ *)

let extra_arcs i_symbol =
	Conjunto [
		Arco_ap (Estado "0", Estado "1", Terminal "", No_terminal "", [i_symbol; No_terminal ""]);
		Arco_ap (Estado "1", Estado "2", Terminal "", No_terminal "", [No_terminal ""])
	]
;;

let create_arcs (Conjunto rules) (Conjunto symbols) i_symbol =
	let map_symbols s = Arco_ap (Estado "1", Estado "1", s, s, []) in
	let map_arcs (Regla_gic (head, body)) = Arco_ap (Estado "1", Estado "1", Terminal "", head, body) in

	let arcs = Conj.union (conjunto_of_list (List.map map_symbols symbols)) (conjunto_of_list (List.map map_arcs rules)) in
	Conj.union arcs (extra_arcs i_symbol)
;;

let ap_of_gic gic =
	let Gic (n_symbols, symbols, rules, i_symbol) = gic in

	let arcs = create_arcs rules symbols i_symbol in
	let states = Conjunto [Estado "0"; Estado "1"; Estado "2"] in
	let ap_stack_symbols = Conj.agregar (No_terminal "") (Conj.union n_symbols symbols) in
	let i_state = Estado "0" in
	let f_states = Conjunto [Estado "2"] in

	Ap (states, symbols, ap_stack_symbols, i_state, arcs, No_terminal "", f_states)
;;

let g1 = gic_of_string("S; a b; S; S -> a S b | epsilon;");;
let ap = ap_of_gic g1;;
(* dibuja_ap ap;; *)
escaner_ap (cadena_of_string "a a b b") ap;;


(* ============  Ej 2  ============ *)

exception No_encaja;;

let is_cima_gic cima = match cima with
	No_terminal "" -> false
| No_terminal _ -> true
| Terminal _ -> false
;;

let print_symbol symbol = match symbol with
	No_terminal "" -> print_string "Z"
| Terminal "" -> print_string "ε"
| No_terminal s -> print_string s
| Terminal s -> print_string s
;;

let print_arc arc = 
	let Arco_ap (origen, destino, entrada, cima, pila_arco) = arc in
	print_string "  ";
	print_symbol entrada; print_string ", ";
	print_symbol cima; print_string ", ";
	if pila_arco <> [] then
		(List.iter (print_symbol) pila_arco;)
	else
		print_symbol (Terminal ""); 
	print_string "\n"
;;

let print_underline s = Printf.printf "\x1b[4m%s\x1b[0m" s
let print_bold s = Printf.printf "\x1b[1m%s\x1b[0m" s

let print_rules arcs =
	let aux arc =
		let Arco_ap (origen, destino, entrada, cima, pila_arco) = arc in
		if is_cima_gic cima then
			(print_string "  ";
			print_symbol cima;
			print_string " -> ";
			if pila_arco <> [] then
				(List.iter (print_symbol) pila_arco;)
			else
				print_symbol (Terminal "");
			print_string "\n";)
	in
	List.iter aux arcs
;;

let encaja (estado, cadena, pila_conf, arcs) arc n =
	let Arco_ap (origen, destino, entrada, cima, pila_arco) = arc
	in
	let
		nuevo_estado =
			if estado = origen then destino
			else raise No_encaja
	and
		nueva_cadena =
			if entrada = Terminal "" then cadena
			else
				if (cadena <> []) && (entrada = List.hd cadena) then List.tl cadena
				else raise No_encaja
	and
		nueva_pila_conf =
			if cima = Terminal "" then
				pila_arco @ pila_conf
			else
				if (pila_conf <> []) && (cima = List.hd pila_conf) then
					pila_arco @ (List.tl pila_conf)
				else
					raise No_encaja
	in

	print_underline ("\n-> Nivel " ^ string_of_int n ^ "\n\n");

	print_bold "Arcos:\n";
	List.iter print_arc (List.rev (arc::arcs));

	print_bold "Reglas:\n";
	print_rules (List.rev (arc::arcs));

	print_bold "Cadena: "; print_string "\"";
	List.iter print_symbol nueva_cadena;
	print_string "\"\n";

	print_bold "Pila: ";
	List.iter print_symbol nueva_pila_conf;
	print_string "\n";

	(nuevo_estado, nueva_cadena, nueva_pila_conf, arc::arcs)
;;

let es_conf_final finales = function
	(estado, [], _, arcs) ->
		if pertenece estado finales then
			(print_underline "\nSolución\n";
			print_rules (List.rev arcs);
			true)
		else
			false
| _ -> false
;;

let traza_ap cadena (Ap (_, _, _, inicial, Conjunto delta, zeta, finales)) =
	let rec aux = function
		([], [], _, _) -> print_underline "\nNo existe solución\n\n"; false
	| ([], l, _, n) ->	aux (l, [], delta, n+1)
	| (_::cfs, l, [], n) -> aux (cfs, l, delta, n)
	| (cf::cfs, l, a::arcos, n) ->
		try
			let
				ncf = encaja cf a n
			in
				(es_conf_final finales ncf) || (aux (cf::cfs, ncf::l, arcos, n))
		with
			No_encaja -> aux (cf::cfs, l, arcos, n)
	in
	aux ([(inicial, cadena, [zeta], [])], [], delta, 0)
;;

let g1 = gic_of_string("S; a b; S; S -> a S b | epsilon;");;
let g2 = gic_of_string("S; a b; S; S -> a S b | a a S b | epsilon;");;

let ap1 = ap_of_gic g1;;
let ap2 = ap_of_gic g2;;

traza_ap (cadena_of_string "a a b b") ap1;;
