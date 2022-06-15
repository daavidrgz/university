vtx(1..4).
edge(1,2). edge(2,3). edge(2,4).
edge(3,1). edge(3,4). edge(4,3). edge(4,1).

{in(X,Y)} :- edge(X,Y).

:- in(X,Y), in(X,Y'), Y!=Y'.
:- in(X,Y), in(X',Y), X!=X'.

:- vtx(X), vtx(Y), not connect(X,Y).

connect(X,Y) :- in(X,Y).
connect(X,Z) :- connect(X,Y), in(Y,Z).

#show in/2.
