digit(1..9).
row(0..8).
col(0..8).

1{cell(X,Y,D) : digit(D)}1 :- col(X), row(Y).

:- col(X), row(Y), digit(D), cell(X,Y,D), cell(X,Z,D), Y != Z.
:- col(X), row(Y), digit(D), cell(X,Y,D), cell(Z,Y,D), X != Z.
:- col(X), row(Y), digit(D), cell(X,Y,D), cell(Z,T,D), X/3 == Z/3, Y/3 == T/3, (X,Y) != (Z,T).

#show cell/3.
