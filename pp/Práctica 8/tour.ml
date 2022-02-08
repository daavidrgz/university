let isvalid m n path = function (s,k) -> s >= 1 && k >= 1 && s <= n && k <= m && not (List.mem (s,k) path);;    

let rec isfinish current jump last = 
    if current = last then true
    else if jump = (0,0) then false
    else if jump = last then true
    else isfinish current (nextpos current jump) last;;


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

let tour m n first last =
    let rec complete path current jump =
      if isfinish current (nextpos current (0,0)) last then List.rev (last::path)
      else if jump = (0,0) then raise Not_found
      else if isvalid m n path jump then 
          try complete (jump::path) jump (nextpos jump (0,0)) with
              Not_found -> complete path current (nextpos current jump)
      else complete path current (nextpos current jump)

  in if (isvalid m n [] first && isvalid m n [] last) then complete [first] first (nextpos first (0,0))
    else raise (Failure "Invalid_argument");;
      

