/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 2
 * AUTHOR 1: ***************************** LOGIN 1: **********
 * AUTHOR 2: ***************************** LOGIN 2: **********
 * GROUP: *.*
 * DATE: ** / ** / **
 */

#include <stdio.h>
#include "types.h"
#include "center_list.h"
#include <stdlib.h>
#include <string.h>

#define CODE_LENGTH 2


void Create(tCenterName centerName, int totalVoters, tListC* mainList)
{
    tItemC item;
    tList L;
    createEmptyList(&L);

    item.partyList = L;
    strcpy(item.centerName, centerName);
    item.nullVotes = 0;
    item.validVotes = 0;
    item.totalVoters = totalVoters;
    if(findItemC(centerName, *mainList) == NULLC){
        if((insertItemC(item, mainList))){
            printf("* Create: center %s totalvoters %d", centerName, totalVoters);
        }
        else{
            printf("+ Error: Create not possible");
        }
    }
    else{
        printf("+ Error: Create not possible");
    }

}

void New(tCenterName centerName, tPartyName partyName, tListC* mainList)
{
    tPosC p;
    tList L;
    tItemC center;
    p = findItemC(centerName, *mainList);

    if(p != NULLC){ //Si la posición es distinta a NULLC, existe el centro donde crear el partido deseado
        center = getItemC(p, *mainList);
        L = center.partyList;
        if(findItem(partyName, L) == LNULL) { //Se comrpueba si existe algún partido ya creado con el mismo nombre
            tItemL item; //Declaramos un tipo Item al que le asignamos el conjunto de valores (partyName y numVotes) necesarios
            strcpy(item.partyName, partyName);
            item.numVotes = 0;
            if(insertItem(item, &L)) { //Si el elemento no pudo ser insertado, la función insertItem() devuelve falso, por lo que se imprime un mensaje de error
                updateListC(L, p, mainList);
                printf("* New: center %s party %s", centerName, partyName);
            }
            else{
                printf("+ Error: New not possible");
            }
        }
        else{
            printf("+ Error: New not possible"); //Si existe un partido con el mismo nombre, la condición del if no se cumple ya que findItem() devolvería una posicion distinta a LNULL y se muestra así un mensaje de error
        }
    }
    else{
        printf("+ Error: New not possible");
    }

}

void Stats(tListC mainList)
{
    float partyVotes, participation; //Variable que representa el número de votos de un partido respecto al total de votos no nulos, por lo tanto, de tipo float
    tItemL item;
    tItemC center;
    tList* L;
    int totalVotes = 0;
    tPosC p;

    if(!isEmptyListC(mainList)){
        p = firstC(mainList);
        while(p != NULLC){ //Si la posicion excede la ultima posicion válida de la lista devuelve NULLC
            center = getItemC(p, mainList);
            printf("Center %s\n", center.centerName);
            L = &center.partyList;
            totalVotes = center.nullVotes + center.validVotes;
            tPosL position = first(*L); //Inicializamos la variable position a la posición inicial de la lista
            while(position != LNULL) //Si la posicion excede la ultima posicion válida de la lista devuelve LNULL
            {
                item = getItem(position, *L); //Cogemos el item de la posición actual y lo analizamos
                if(center.validVotes == 0) //Condición para evitar que el resultado de una división por 0 de como resultado un Not A Number (nan)
                {
                    partyVotes = .0f;
                }
                else
                {
                    partyVotes = item.numVotes/(float)(center.validVotes)*100;
                }
                printf("Party %s numvotes %d (%.2f%%)\n", item.partyName, item.numVotes, partyVotes);
                position = next(position, *L); //Establecemos la posición siguiente
            }

            printf("Null votes %d\n", center.nullVotes);
            if(center.totalVoters == 0) //De nuevo aplicamos la misma condición que, aunque en los casos de la práctica nunca se da, evitamos un resultado indeseado si el número total de votantes es 0
            {
                participation = .0f;
            }
            else
            {
                participation = totalVotes/(float)(center.totalVoters)*100;
            }
            printf("Participation: %d votes from %d voters (%.2f%%)\n", totalVotes, center.totalVoters, participation);
            printf("\n");
            p = nextC(p, mainList);
        }
    }

}

void Vote(tCenterName centerName, tPartyName partyName, tListC* mainList)
{
    tPosC p;
    tItemC center;
    p = findItemC(centerName, *mainList);
    if(p != NULLC){
        center = getItemC(p, *mainList);
        tList L = center.partyList;
        tPosL pos = findItem(partyName, L);
        if(pos != LNULL){    //Si la posición es distinta de LNULL, quiere decir que existe el partido en la lista
            tItemL item = getItem(pos, L);
            updateVotes(1, pos, &L);
            printf("* Vote: center %s party %s numvotes %d", centerName, partyName, item.numVotes+1);
            updateValidVotesC(1, p, mainList);
        }
        else{
            printf("+ Error: Vote not possible. Party %s not found in center %s. NULLVOTE", partyName, centerName);
            updateNullVotesC(1, p, mainList);
        }
    }else{
        printf("+ Error: Vote not possible");
    }
}

void Remove(tListC* mainList){
    tPosL r;
    int aux = 0;
    tList L;
    tItemC center;
    tPosC p, lastPosition = lastC(*mainList);

    if(!isEmptyListC(*mainList)){
        p=firstC(*mainList);
        while(p != NULLC){
            center = getItemC(p, *mainList);
            if(center.validVotes == 0){
                if(lastC(*mainList) == p){ //Comprobamos si estamos en la última posición de la lista
                    aux=1;
                }

                L = center.partyList;
                r=first(L);
                while(r != LNULL){ //Eliminamos la lista de partidos para liberar memoria
                    deleteAtPosition(r, &L);
                    r=first(L);
                }
                updateListC(L, p, mainList);

                deleteAtPositionC(p, mainList);
                printf("* Remove: center %s\n", center.centerName);
                if(aux == 1){ //Condición de salida del bucle
                    p = NULLC;
                }
            }
            else{
                p = nextC(p, *mainList);
            }
        }
        if(lastPosition == lastC(*mainList)){ //Si la posición final no cambió quiere decir que no se eliminó ningún partido
            printf("* Remove: no centers removed\n");
        }

    } else
        printf("* Remove: no centers removed\n");

}

void processCommand(char commandNumber[CODE_LENGTH+1], char command, char param1[NAME_LENGTH_LIMIT+1], char param2[NAME_LENGTH_LIMIT+1], tListC* mainList) {
    printf("Read from input file: %s %c %s %s\n", commandNumber, command, param1, param2);
    printf("********************");
    printf("\n");
    switch(command) {
        case 'C': {
            printf("%s %c: center %s totalvoters %s\n", commandNumber, command, param1, param2);
            int totalVoters;
            totalVoters = atoi(param2);
            Create(param1, totalVoters, mainList);
            printf("\n");
            break;
        }
        case 'N': {
            printf("%s %c: center %s party %s\n", commandNumber, command, param1, param2);
            New(param1, param2, mainList);
            printf("\n");
            break;
        }
        case 'V': {
            printf("%s %c: center %s party %s\n", commandNumber, command, param1, param2);
            Vote(param1, param2, mainList);
            printf("\n");
            break;
        }
        case 'S': {
            Stats(*mainList);
            break;
        }
        case 'R': {
            Remove(mainList);
            break;
        }
        default: {
            break;
        }
    }
}

void readTasks(char *filename, tListC* mainList) {
    FILE *df;
    char commandNumber[CODE_LENGTH+1], command, param1[NAME_LENGTH_LIMIT+1], param2[NAME_LENGTH_LIMIT+1];

    df = fopen(filename, "r");
    if (df != NULL) {
        while (!feof(df)) {
            char format[16];
            sprintf(format, "%%%is %%c ", CODE_LENGTH);
            fscanf(df, format, commandNumber, &command);
            if (command == 'S' || command == 'R') {
                param1[0] = '\0';
                param2[0] = '\0';
            } else {
                sprintf(format, "%%%is %%%is", NAME_LENGTH_LIMIT, NAME_LENGTH_LIMIT);
                fscanf(df, format, param1, param2);
            }
            processCommand(commandNumber, command, param1, param2, mainList);
        }
        fclose(df);
    } else {
        printf("Cannot open file %s.\n", filename);
    }
}

int main(int nargs, char **args) {
    tListC mainList;
    createEmptyListC(&mainList);

    char *filename = "new.txt";

    if (nargs > 1) {
        filename = args[1];
    } else {
        #ifdef INPUT_FILE
        filename = INPUT_FILE;
        #endif
    }

    readTasks(filename, &mainList);

    return 0;
}