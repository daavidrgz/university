/*
 * TITLE: PROGRAMMING II LABS
 * SUBTITLE: Practical 1
 * AUTHOR 1: David Rodríguez Bacelar LOGIN 1: david.rbacelar
 * AUTHOR 2: Inmaculada López Vázquez LOGIN 2: i.lopezv
 * GROUP: 2.4
 * DATE: ** / ** / **
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define CODE_LENGTH 2

#ifdef STATIC_LIST
#include "static_list.h"
#endif
#ifdef DYNAMIC_LIST
#include "dynamic_list.h"
#endif
#ifdef TEST_LIST
#include "list/list.h"
#endif

typedef tNumVotes* tPNumVotes;
void new(char partyName[NAME_LENGTH_LIMIT+1], tList* L)
{
    if(findItem(partyName, *L) == LNULL) //Se comrpueba si existe algún partido ya creado con el mismo nombre
    {
        tItemL item; //Declaramos un tipo Item al que le asigamos el conjunto de valores (partyName y numVotes) necesarios
        strcpy(item.partyName, partyName);
        item.numVotes = 0;
        if(insertItem(item, LNULL, L)) //Si el elemento no pudo ser insertado, la función insertItem() devuelve falso, por lo que se imprime un mensaje de error
        {
            printf("* New: party %s", partyName);
        }
        else
        {
            printf("+ Error: New not possible");
        }
    }
    else
    {
        printf("+ Error: New not possible"); //Si existe un partido con el mismo nombre, la condición del if no se cumple ya que findItem() devolvería una posicion distinta a LNULL y se muestra así un mensaje de error
    }


}
void stats(int totalVoters, tList* L, tPNumVotes ptotalVotes, tPNumVotes pnullVotes)
{
    float partyVotes, participation; //Variable que representa el número de votos de un partido respecto al total de votos no nulos, por lo tanto, de tipo float
    tItemL item;
    tPosL position = first(*L); //Inicializamos la variable position a la posición inicial de la lista
    while(position != LNULL) //Si la posicion excede la ultima posicion válida de la lista devuelve LNULL
    {
        item = getItem(position, *L); //Cogemos el item de la posición actual y lo analizamos
        if(*ptotalVotes-*pnullVotes == 0) //Condición para evitar que el resultado de una división por 0 de como resultado un Not A Number (nan)
        {
            partyVotes = .0f;
        }
        else
        {
            partyVotes = item.numVotes/(float)(*ptotalVotes-*pnullVotes)*100;
        }
        printf("Party %s numvotes %d (%.2f%%)\n", item.partyName, item.numVotes, partyVotes);
        position = next(position, *L); //Establecemos la posición siguiente
    }
    printf("Null votes %d\n", *pnullVotes);
    if(totalVoters == 0) //De nuevo aplicamos la misma condición que, aunque en los casos de la práctica nunca se da, evitamos un resultado indeseado si el número total de votantes es 0
    {
        participation = .0f;
    }
    else
    {
        participation = *ptotalVotes/(float)totalVoters*100;
    }
    printf("Participation: %d votes from %d voters (%.2f%%)", *ptotalVotes, totalVoters, participation);
}
void vote(char partyName[NAME_LENGTH_LIMIT+1], tList* L, tPNumVotes ptotalVotes, tPNumVotes pnullVotes)
{
    tPosL pos = findItem(partyName, *L);
    if(pos != LNULL) //Si la posición es distinta de LNULL, quiere decir que existe el partido en la lista
    {
        tItemL item = getItem(pos, *L);
        updateVotes(item.numVotes+1, pos, L);
        printf("* Vote: party %s numvotes %d", partyName, item.numVotes+1);

    }
    else
    {
        printf("+ Error: Vote not possible. Party %s not found. NULLVOTE", partyName);
        *pnullVotes += 1;
    }
    *ptotalVotes += 1;

}
void illegalize(char partyName[NAME_LENGTH_LIMIT+1], tList* L, tPNumVotes pnullVotes)
{
    tPosL pos = findItem(partyName, *L);
    if(pos != LNULL) //De nuevo, si la posición es distinta de LNULL, nos indica que existe ese nombre de partido en la lista
    {
        tItemL item;
        item = getItem(pos, *L); //Necesitamos obetner el número de votos del partido para convertirlos a nulos por lo que obtenemos el item en esa posición
        *pnullVotes += item.numVotes;
        deleteAtPosition(pos, L); //Borramos el partido
        printf("* Illegalize: party %s", partyName);
    }
    else
    {
        printf("+ Error: Illegalize not possible");
    }
}
void processCommand(char command_number[CODE_LENGTH+1], char command, char param[NAME_LENGTH_LIMIT+1], tList* pList, tPNumVotes ptotalVotes, tPNumVotes pnullVotes) {
    printf("********************");
    printf("\n");
    switch(command) {
        case 'N': {
            printf("%s %c: party %s.\n", command_number, command, param);
            new(param, pList);
            break;
        }
        case 'V': {
            printf("%s %c: party %s.\n", command_number, command, param);
            vote(param, pList, ptotalVotes, pnullVotes);
            break;
        }
        case 'I': {
            printf("%s %c: party %s.\n", command_number, command, param);
            illegalize(param, pList, pnullVotes);
            break;
        }
        case 'S': {
            int totalVoters;
            printf("%s %c: totalvoters %s.\n", command_number, command, param);
            //Ya que el param que nos devuelve la funcion es de tipo string, hace falta aplicar un algoritmo que lo convierta a un tipo int; utilizamos la función atoi
            totalVoters=atoi(param);
            stats(totalVoters, pList, ptotalVotes, pnullVotes);
            break;
        }
        default: {
            break;
        }
    }
    printf("\n");
}

void readTasks(char* filename, tList* pList, tPNumVotes ptotalVotes, tPNumVotes pnullVotes) {
    printf("%p", filename);
    FILE *df;
    char command_number[CODE_LENGTH+1], command, param[NAME_LENGTH_LIMIT+1];
    df = fopen(filename, "r");
    if (df != NULL) {
        while (!feof(df)) {
            char format[16];
            sprintf(format, "%%%is %%c %%%is", CODE_LENGTH, NAME_LENGTH_LIMIT);
            fscanf(df,format, command_number, &command, param);
            processCommand(command_number, command, param, pList, ptotalVotes, pnullVotes);
        }
        fclose(df);
    }
    else
    {
        printf("Cannot open file %s.\n", filename);
    }
}

int main(int nargs, char **args) {
    tPNumVotes main_totalVotes = malloc(sizeof(tNumVotes)); //Declaramos e inicializamos dos variables de tipo tPNumVotes (puntero al tipo tNumVotes); están declaradas en el main() para su sencilla utilización en otras funciones
    tPNumVotes main_nullVotes = malloc(sizeof(tNumVotes));
    *main_totalVotes = 0;
    *main_nullVotes = 0;
    tList L; //Lista Principal
    createEmptyList(&L);
    char *file_name;

    if (nargs > 1) {
        file_name = args[1];
    }
    else
    {
        #ifdef INPUT_FILE
        file_name = INPUT_FILE;
        #endif
    }
    printf("%s", file_name);
    readTasks(file_name, &L, main_totalVotes, main_nullVotes);

    return 0;
}


