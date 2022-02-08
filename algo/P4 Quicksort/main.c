/*
David Rodríguez Bacelar: david.rbacelar@udc.es
Anxo Portela Iglesias: anxo.portela.iglesias@udc.es
Sergio Rodríguez Seoane: sergio.rodriguez.seoane@udc.es
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <time.h>
#include <stdbool.h>
#define UMBRAL 1
#define K 10

double microsegundos();
void inicializarSemilla();
int isSort(int v[], int n);
void ascIni(int v[], int n);
void randomIni(int v[], int n);
void descIni(int v[], int n);
void printVector(int v[], int n);
void ord_ins(int v[], int n);
void intercambiar(int v[], int i, int j);
void ord_rapida(int v[], int n);
void rapida_aux(int v[], int izq, int der);
void test();
void timeSort(void (*fun)(int v[], int n));
double repeatSort(void (*fun)(int v[], int n), int n);
void printTimes(int n, double t, void (*fun)(int v[], int n));

int main() {
    int i;

    inicializarSemilla();
    test();
    printf("====================\n");
    printf("\nUMBRAL = %d\n", UMBRAL);
    for ( i=0; i<3;i++) {
        printf("\nORDENACIÓN ASCENDENTE:\n");
        timeSort(ascIni);
        printf("\nORDENACIÓN DESCENDENTE:\n");
        timeSort(descIni);
        printf("\nORDENACIÓN ALEATORIA:\n");
        timeSort(randomIni);
        printf("\n---------------\n");
    }
}

void inicializarSemilla(){
    srand(time(NULL));
}

void timeSort(void (*fun)(int v[], int n)) {
    int n;
    int v[2048000];
    double t1, t2, t;
    
    printf("\n%10s%15s%15s%15s%15s\n\n", "n", "t(n)", "t(n)/f(n)",
            "t(n)/g(n)", "t(n)/h(n)");
    for ( n = 16000; n <= 2048000 && t < 5000000; n=2*n ) {
        fun(v, n);
        t1 = microsegundos();
        ord_rapida(v, n);
        t2 = microsegundos();
        t = t2 - t1;
        if ( t<500 ) {
            t = repeatSort(fun, n);
            printf("*");
        } else printf(" ");
        printTimes(n, t, fun);
    }
}

void printTimes(int n, double t, void (*fun)(int v[], int n)) {

    switch ( UMBRAL ) {
        case 1:
            if ( fun == randomIni ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                        t/(n*log2(n)), t/pow(n, 1.2));
            } else {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                        t/pow(n, 1.05), t/pow(n, 1.2));
            }
            break;
        case 10:
            printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                    t/(n*log2(n)), t/pow(n, 1.2));
            break;
        case 100:
            if ( fun == randomIni ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                        t/(n*log2(n)), t/pow(n, 1.2));
            } else {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                        t/pow(n, 1.1), t/pow(n, 1.2));
            }
            break;
    }
}

double repeatSort(void (*fun)(int v[], int n), int n) {
    int i = 0;
    int* v;
    double t1, t2, ta, tb;
	v = malloc(n*sizeof(int));
    t1 = microsegundos();
    for ( i=0; i<K; i++ ) {
        fun(v, n);
        ord_rapida(v, n);
    }
    t2 = microsegundos();
    ta = t2 - t1;

    t1 = microsegundos();
    for ( i=0; i<K; i++ ) {
        fun(v, n);
    }
    t2 = microsegundos();
    tb = t2 - t1;
    free(v);
    return ((ta-tb)/K);
}

void ord_ins(int v[], int n) {
    int x, j, i;

    for ( i=1; i<n; i++ ) {
        x = v[i];
        j = i - 1;
        while ( j > -1 && v[j] > x ) {
            v[j+1] = v[j];
            j--;
        }
        v[j+1] = x;
    }
}

void ord_rapida(int v[], int n) {

    rapida_aux(v, 0, n-1);
    if ( UMBRAL > 1 ) {
        ord_ins(v, n);
    }
}

void rapida_aux(int v[], int izq, int der) {
    int x, pivote, i, j;

    if ( izq+UMBRAL <= der ) {
        x = rand() % (der-izq+1) + izq;
        pivote = v[x];
        intercambiar(v, izq, x);
        i = izq + 1;
        j = der;
        while ( i <= j ) {
            while ( i <= der && v[i] < pivote ) {
                i = i + 1;
            }
            while ( v[j] > pivote ) {
                j = j - 1;
            }
            if ( i <= j ) {
                intercambiar(v, i, j);
                i = i + 1;
                j = j - 1;
            }
        }
        intercambiar(v, izq, j);
        rapida_aux(v, izq, j-1);
        rapida_aux(v, j+1, der);
    }
}

void intercambiar(int v[], int i, int j) {
    int aux;

    aux = v[j];
    v[j] = v[i];
    v[i] = aux;
}

int isSort(int v[], int n){
    int i=0;
    while ( i != n-1 && v[i+1] >= v[i] ) {
        i++;
    }
    if ( i == n-1 )
        return 1;
    return 0;
}

void randomIni(int v[], int n){
    int m = 2*n+1, i;
    for ( i=0; i<n; i++ ) {
        v[i] = (rand() % m) - n;
    }
}

void descIni(int v[], int n){
    int i;
    for ( i=0; i<n; i++ ) {
        v[i] = n-i-1;
    }
}

void ascIni(int v[], int n){
    int i;
    for ( i=0; i<n; i++ ) {
        v[i] = i;
    }
}

void printVector(int v[], int n){
    int i;
    printf("(");
    for ( i=0; i<n-1; i++ ) {
        printf("%d, ", v[i]);
    }
    printf("%d)", v[n-1]);
    printf("\n");
}

void test() {
    int v[20];

    printf("\n---> INICIALIZACIÓN DESCENDENTE\n\n");
    descIni(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n\n", isSort(v, 20));
    printf("Quicksort:\n");
    ord_rapida(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n", isSort(v, 20));

    printf("\n\n---> INICIALIZACIÓN ALEATORIA\n\n");
    randomIni(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n\n", isSort(v, 20));
    printf("Quicksort:\n");
    ord_rapida(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n\n", isSort(v, 20));
}

double microsegundos() {
    struct timeval t;
    if ( gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}
