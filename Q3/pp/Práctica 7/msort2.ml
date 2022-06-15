let rec divide l = match l with
    h1::h2::t -> let t1, t2 = divide t in (h1::t1, h2::t2)
  | _ -> l, [];;

let rec merge f  = function
    [], l | l, [] -> l
  | h1::t1, h2::t2 -> if f h1 h2 then h1 :: merge f (t1, h2::t2)
                      else h2 :: merge f (h1::t1, t2);;

let rec msort1 f l = match l with
    [] | _::[] -> l
  | _ -> let l1, l2 = divide l in
        merge f (msort1 f l1, msort1 f l2);;

(*Si que puede causar problemas con listas grandes*)
let l2 = List.init 200000 abs;;

let divide' l = (*Tail-recursive*)
    let rec aux l1 l2 = function
      h1::h2::t -> aux (h1::l1) (h2::l2) t
    | h::t -> (List.rev (h::l1)), (List.rev l2)
    | _ -> (List.rev l1), (List.rev l2)
  in aux [] [] l;;

let merge' f g =
    let rec aux f l g = match g with
      [], s | s, [] -> List.rev_append l s
    | h1::t1, h2::t2 -> if f h1 h2 then aux f (h1::l) (t1, h2::t2)
                        else aux f (h2::l) (h1::t1, t2)
  in aux f [] g;;

let rec msort2 f l = match l with
    [] | _::[] -> l
  | _ -> let l1, l2 = divide' l in
        merge' f (msort2 f l1, msort2 f l2);;

(*De nuevo, implementamos estas dos funciones para la medición de tiempos*)
let crono f l =
  let t = Sys.time () in
  let _ = f l in
  Sys.time () -. t;;

let time f n =
  let rec aux f n t = function
      0 -> t /. (float n)
    | i -> let l = List.init 50000 (function x -> Random.int 50000) in
          aux f n (t +. crono f l) (i-1) in
  aux f n 0. n;;

let rec qsort2 ord =
  let append' l1 l2 = List.rev_append (List.rev l1) l2 in
  function
    [] -> []
  | h::t -> let after, before = List.partition (ord h) t in
            append' (qsort2 ord before) (h :: qsort2 ord after);;

let t1 = time (msort1 (<)) 100;;
(*val t1 : float = 0.0710143300000002919*)
let t2 = time (msort2 (<)) 100;;
(*val t2 : float = 0.0829872399999997817*)
let t3 = time (qsort2 (<)) 100;;
(*val t3 : float = 0.0803361100000003631*)

let a = t2 /. t1;;
(*val a : float = 1.1685985068084066*)
let b = t2 /. t3;;
(*val b : float = 1.0330004776183388*)

(*msort2 es un 16.86% más lento que msort1 y un 3.3% más lento que qsort2*)

