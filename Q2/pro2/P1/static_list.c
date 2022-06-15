/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 1
 * AUTHOR 1: ***************************** LOGIN 1: **********
 * AUTHOR 2: ***************************** LOGIN 2: **********
 * GROUP: *.*
 * DATE: ** / ** / **
 */

#include "static_list.h"

void createEmptyList(tList *L){
    L->lastPos=LNULL;
}

bool isEmptyList(tList L) {
    if (L.lastPos == LNULL) {
        return true;
    }else{
        return false;
    }
}

tPosL first(tList L){
    return 0;
}

tPosL last(tList L){
    return L.lastPos;
}

tPosL next(tPosL p,tList L){
    if(p==L.lastPos)
        return LNULL;
    else
        return ++p;
}

tPosL previous(tPosL p, tList L){
    return --p;
}

bool insertItem(tItemL d, tPosL p, tList *L){
    tPosL i;
    if(L -> lastPos == MAX - 1){  //Si no hay hueco en el array
        return false;
    } else {
        L -> lastPos++;
        if(p == LNULL) {
            L->data[L->lastPos] = d; //Data es el array
        } else {
            for(i = L -> lastPos; i >= p + 1; i--)
                L -> data[i] = L -> data[i - 1];
            L -> data[p] = d;
        }
        return true;
    }
}

void deleteAtPosition(tPosL p, tList *L){ //(si se añade const delante de tList, indicamos que no se puede modificar desde dentro
    L -> lastPos--;
    for(int i = p; i <= L -> lastPos; i++)
        L -> data[i] = L -> data[i + 1];
}


tItemL getItem(tPosL p, tList L){
    return (L.data[p]);
}

void updateVotes(tNumVotes d, tPosL p, tList *L){
    L -> data[p].numVotes = d;
}


tPosL findItem(tPartyName d, tList L) {
    tPosL p;
    if (isEmptyList(L))
        return LNULL;
    else {
        for (p = 0; (p <= L.lastPos) && (strcmp(L.data[p].partyName, d) != 0); p++);
        if (p <= L.lastPos)
            return p;
        else
            return LNULL;
    }
}

bool copyList(tList L, tList* M){
    tPosL p;
    for(p=0;p<=L.lastPos;p++){
        M->data [p]=L.data[p];
    }
    M->lastPos=L.lastPos;
    return true;
}
