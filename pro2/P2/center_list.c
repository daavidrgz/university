/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 2
 * AUTHOR 1: ***************************** LOGIN 1: **********
 * AUTHOR 2: ***************************** LOGIN 2: **********
 * GROUP: *.*
 * DATE: ** / ** / **
 */

#include "types.h"
#include "party_list.h"
#include "center_list.h"
#include <string.h>

void createEmptyListC(tListC *L){
    L->lastPos = NULLC;
}

bool isEmptyListC(tListC L) {
    if (L.lastPos == NULLC) {
        return true;
    }else{
        return false;
    }
}

tPosC firstC(tListC L){
    return 0;
}

tPosC lastC(tListC L){
    return L.lastPos;
}

tPosC nextC(tPosC p,tListC L){
    if(p==L.lastPos)
        return NULLC;
    else
        return ++p;
}

tPosC previousC(tPosC p, tListC L){
    return --p;
}

void convert_minC(char* centerName){ //Funcion utilizada para convertir a strings con todas las letras en minuscula, para asi poder ordenar alfabeticamente sin tener en cuenta las mayusculas y minusculas en el strcmp()
    int i=0;
    while(centerName[i] != '\0'){
        if(centerName[i] >= 65 && centerName[i] <= 90){
            centerName[i] = centerName[i] + 32;
        }
        i++;
    }
}

bool insertItemC(tItemC d, tListC *L){
    tPosC p;
    char straux1[NAME_LENGTH_LIMIT], straux2[NAME_LENGTH_LIMIT];
     if(L->lastPos == MAX-1) {
        return false;
     }else {
        if (isEmptyListC(*L)){
            L->lastPos++;
            L->data[L->lastPos] =d;
        } else {
            L->lastPos++;
            strcpy(straux1, d.centerName);
            convert_minC(straux1);

            p = L->lastPos;
            strcpy(straux2, L->data[p - 1].centerName);
            convert_minC(straux2);

            while(strcmp(straux1, straux2) < 0 && p > 0) {
                L->data[p] = L->data[p - 1];
                if(p != 1){ //Condicion para evitar copiar el nombre del centro de una posicion no valida
                    strcpy(straux2, L->data[p - 2].centerName);
                    convert_minC(straux2);
                }
                p--;
            }
            L->data[p] = d;
        }
         return true;
     }
}

void deleteAtPositionC(tPosC p, tListC *L){
    L -> lastPos--;
    for(int i = p; i <= L->lastPos; i++)
        L -> data[i] = L -> data[i + 1];
}


tItemC getItemC(tPosC p, tListC L){
    return (L.data[p]);
}

void updateListC(tList d, tPosC p, tListC *L){
    L -> data[p].partyList = d;
}

void updateValidVotesC(tNumVotes d, tPosC p, tListC *L){
    L -> data[p].validVotes = L -> data[p].validVotes + d;
}

void updateNullVotesC(tNumVotes d, tPosC p, tListC *L){
    L -> data[p].nullVotes = L -> data[p].nullVotes + d;
}

tPosC findItemC(tCenterName d, tListC L) {
    tPosC p;
    if (isEmptyListC(L))
        return NULLC;
    else {
        for (p = 0; (p <= L.lastPos) && (strcmp(d, L.data[p].centerName)!=0); p++);
        if (p <= L.lastPos)
            return p;
        else
            return NULLC;
    }
}