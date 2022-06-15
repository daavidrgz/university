open St_tree;;

let t = comp 3 (single 8,
                comp 2 (single 5,
                        single 1));;

let is_single t =
    try let _ = branches t in false
    with No_branches -> true;;

let breadth_first t = 
    let rec aux queue = match queue with
        [] -> []
      | h::t -> let r = root h in
                if is_single h then 
                  r :: aux t
                else 
                  let (h1, h2) = branches h in
                  r :: (aux (t@[h1;h2]))
  in aux [t];;