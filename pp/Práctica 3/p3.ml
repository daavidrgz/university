let rec gcd x y =
    if y = 0 then x
    else gcd y (x mod y);;

let is_prm n =
    let rec not_divisible_from d =
        d * d > n || (n mod d <> 0 && not_divisible_from (d+1)) in
    n > 1 && not_divisible_from 2;;

(*Empezando en 2, el algoritmo recursivo primero comprueba si el número n sería divisible por el cuadrado de d
(basándose dicha proposición en la criba de Eratóstenes). Si el resultado de la comparación es falso, pasará a comprobar
si la segunda parte es verdadera o no. Para ello evaluará primero si es realmente cierto que el número n no es divisible por d y, si
es cierto que no lo divide, pasa a hacer las comprobaciones con el siguiente número.*)

let is_prm2 n =
    let rec not_divisible_from d =
    (n mod d <> 0 && not_divisible_from (d+1)) || d * d > n in
    n > 1 && not_divisible_from 2;;

(*En este caso, el algortimo comprueba si n es divisible por d hasta que la condición se vuelva falsa para luego pasar a comprobar si ese número
d al cuadrado es mayor que n. Es decir, el primer algoritmo para mucho antes de hacer la comprobaciones y, en cambio, este otro en el caso de que fuera primo,
comprobaría todos los números hasata llegar a él.*)

let capicua n =
    let rec invert_num num x y =
    if y = 0 then num
    else invert_num (num * 10 + x) ((y / 10) mod 10) (y / 10) in
    n = invert_num (0) (n mod 10) (n);;