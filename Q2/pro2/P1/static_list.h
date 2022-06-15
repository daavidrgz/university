/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 1
 * AUTHOR 1: ***************************** LOGIN 1: **********
 * AUTHOR 2: ***************************** LOGIN 2: **********
 * GROUP: *.*
 * DATE: ** / ** / **
 */

#ifndef STATIC_LIST_H
#define STATIC_LIST_H

#include "types.h"
#include <stdbool.h>
#include <string.h>
#define LNULL -1
#define MAX 25

typedef int tPosL;

typedef struct {
    tItemL data[MAX];
    tPosL lastPos;
}tList;

void createEmptyList (tList*);
bool insertItem(tItemL, tPosL, tList*);
bool copyList(tList, tList*);
void updateVotes(tNumVotes, tPosL, tList*);
void deleteAtPosition(tPosL, tList*);
void deleteList(tList*);
tPosL findItem(tPartyName, tList);
bool isEmptyList(tList);
tItemL getItem(tPosL, tList);
tPosL first(tList);
tPosL last(tList);
tPosL previous(tPosL, tList);
tPosL next(tPosL, tList);

#endif
