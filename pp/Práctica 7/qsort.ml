let rec qsort1 ord = function
    [] -> []
  | h::t -> let after, before = List.partition (ord h) t 
            in qsort1 ord before @ h :: qsort1 ord after;;

(*El rendimiento de esta implementación no será bueno para vectores inicialmente ordenados
de forma ascendente o descendente, ya que siempre escogemos como pivote el primer elemento, resultando
en que al realizar el List.partition, quedarán todos los elementos menos uno en una de las dos listas*)

let rec qsort2 ord =
  let append' l1 l2 = List.rev_append (List.rev l1) l2 in
  function
    [] -> []
  | h::t -> let after, before = List.partition (ord h) t in
            append' (qsort2 ord before) (h :: qsort2 ord after);;

(*La ventaja de qsort2 frente a qsort1 es la recursividad terminal del append que, aunque la función qsort2 en si
no sea terminal (el número de llamadas recursivas pendientes sigue siendo, como máximo, log2(n)), permite
ordenar listas que dejen un gran número de elementos en la primera parte del vector: *)
let l1 = List.init 400000 (function x -> Random.int 400000);;

(*Para hacer la comprobación de tiempos se utilizaron las siguientes funciones*)
let crono f l =
  let t = Sys.time () in
  let _ = f l in
  Sys.time () -. t;;

let time f n =
  let rec aux f n t = function
      0 -> t /. (float n)
    | i -> let l = List.init 100000 (function x -> Random.int 100000) in
          aux f n (t +. crono f l) (i-1) in
  aux f n 0. n;;

let t1 = time (qsort1 (<)) 100;; (*Media de tiempos para 100 ordenaciones*)
(*val t1 : float = 0.263992849999999946*)
let t2 = time (qsort2 (<)) 100;; (*Media de tiempos para 100 ordenaciones*)
(*val t2 : float = 0.297138799999999315*)

let a = t2 /. t1;;
(*val a : float = 1.12555624139062616*)

(*qsort2 es un 12.55% más lento que qsort1. Esto se debe a que la función append' aplica la función List.rev para poder utilizar luego
la función List.rev_append sin desordenar el vector, lastrando el rendimiento.*)