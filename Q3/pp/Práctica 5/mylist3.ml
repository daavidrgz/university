let rev = (*Aux function*)
    let rec aux l = function
      [] -> l
    | h::t -> aux (h::l) t
  in function l1 -> aux [] l1;;

let rev_append l1 l2 = (*Aux function*)
    let rec aux l = function
      [] -> l
    | h::t -> aux (h::l) t
  in aux l2 l1;;

let rec append l1 l2 = match l1 with (*Aux function*)
    [] -> l2
  | h::t -> h::append t l2;;

(******* FUNCTIONS *******)

let remove i l = (*Not tail-recursive*)
    let rec aux i l2 = function
      [] -> l2
    | h::t -> if h = i then append l2 t
              else aux i (append l2 (h::[])) t
  in aux i [] l;;

let remove_all i l = (*Tail-recursive*)
    let rec aux i l2 = function
      [] -> (rev l2)
    | h::t -> if h = i then aux i l2 t
              else aux i (h::l2) t
  in aux i [] l;;

let rec ldif l1 = function (*Tail-recursive*)
    [] -> l1
  | h::t -> ldif (remove_all h l1) t;;

let lprod l1 l2 = (*Tail-recursive*)
    let rec aux i l = function
      [] -> (rev l)
    | h::t -> aux i ((i,h)::l) t

    in let rec aux2 l l2 = function
      [] -> (rev l)
    | h::t -> aux2 (rev_append (aux h [] l2) l) l2 t
  in aux2 [] l2 l1;;

let divide l = (*Tail-recursive*)
    let rec aux i l1 l2 = function
      [] -> (rev l1), (rev l2)
    | h::t -> if i mod 2 = 0 then aux (i+1) (h::l1) l2 t
              else aux (i+1) l1 (h::l2) t
  in aux 0 [] [] l;;
