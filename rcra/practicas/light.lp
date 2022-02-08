switch(a;b;c).
sit(0..2).

{up(X,Y) : switch(X), sit(Y)}.
{light(Y) : sit(Y)}.
{toggle(X,Y) : switch(X)}1 :- sit(Y), Y<2.

:- switch(X), sit(Y), toggle(X, Y), up(X,Y), up(X,Y+1).
:- switch(X), sit(Y), toggle(X, Y), not up(X,Y), not up(X,Y+1).

:- switch(X), sit(Y), toggle(X, Y), light(Y), light(Y+1).
:- switch(X), sit(Y), toggle(X, Y), not light(Y), not light(Y+1).

:- switch(X), switch(Z), sit(Y), toggle(X,Y), up(Z,Y), not up(Z,Y+1), Y<2, X!=Z.
:- switch(X), switch(Z), sit(Y), toggle(X,Y), not up(Z,Y), up(Z,Y+1), Y<2, X!=Z.

% :- switch(X),sit(Y),not toggle(X,Y),up(X,Y),not up(X,Y+1),Y<2.
% :- switch(X),sit(Y),not toggle(X,Y),not up(X,Y), up(X,Y+1),Y<2.

up(X,0) :- switch(X).
light(0).
toggle(a,0).
toggle(b,1).

#show up/2.
#show light/1.
