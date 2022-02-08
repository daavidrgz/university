/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 2
 * AUTHOR 1: David Rodríguez Bacelar LOGIN 1: david.rbacelar
 * AUTHOR 2: Inma López Vázquez      LOGIN 2: i.lopezv
 * GROUP: 2.4
 * DATE: 07 / 05 / 2020
 */

#ifndef PARTY_LIST_H
#define PARTY_LIST_H

#include "types.h"
#include <stdbool.h>
#include <stdlib.h>

#define LNULL NULL

typedef struct tNode* tPosL;
struct tNode {
    tItemL data;
    tPosL next;
};
typedef tPosL tList;

void createEmptyList (tList*);
/* createEmptyList (tList) → tList
{	Objetivo: Crea una lista vacía.
    Salida: Lista creada.
    PostCD: La lista queda inicializada y no contiene elementos.}*/

bool insertItem(tItemL, tList*);
/*insertItemL(tItemL, tList*) → tList, Boolean
{	Objetivo: Inserta un elemento de forma ordenada según el campo "partyname".
    Entrada: El elemento a insertar y la lista donde insertarlo.
	Salida: Lista con el item insertado; devuelve true si el elemento pudo ser insertado, false en caso contrario.
	PreCD: La lista está inicializada.
    PostCD: Las posiciones de los elementos de la lista posteriores al insertado pueden cambiar de valor.}*/

void updateVotes(tNumVotes, tPosL, tList*);
/*updateVotes (tNumVotes, tPosL, tList) → tList
{	Objetivo: Modifica el número de votos del elemento situado en la posición indicada.
    Entrada: Número de votos a sumar, la posición del elemento y la lista que lo contiene.
    Salida: Lista modificada.
    PreCD: La posición indicada es una posición válida en la lista.
    PostCD: El orden de los elementos de la lista no se ve modificado.}*/

void deleteAtPosition(tPosL, tList*);
/*deleteAtPosition (tPosL, tList) → tList
{	Objetivo: Elimina de la lista el elemento que ocupa la posición indicada.
	Entrada: Posición del elemento que se desea eliminar y la lista que lo contiene.
	Salida: Lista modificada.
	PreCD: La posición indicada es una posición válida en la lista.
	PostCD: Tanto la posición del elemento eliminado como aquellas de los elementos de la lista a continuación del mismo pueden cambiar de valor.}*/

tPosL findItem(tPartyName, tList);
/*findItem (tPartyName, tList) → tPosL
{	Objetivo: Devuelve la posición del primer elemento de la lista cuyo nombre de partido se corresponda con el indicado (o LNULL si no existe tal elemento).
    Entrada: Nombre de partido del elemento y lista donde está contenido.
    Salida: Posición de dicho elemento o LNULL si no existe.}*/

bool isEmptyList(tList);
/*isEmptyList (tList) → bool
{	Objetivo: Determina si la lista está vacía.
	Entrada: Lista que se va a comprobar.
	Salida: Un booleano que devuelva true si la lista está vacía o false en caso contrario.}*/

tItemL getItem(tPosL, tList);
/*getItem (tPosL, tList) → tItemL
{	Objetivo: Devuelve el contenido del elemento de la lista que ocupa la posición indicada.
	Entrada: Posición del elemento que se desea obtener y lista que lo contiene.
	Salida: Elemento obtenido.
	PreCD: La posición indicada es una posición válida en la lista.}*/

tPosL first(tList);
/*first (tList) → tPosL
{	Objetivo: Devuelve la posición del primer elemento de la lista.
	Entrada: Lista donde se encuentra dicho elemento.
	Salida: Posición del primer elemento.
	PreCD: La lista no está vacía.}*/

tPosL last(tList);
/*last (tList) → tPosL
{	Objetivo: Devuelve la posición del último elemento de la lista.
	Entrada: Lista donde se encuentra dicho elemento.
	Salida: Posición del último elemento.
	PreCD: La lista no está vacía.}*/

tPosL previous(tPosL, tList);
/*previous (tPosL, tList) → tPosL
{	Objetivo: Devuelve la posición en la lista del elemento anterior al de la posición indicada.
	Entrada: Posición del elemento indicado y lista donde se encuentra.
	Salida: Posición del elemento anterior al indicado o LNULL si no tiene.
	PreCD: La posición indicada es una posición válida en la lista. }*/

tPosL next(tPosL, tList);
/*next (tPosL, tList) → tPosL
{	Objetivo: Devuelve la posición en la lista del elemento siguiente al de la posición indicada.
	Entrada: Posición del elemento indicado y lista donde se encuentra.
	Salida: Posición del elemento siguiente al indicado o LNULL si no tiene.
	PreCD: La posición indicada es una posición válida en la lista.}*/


#endif
bool Similar(tBinTree T, tBinTree C){
    if(isEmptyTree(T) && isEmptyTree(C)){
        return true;
    }
    if(!isEmptyTree(T) && !isEmptyTree(C)){
        return (Similar(leftChild(T), leftChild(C)) && Similar(rightChild(T), rightChild(C)));
    } else
        return false;
}

