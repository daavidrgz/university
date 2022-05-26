#program initial.
block(a,hl,2).
block(b,vl,3).
block(c,vl,3).
block(d,hl,2).
block(e,vl,3).
block(f,vl,2).
block(g,hl,2).
block(h,hl,3).

col(0..6).
row(0..6).

direction(hl;vl).
cell(X,Y) :- row(X), col(Y).
{ action(move(I,M)) : M=-6..6, M!=0 } :- block(I,_,_).

% Initial state
h(block(a,hl,2), cell(0,0)).
h(block(b,vl,3), cell(0,5)).
h(block(c,vl,3), cell(1,0)).
h(block(d,hl,2), cell(2,1)).
h(block(e,vl,3), cell(1,3)).
h(block(f,vl,2), cell(4,0)).
h(block(g,hl,2), cell(4,4)).
h(block(h,hl,3), cell(5,2)).

#program dynamic.
1 {o(X) : _action(X)} 1.
moved(B,A) :- o(move(B,A)).

% Executability
% owns(B, cell(X,Z)) :- 'h(block(B,hl,S), cell(X,Y)), h(block(B,hl,S), cell(X,Y')), cell(X,Z), Z>=Y, Z<Y'+S, Y'>=Y. 
% owns(B, cell(X,Z)) :- 'h(block(B,hl,S), cell(X,Y)), h(block(B,hl,S), cell(X,Y')), cell(X,Z), Z>=Y', Z<Y+S, Y'<=Y. 

% owns(B, cell(Z,Y)) :- 'h(block(B,vl,S), cell(X,Y)), h(block(B,vl,S), cell(X',Y)), cell(Z,Y), Z>=X, Z<X'+S, X'>=X.
% owns(B, cell(Z,Y)) :- 'h(block(B,vl,S), cell(X,Y)), h(block(B,vl,S), cell(X',Y)), cell(Z,Y), Z>=X', Z<X+S, X'<=X.

% :- moved(B,Z), owns(B,C), owns(D,C), B != D.

:- moved(B,A), 'h(block(B,hl,S), cell(X,Y)), 'h(block(D,vl,S'), cell(X',Y')), X >= X', X <= X'+S'-1, Y+S+A > Y', Y<Y', A>0.
:- moved(B,A), 'h(block(B,hl,S), cell(X,Y)), 'h(block(D,vl,S'), cell(X',Y')), X >= X', X <= X'+S'-1, Y+A <= Y', Y>Y', A<0.

:- moved(B,A), 'h(block(B,vl,S), cell(X,Y)), 'h(block(D,hl,S'), cell(X',Y')), Y >= Y', Y <= Y'+S'-1, X+S+A > X', X<X', A>0.
:- moved(B,A), 'h(block(B,vl,S), cell(X,Y)), 'h(block(D,hl,S'), cell(X',Y')), Y >= Y', Y <= Y'+S'-1, X+A <= X', X>X', A<0.

:- moved(B,A), 'h(block(B,hl,S), cell(X,Y)), 'h(block(D,hl,S'), cell(X,Y')), Y+S+A > Y', Y<Y', A>0, B != D.
:- moved(B,A), 'h(block(B,hl,S), cell(X,Y)), 'h(block(D,hl,S'), cell(X,Y')), Y+A < Y'+S', Y>Y', A<0, B != D.

:- moved(B,A), 'h(block(B,vl,S), cell(X,Y)), 'h(block(D,vl,S'), cell(X',Y)), X+S+A > X', X<X', A>0, B != D.
:- moved(B,A), 'h(block(B,vl,S), cell(X,Y)), 'h(block(D,vl,S'), cell(X',Y)), X+A < X'+S', X>X', A<0, B != D.

h(block(B,hl,S), cell(X,Y+Z)) :- 'h(block(B,hl,S), cell(X,Y)), moved(B,Z), Y+Z+S <= 6, Y+Z >= 0.
h(block(B,vl,S), cell(X+Z,Y)) :- 'h(block(B,vl,S), cell(X,Y)), moved(B,Z), X+Z+S <= 6, X+Z >= 0.

% Inertia
h(B, Y) :- 'h(B, Y), not c(B).
c(F) :- 'h(F, X), h(F, Y), X != Y.

#program final.
goal :- h(block(d,_,_), cell(_,4)).
:- not goal.

#show moved/2.
