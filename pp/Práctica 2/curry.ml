let curry f = function x -> (function y -> f (x,y));;
let uncurry g = function (x,y) -> g x y;;

uncurry (+);;
(*- : int * int -> int = <fun>*)

let sum = (uncurry (+));;
(*val sum : int * int -> int = <fun>*)

(*sum 1;;*)
(*Type Error: Argument type int but expected int * int*)

sum (2,1);;
(*- : int = 3*)

let g = curry (function p -> 2 * fst p + 3 * snd p);;
(*val g : int -> int -> int = <fun>*)

(*g (2,5);;*)
(*Type Error: Argument type 'a' * 'b' but expected int*)

let h = g 2;;
(*val h : int -> int = <fun>*)

h 1, h 2, h 3;;
(*- : int * int * int = (7, 10, 13)*)
