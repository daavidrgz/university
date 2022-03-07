house(1..5)
.
% Reíficiar
type(pet, (dog;fish;bird;horse;cat)).
type(person, (dane;swede;brit;german;norw)).
type(color, (blue;red;white;green;yellow)).
type(tobaco, (pall;dunhill;blends;bluemast;prince)).
type(drink, (tea;water;milk;beer;coffee)).

type(T) :- type(T,V).

1{at(T,V,X): type(T,V)}1 :- house(X), type(T).
:- at(T,V,X), at(T,V,X'), X!=X'.

:- at(person,brit,X), not at(color,red,X).
:- at(person,swede,X), not at(pet,dog,X).
:- at(person,dane,X), not at(drink,tea,X).
:- at(color,white,X), not at(color,green,X-1).
:- at(color,green,X), not at(drink,coffee,X).
:- at(tobaco,pall,X), not at(pet,bird,X).
:- at(color,yellow,X), not at(tobaco,dunhill,X).
% :- not at(drink,milk,3).
at(drink,milk,3).
% :- not at(person,norw,1).
at(person,norw,1).
:- at(tobaco,blends,X), not at(pet,cat,X-1), not at(pet,cat,X+1).
:- at(pet,horse,X), not at(tobaco,dunhill,X-1), not at(tobaco,dunhill,X+1).
:- at(tobaco,bluemast,X), not at(drink,beer,X).
:- at(person,german,X), not at(tobaco,prince,X).
:- at(person,norw,X), not at(color,blue,X-1), not at(color,blue,X+1).
:- at(tobaco,blends,X), not at(drink,water,X-1), not at(drink,water,X+1).

#show at/3.
