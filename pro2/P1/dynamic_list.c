#include "dynamic_list.h"

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
    p->data.numVotes = d ;
}

tPosL findItem(tPartyName d, tList L){
    tPosL p;
    for(p = L; (p != LNULL) && (strcmp(p->data.partyName, d)!=0); p = p->next);
    return p;
}

bool createNode(tPosL* p){
    *p = malloc(sizeof(**p));
    return *p != LNULL;
}

bool insertItem(tItemL d, tPosL p, tList *L){
    tPosL q, r;
    if(!createNode(&q)) {
        return false;
    }else {
        q->data = d;
        q->next = LNULL;
        if(*L == LNULL) {
            *L = q;
        } else {
            if(p == LNULL) {
                for(r = *L; r->next != LNULL; r = r->next);
                r->next = q;
            }else {
                if (p == *L) {
                    q->next = *L;
                    *L = q;
                }else {
                    q->data = p->data;
                    p->data = d;
                    q->next = p->next;
                    p->next = q;
                }
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

void deleteList( tList *L ) {
    tPosL p = *L;
    while(*L != LNULL) {
        *L = p->next;
        free(p);
        p = *L;
    }
}
