1 {d1; d2; d3} 1.
0 {p1; p2; p3} 1.

% Letrero 1 p1 <-> not d1
:- p1, d1.
:- not d1, not p1.

% Letrero 2 p2 <-> d2
:- p2, not d2.
:- d2, not p2.

% Letrero 3 p3 <-> not d2
:- p3, d3.
:- not d2, not p3.
