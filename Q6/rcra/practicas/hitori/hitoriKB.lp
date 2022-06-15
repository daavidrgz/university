digit(1..9).

{black(X,Y) : row(X), col(Y)}.

:- cell(X,Y,D), cell(X,Z,D), not black(X,Y), not black(X,Z), Y!=Z.
:- cell(X,Y,D), cell(Z,Y,D), not black(X,Y), not black(Z,Y), X!=Z.

:- black(X,Y), black(X+1,Y).
:- black(X,Y), black(X,Y+1).
:- black(X,Y), black(X-1,Y).
:- black(X,Y), black(X,Y-1).

:- not black(X,Y), black(X+1,Y), black(X-1,Y), black(X,Y+1), black(X,Y-1).

% % Esquinas
% :- not black(0,0), black(1,0), black(0,1).
% :- not black(7,7), black(6,7), black(7,6).
% :- not black(0,7), black(0,6), black(1,7).
% :- not black(7,0), black(6,0), black(7,1).

% % Lados
% :- not black(0,Y), black(0,Y-1), black(0,Y+1), black(1,Y), Y>0, Y<7.
% :- not black(7,Y), black(7,Y-1), black(7,Y+1), black(6,Y), Y>0, Y<7.
% :- not black(X,0), black(X+1,0), black(X-1,0), black(X,1), X>0, X<7.
% :- not black(X,7), black(X+1,7), black(X-1,7), black(X,6), X>0, X<7.

% % Resto
% :- not black(X,Y), black(X+1,Y), black(X-1,Y), black(X,Y+1), black(X,Y-1), X>0, X<7, Y>0, Y<7.

#show black/2.
#show cell/3.
