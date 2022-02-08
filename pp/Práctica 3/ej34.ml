let goldbach =
	let is_prm n =
        let rec not_divisible_from d =
            d * d > n || (n mod d <> 0 && not_divisible_from (d+1)) in
        n > 1 && not_divisible_from 2 in
        
	let rec getprim z =
    	if is_prm z then z
    	else getprim (z + 1) in
      
	let rec is_sum num x =
    	if (num - x) < 0 || is_prm (num - x) then
        	if (num - x) < 0 then raise (Failure "Invalid_argument")
        	else x, (num - x)
    	else is_sum num (getprim (x + 1)) in
      
	function num -> is_sum num 2;;
