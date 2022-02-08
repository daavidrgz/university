 
/*
David Rodríguez Bacelar: david.rbacelar@udc.es
Anxo Portela Iglesias: anxo.portela.iglesias@udc.es
Sergio Rodríguez Seoane: sergio.rodriguez.seoane@udc.es
*/

#include <sys/time.h>
#include <time.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

struct nodo {
    int elem;
    int num_repeticiones;
    struct nodo *izq, *der;
};
typedef struct nodo * posicion;
typedef struct nodo * arbol; 

arbol insertar(int e, arbol a);
arbol crearArbol();
int esArbolVacio(arbol A);
posicion buscar(int e, arbol A);
posicion hijoIzquierdo(arbol A);
posicion hijoDerecho(arbol A);
int elemento(posicion P);
int numeroRepeticiones(posicion P);
int altura(arbol A);
arbol eliminarArbol(arbol A);
void visualizar(arbol A);
double microsegundos();
void inicializarSemilla();
void test();
int getRandomNum(int limit);
void timeFindSearch();
void printTables(int n, bool isInsert, double times[8]);

int main() {
    inicializarSemilla();
    test();
    timeFindSearch();
}

void printTables(int n, bool isInsert, double times[8]) {
    int i;
    double t;

    printf("\n%9s%15s%15s%15s%15s\n\n", "n", "t(n)", "t(n)/f(n)",
            "t(n)/g(n)", "t(n)/h(n)");
    for ( i=0; i<8; i++ ) {
        t = times[i];
        if ( isInsert ) {
            printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 1.1),
                    t/pow(n, 1.35), t/pow(n, 1.5));
        } else {
            printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 1.1),
                    t/pow(n, 1.35), t/pow(n, 1.5));
        }
        n = 2*n;
    }
}

void timeFindSearch() {
    arbol T = crearArbol();
    int i, j, h, rand, n=8000;
    double ta, tb, t1, t2, t;
    double times[2][8];

    printf("\n%9s%15s%15s\n\n", "n", "t_ins(n)", "t_sea(n)");
    for ( h=0; h<8; h++ ) {
        for ( j=0; j<2; j++ ) {
            t1 = microsegundos();
            for ( i=0; i<n; i++ ) {
                if ( j == 0 ) {
                    rand = getRandomNum(n);
                    T = insertar(rand, T);
                } else {
                    rand = getRandomNum(n);
                    buscar(rand, T);
                }
            }
            t2 = microsegundos();
            ta = t2 - t1;
            t1 = microsegundos();
            for ( i=0; i<n; i++ ) {
                if ( j == 0 ) {
                    rand = getRandomNum(n);
                } else {
                    rand = getRandomNum(n);
                }
            }
            t2 = microsegundos();
            tb = t2 - t1;
            t = ta - tb;
            times[j][h] = t;
        }
        printf("%9d%15.4f%15.6f\n", n, times[0][h], times[1][h]);
        T = eliminarArbol(T);
        n = 2*n;
    }
    printf("\n\n******** INSERTAR ********\n");
    printTables(8000, true, times[0]);
    printf("\n\n******** BUSCAR ********\n");
    printTables(8000, false, times[1]);
}

void test() {
    int i, rand;
    arbol T;
    posicion P;
    
    printf("========== TEST ==========\n\nCreando árbol\n");
    T = crearArbol();
    printf("- Altura del árbol: %d\n", altura(T));
    for ( i=0; i<5; i++ ) {
        rand = getRandomNum(10);
        printf("Insertando elemento %d\n", rand);
        T = insertar(rand, T);
    }
    visualizar(T);
    printf("- Altura del árbol: %d\n", altura(T));
    printf("Borrando árbol\n");
    T = eliminarArbol(T);
    visualizar(T);
    printf("Insertando elemento 3\n");
    T = insertar(3, T);
    printf("Insertando elemento 4\n");
    T = insertar(4, T);
    printf("Insertando elemento 0\n");
    T = insertar(0, T);
    printf("Insertando elemento 0\n");
    T = insertar(0, T);
    visualizar(T);
    printf("Buscando el elemento 3:\n");
    P = buscar(3, T);
    printf("- Elemento %d encontrado con %d repeticiones\n",
            elemento(P), numeroRepeticiones(P));
    printf("Buscando el elemento 0:\n");
    P = buscar(0, T);
    printf("- Elemento %d encontrado con %d repeticiones\n",
            elemento(P), numeroRepeticiones(P));
    printf("Buscando el elemento 5:\n");
    if ( (P = buscar(5, T)) == NULL )
        printf("- Elemento 5 no encontrado\n");

    printf("\n========== FIN TESTS ==========\n");
}

int getRandomNum(int limit) {
    int m = 2*limit+1;
    return (rand() % m) - limit;
}

void inicializarSemilla(){
    srand(time(NULL));
}

double microsegundos() {
    struct timeval t;
    if ( gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}

static struct nodo* crearNodo(int e) {
    struct nodo* p = malloc(sizeof(struct nodo));
    if (p == NULL) {
        printf("Out of memory\n"); exit(EXIT_FAILURE);
    }
    p->elem = e;
    p->num_repeticiones = 1;
    p->izq = NULL;
    p->der = NULL;
    return p;
}

arbol insertar(int e, arbol A) {
    if (A == NULL)
        return crearNodo(e);
    else if (e < A->elem)
        A->izq = insertar(e, A->izq);
    else if (e > A->elem)
        A->der = insertar(e, A->der);
    else
        A->num_repeticiones++;
    return A;
}

arbol crearArbol() {
    return NULL;
}

int esArbolVacio(arbol A) {
    if ( A == NULL )
        return 1;
    else
        return 0;
}

posicion buscar(int e, arbol A) {
    if ( esArbolVacio(A) == 1 ) {
        return NULL;
    } else if ( e == A->elem ) {
        return A;
    } else if ( e < A->elem ) {
        return buscar(e, A->izq);
    } else {
        return buscar(e, A->der);
    }
}

void visualizarAux(arbol A) {
    if ( !esArbolVacio(A) ) {
        if ( !esArbolVacio(A->izq) ) {
            printf("(");
            visualizarAux(A->izq);	
            printf(")");
        }
        printf(" %d ", elemento(A));
        if ( !esArbolVacio(A->der) ) {
            printf("(");
            visualizarAux(A->der);	
            printf(")");
        }
    } else {
        printf("( )");
    }
}

void visualizar(arbol A){
    visualizarAux(A);
    printf("\n");
}

arbol eliminarArbol(arbol A) {
    if ( esArbolVacio(A) ) {
        return NULL;
    }
    A->izq = eliminarArbol(A->izq);
    A->der = eliminarArbol(A->der);
    free(A);
    return NULL;
}

posicion hijoIzquierdo(arbol A) {
    if ( !esArbolVacio(A) )
        return A->izq;
    else {
        printf("Error: Árbol vacío");
        return NULL;
    }
}

posicion hijoDerecho(arbol A) {
    if ( !esArbolVacio(A) )
        return A->der;
    else {
        printf("Error: Árbol vacío");
        return NULL;
    }
}

int elemento(posicion P) {
    if ( !esArbolVacio(A) )
        return P->elem;
    else {
        printf("Error: Árbol vacío");
        return -1;
    }  
}

int numeroRepeticiones(posicion P) {
    if ( !esArbolVacio(A) )
        return P->num_repeticiones;
    else {
        printf("Error: Árbol vacío");
        return -1;
    }
}

int altura(arbol A) {
    if ( A == NULL ) {
        return -1;
    } else {
        return 1 + fmax(altura(A->izq), altura(A->der));
    }
}