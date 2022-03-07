#program initial
1{h(sw(X),up); h(sw(X),down)}1 :- switch(X).
1{h(light,on); h(light,off)}1.

:- not &tel{
	% h(light,on) & h(sw(3), up)
	;> h(light,off) & h(sw(1),down) & h(sw(3),up)
	;> h(light,on) & h(sw(1), down) & h(sw(2), up) & h(sw(3), down)
}.
