let e1 = 
    let pi = 2. *. asin 1. in pi *. (pi +. 1.);;
(*val e1 : float = 13.0111970546791511*)
    
let e1 = (function pi -> pi *. (pi +. 1.)) (2. *. asin 1.);;
(*val e1 : float = 13.0111970546791511*)

(*========================================*)

let e2 = 
    let lg2 = log 2. in
    let log2 = function x -> log x /. lg2 in 
    log2 (float (1024 * 1024));;
(*val e2 : float = 20.*)
        
let e2 = (function log2 -> (function x -> log2 x)) 
        ((function lg2 -> (function x -> log x /. lg2)) (log 2.)) (float (1024 * 1024));;
(*val e2 : float = 20.*)

(*========================================*)

let e3 =
    let pi_2 = 4. *. asin 1. in
    function r -> pi_2 *. r;;
(*val e3 : float -> float = <fun>*)
    
let e3 = (function pi_2 -> function r -> pi_2 *. r) (4. *. asin 1.);;
(*val e3 : float -> float = <fun>*)

(*========================================*)

let e4 =
    let sqr = function x -> x *. x in
    let pi = 2. *. asin 1. in
    function r -> pi *. sqr r;;
(*val e4 : float -> float = <fun>*)

let e4 = (function sqr -> (function pi -> (function x -> pi *. sqr x))) (function y -> y *. y) (2. *. asin 1.);;
(*val e4 : float -> float = <fun>*)
