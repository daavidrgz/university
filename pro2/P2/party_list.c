/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 2
 * AUTHOR 1: ***************************** LOGIN 1: **********
 * AUTHOR 2: ***************************** LOGIN 2: **********
 * GROUP: *.*
 * DATE: ** / ** / **
 */

#include "party_list.h"
#include <string.h>
void createEmptyList(tList *L){
    *L = LNULL;
}

bool isEmptyList(tList L){
    return L == LNULL;
}

tPosL first(tList L){
    return L;
}

tPosL next(tPosL p, tList L){
    return p->next;
}

tPosL last(tList L){
    tPosL p = L;
    while(p->next != LNULL) {
        p = next(p, L);
    }
    return p;
}

tPosL previous(tPosL p , tList L ){
    tPosL q;
    if(p == L) {
        return LNULL;
    }else {
        q = L;
        while(q->next != p) {
            q = next(q, L);
        }
        return q;
    }
}

tItemL getItem(tPosL p, tList L){
    return p->data;
}

void updateVotes(tNumVotes d, tPosL p, tList *L){
    p->data.numVotes = p->data.numVotes + d ;
}

tPosL findItem(tPartyName d, tList L){
    tPosL p;
    for(p = L; (p != LNULL) && (strcmp(d, p->data.partyName)>0); p = p->next);
    if((p!=LNULL) && (strcmp(d, p->data.partyName) == 0)){
        return p;
    } else{
        return LNULL;
    }

}

bool createNode(tPosL* p){
    *p = malloc(sizeof(**p));
    return *p != LNULL;
}

void convert_min(char* partyName){ //Funcion utilizada para convertir a strings con todas las letras en minuscula, para asi poder ordenar alfabeticamente sin tener en cuenta las mayusculas y minusculas en el strcmp()
    int i=0;
    while(partyName[i] != '\0'){
        if(partyName[i] >= 65 && partyName[i] <= 90){
            partyName[i] = partyName[i] + 32;
        }
        i++;
    }
}

bool insertItem(tItemL d, tList *L){
    tPosL q, r;
    char straux1[NAME_LENGTH_LIMIT], straux2[NAME_LENGTH_LIMIT];
    if(!createNode(&q)) {
        return false;
    }else {
        q->data = d;
        q->next = LNULL;
        if(isEmptyList(*L)) {
            *L = q;
        } else{
            strcpy(straux1, d.partyName);
            convert_min(straux1);
            strcpy(straux2, (*L)->data.partyName);
            convert_min(straux2);

            if(strcmp(straux1, straux2)<0){ //Comprobacion inicial para actualizar o no la dirección de la lista
                q->next = *L;
                *L = q;
            }
            else{
                r = *L;
                if(r->next != LNULL){ //Comprobamos si existe un segundo elemento en la lista para poder empezar las comprobaciones del bucle de manera eficiente
                    strcpy(straux2, r->next->data.partyName);
                    convert_min(straux2);
                    while((r->next!=LNULL) && (strcmp(straux1, straux2)>0)){
                        r = r->next;
                        if(r->next != LNULL) //Si no existe un siguiente, no seria posible conseguir su nombre de partido
                        {
                            strcpy(straux2, r->next->data.partyName);
                            convert_min(straux2);
                        }
                    }
                }
                q->next = r->next;
                r->next = q;
            }
        }
        return true;
    }
}

void deleteAtPosition(tPosL p, tList *L){
    tPosL q;
    if(p == *L) {
        *L = (*L)->next;
    }else {
        if(p->next == LNULL ) {
            for(q = *L ; q->next != p; q = q->next);
            q->next = LNULL;
        }else{
            q = p->next;
            p->data = q->data;
            p->next = q->next;
            p = q;
        }
    }
    free(p);
}