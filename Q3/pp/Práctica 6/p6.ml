let suml =
    let rec aux n = function
      [] -> n
    | h::t -> aux (n+h) t
  in function l -> aux 0 l;; 

let maxl = 
    let rec aux maxnum = function
      [] -> maxnum
    | h::t -> aux (max h maxnum) t
  in function
    [] -> raise (Failure "maxl")
  | h::t -> aux h t;;

let to0from = 
    let rec aux n l =
      if n < 0 then l
      else aux (n-1) (n::l)
  in function n -> aux n [];;

let fromto =
    let rec aux m n l =
      if m > n then l
      else aux m (n-1) (n::l)
  in function m -> (function n -> aux m n []);;

let from1to =
    let rec aux n l =
      if n < 1 then l
      else aux (n-1) (n::l)
  in function n -> aux n [];;

let append l1 l2 = List.rev_append (List.rev l1) l2;;

let map = 
    let rec aux f l = function
      [] -> l
    | h::t -> aux f ((f h)::l) t
  in function f -> (function l -> aux f [] (List.rev l));;

let power x y =
    let rec innerpower y total =
      if y = 0 then total
      else innerpower (y-1) (x*total)
  in 
    if y >= 0 then innerpower y 1
    else invalid_arg "power";;

let incseg =
    let rec aux i l = function
      [] -> List.rev l
    | h::t -> aux (i+h) ((i+h)::l) t
  in function l -> aux 0 [] l;;

let remove i l =
    let rec aux i l2 = function
      [] -> List.rev l2
    | h::t -> if h = i then List.rev_append l2 t
              else aux i (h::l2) t
  in aux i [] l;;

let insert x l =
    let rec aux x l = function
      [] -> List.rev (x::l) 
    | h::t -> if x <= h then List.rev_append (h::x::l) t
              else aux x (h::l) t
  in aux x [] l;;

let rec insert_gen f x l =
    let rec aux f x l = function
      [] -> List.rev (x::l) 
    | h::t -> if f x h then List.rev_append (h::x::l) t
              else aux f x (h::l) t
  in aux f x [] l;;
