let isvalid m n path = function (s,k) -> s >= 1 && k >= 1 && s <= n && k <= m && not (List.mem (s,k) path);;    

let nextpos (x,y) (s,k) =
  if s = 0 && k = 0 then (x-2, y+1)
  else if s - x = -2 && k - y = 1 then (x-1, y+2)
  else if s - x = -1 && k - y = 2 then (x+1, y+2)
  else if s - x = 1 && k - y = 2 then (x+2, y+1)
  else if s - x = 2 && k - y = 1 then (x+2, y-1)
  else if s - x = 2 && k - y = -1 then (x+1, y-2)
  else if s - x = 1 && k - y = -2 then (x-1, y-2)
  else if s - x = -1 && k - y = -2 then (x-2, y-1)
  else (0,0);;

let shortest_tour m n first last =
    let rec complete max path current jump =
      if List.length path > max then []
      else if current = last then [List.rev path]
      else if jump = (0,0) then []
      else if isvalid m n path jump then
        let sol = complete max (jump::path) jump (nextpos jump (0,0)) in
        let max = List.fold_left min max (List.map List.length sol) in
        let sol2 = complete (max - 1) path current (nextpos current jump) in
        if sol2 = [] then sol
        else sol2
      else complete max path current (nextpos current jump)

  in if (isvalid m n [] first && isvalid m n [] last) 
    then let sol = complete (m * n) [first] first (nextpos first (0,0)) in
      if sol = [] then raise Not_found
      else List.hd sol
    else raise (Failure "Invalid_argument");;