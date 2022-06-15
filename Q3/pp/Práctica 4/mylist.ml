let hd = function
    [] -> raise (Failure "hd")
  | h::_ -> h;;

let tl = function
    [] -> raise (Failure "tl")
  | _::t -> t;;

let length = 
    let rec sum n = function
        [] -> n
      | _::t -> sum (n+1) t 
  in function l -> sum 0 l;;

let rec compare_lengths l1 l2 = match (l1, l2) with
    [], [] -> 0
  | [], _ -> -1
  | _, [] -> 1
  | _::t1, _::t2 -> compare_lengths t1 t2;;

let rec nth l1 p =
  if p < 0 then raise (Invalid_argument "nth")
  else match l1 with
      [] -> raise (Failure "nth")
    | h::t -> if ( p = 0 ) then h
              else nth t (p-1);;

let rec append l1 l2 = match l1 with
    [] -> l2
  | h::t -> h::append t l2;;

(*----------------------------*)

let rec find f = function
    [] -> raise (Failure "Not_found")
  |	h::t -> if ( f h ) then h
            else find f t;;

let rec for_all f = function
    [] -> true
  | h::t -> (f h) && for_all f t;;

let rec exists f = function
    [] -> false
  | h::t -> (f h) || exists f t;;

let rec mem a = function
    [] -> false
  | h::t -> (a = h) || mem a t;;

let rec split = function
    [] -> [], []
  | h::t -> let x, y = h in
            let p, s = split t in
                x::p, y::s;;

let rec combine l1 l2 = match (l1, l2) with
    [], [] -> []
  | [], _ -> raise (Invalid_argument "combine")
  | _, [] -> raise (Invalid_argument "combine")
  | h1::t1, h2::t2 -> (h1, h2)::combine t1 t2;;

(*----------------------------*)

let init n f = 
  if n < 0 then raise (Invalid_argument "init")
  else
    let rec aux l = function
       -1 -> l
      | i -> aux (f(i)::l)(i-1)
    in aux [] (n-1);;

let rev =
    let rec aux l = function
      [] -> l
    | h::t -> aux (h::l) t
  in function l1 -> aux [] l1;;

let rev_append l1 l2 =
    let rec aux l = function
      [] -> l
    | h::t -> aux (h::l) t
  in aux l2 l1;;

let rec concat = function
    [] -> []
  | h::t -> append h (concat t);;

let rec flatten = function
    [] -> []
  | h::t -> append h (flatten t);;

let rec map f l = match l with
    [] -> []
  | h::t -> f h::map f t;;

let rev_map f l1 =
    let rec aux f l2 = function
      [] -> l2
    | h::t -> aux f (f h::l2) t
  in aux f [] l1;;

let rec map2 f l1 l2 = match (l1, l2) with
    [], [] -> []
  | [], _ -> raise (Invalid_argument "map2")
  | _, [] -> raise (Invalid_argument "map2")
  | h1::t1, h2::t2 -> f h1 h2::map2 f t1 t2;;

let rec fold_left f n l = match l with
    [] -> n
  | h::t -> fold_left f (f n h) t;;

let rec fold_right f l n = match l with
    [] -> n
  | h::t -> f h (fold_right f t n);;

let partition f l = 
    let rec aux f l1 l2 = function
      [] -> (rev l1), (rev l2)
    | h::t -> if ( f h ) then aux f (h::l1) l2 t
              else aux f l1 (h::l2) t
  in aux f [] [] l;;

let filter f l1 =
    let rec aux f l2 = function
      [] -> (rev l2)
    | h::t -> if ( f h ) then aux f (h::l2) t
            else aux f l2 t
  in aux f [] l1;;

let find_all f l1 =
    let rec aux f l2 = function
      [] -> (rev l2)
    | h::t -> if ( f h ) then aux f (h::l2) t
            else aux f l2 t
  in aux f [] l1;;