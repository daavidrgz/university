/*
* TITLE: PROGRAMMING II LABS
        * SUBTITLE: Practical 2
* AUTHOR 1: David Rodríguez Bacelar LOGIN 1: david.rbacelar
        * AUTHOR 2: Inma López Vázquez      LOGIN 2: i.lopezv
        * GROUP: 2.4
* DATE: 07 / 05 / 2020
*/

#ifndef CENTER_LIST_H
#define CENTER_LIST_H

#include "types.h"
#include <stdbool.h>
#include "party_list.h"

#define NULLC -1
#define MAX 10

typedef int tPosC;

typedef struct{
    tCenterName centerName;
    tNumVotes totalVoters;
    tNumVotes validVotes;
    tNumVotes nullVotes;
    tList partyList;
}tItemC;

typedef struct{
    tItemC data[MAX];
    tPosC lastPos;
}tListC;

void createEmptyListC(tListC*);
/* createEmptyList (tListC*) → tListC
{	Objetivo: Crea una lista vacía.
    Salida: Lista creada.
    PostCD: La lista queda inicializada y no contiene elementos.}*/

bool insertItemC(tItemC, tListC*);
/*insertItemC(tItemC, tListC*) → tListC, Boolean
{	Objetivo: Inserta un elemento de forma ordenada en función del campo "centername".
    Entrada: El elemento a insertar y la lista donde insertarlo.
	Salida: Lista con el item insertado; devuelve true si el elemento pudo ser insertado, false en caso contrario.
    PreCD: La lista está inicializada.
    PostCD:Las posiciones de los elementos de la lista posteriores al insertado pueden cambiar de valor. }*/

void updateListC(tList, tPosC, tListC*);
/*updateListC(tList,tPosC, tListC*) → tListC
{	Objetivo: Modifica la lista de partidos del elemento situado en la posición indicada.
    Entrada: Lista de partidos a sobreescribir, posición del centro y lista de centros
	Salida: Lista de centros con la lista de partidos actualizada.
	PreCD: La posición indicada es una posición válida en la lista.}*/

void updateValidVotesC(tNumVotes, tPosC, tListC*);
/*updateValidVotesC(tNumVotes, tPosC, tListC*) → tListC
{	Objetivo: Modifica el número de votos válidos del elemento situado en la posición indicada.
    Entrada: Número de votos a añadir a los votos válidos totales del centro, posición del elemento a modificar y la lista que lo contiene.
	Salida: Lista con el número de votos válidos de un centro actualizado.
	PreCD: La posición indicada es una posición válida en la lista.}*/

void updateNullVotesC(tNumVotes, tPosC, tListC*);
/*updateNullVotesC(tNumVotes, tPosC, tListC*) → tListC
{	Objetivo: Modifica el número de votos nulos del elemento situado en la posición indicada.
    Entrada:  Número de votos a añadir a los votos nulos totales del centro, posición del elemento a modificar y la lista que lo contiene.
	Salida: Lista con el número de votos nulos de un centro actualizado.
	PreCD: La posición indicada es una posición válida en la lista .}*/

void deleteAtPositionC(tPosC, tListC*);
/*deleteAtPositionC (tPosC, tListC) → tListC
{	Objetivo: Elimina de la lista el elemento que ocupa la posición indicada.
	Entrada: Posición del elemento que se desea eliminar y la lista que lo contiene.
	Salida: Lista modificada.
	PreCD: La posición indicada es una posición válida en la lista.
	PostCD: Tanto la posición del elemento eliminado como aquellas de los elementos de la lista a continuación del mismo pueden cambiar de valor.}*/

tPosC findItemC(tCenterName, tListC);
/*findItemC(tCenterName, tListC) → tPosC
{	Objetivo: Devuelve la posición del primer elemento de la lista cuyo nombre de centro se corresponda con el indicado.
    Entrada: Nombre del centro y lista donde está contenido.
	Salida: Posición del elemento o NULLC si no existe.}*/

bool isEmptyListC(tListC);
/*insertItemC(tItemC, tListC*) → tListC, Boolean
{	Objetivo: Determina si la lista está vacía.
	Entrada: Lista que se va a comprobar.
	Salida: Un booleano que devuelve true si la lista está vacía o false en caso contrario.}*/

tItemC getItemC(tPosC, tListC);
/*getItemC (tPosL, tList) → tItemC
{	Objetivo: Devuelve el contenido del elemento de la lista que ocupa la posición indicada.
	Entrada: Posición del elemento que se desea obtener y lista que lo contiene.
	Salida: Elemento obtenido.
	PreCD: La posición indicada es una posición válida en la lista.}*/

tPosC firstC(tListC);
/*firstC (tListC) → tPosC
{	Objetivo: Devuelve la posición del primer elemento de la lista.
	Entrada: Lista donde se encuentra dicho elemento.
	Salida: Posición del primer elemento.
	PreCD: La lista no está vacía.}*/

tPosC lastC(tListC);
/*lastC (tListC) → tPosC
{	Objetivo: Devuelve la posición del último elemento de la lista.
	Entrada: Lista donde se encuentra dicho elemento.
	Salida: Posición del último elemento.
	PreCD: La lista no está vacía.}*/

tPosC previousC(tPosC, tListC);
/*previousC (tPosC, tListC) → tPosC
{	Objetivo: Devuelve la posición en la lista del elemento anterior al de la posición indicada.
	Entrada: Posición del elemento indicado y lista donde se encuentra.
	Salida: Posición del elemento anterior al indicado o LNULL si es el primero.
	PreCD: La posición indicada es una posición válida en la lista. }*/

tPosC nextC(tPosC, tListC);
/*nextC (tPosC, tListC) → tPosC
{	Objetivo: Devuelve la posición en la lista del elemento siguiente al de la posición indicada.
	Entrada: Posición del elemento indicado y lista donde se encuentra.
	Salida: Posición del elemento siguiente al indicado o LNULL si es el último.
	PreCD: La posición indicada es una posición válida en la lista.}*/

#endif

