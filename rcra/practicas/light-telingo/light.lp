switch(1..3).
action(tog(X)) :- switch(X).

opp(on,off). opp(up,down).
opp(X,Y) :- opp(Y,X).

#program dynamic.
% Axiomas de efecto
% h(F,V), el fluente F tiene el valor V
% o(A), la acción A ha ocurrido
h(sw(X), V) :- o(tog(X)), 'h(sw(X),W), _opp(W,V).
h(light, V) :- o(tog(_)), 'h(light,W), _opp(W,V).

% Inercia
h(F,V) :- 'h(F,V), not change(F).
change(F) :- 'h(F,V), h(F,W), V!=W.
1{o(A) : _action(A)}1.

#program initial.
h(sw(1), up).
h(sw(2), down).
h(sw(3), up).
h(light, on).

&tel {
	&true
	;> o(tog(2))
	;> &true
	;> o(tog(2))
}.
