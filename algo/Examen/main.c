/*
David Rodríguez Bacelar: david.rbacelar@udc.es
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <time.h>
#include <stdbool.h>
#define K 10

double microsegundos();
void inicializarSemilla();
int isSort(int v[], int n);
void ascIni(int v[], int n);
void randomIni(int v[], int n);
void descIni(int v[], int n);
void printVector(int v[], int n);
void ordenar(int v[], int n);
void test();
void timeSort(void (*fun)(int v[], int n));
double repeatSort(void (*fun)(int v[], int n), int n);
void printTimes(int n, double t, void (*fun)(int v[], int n));

int main() {
    int i;

    inicializarSemilla();
    test();
    printf("====================\n");
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
    for ( n = 16000; n <= 1024000 && t < 5000000; n=2*n ) {
        fun(v, n);
        t1 = microsegundos();
        ordenar(v, n);
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

    if ( fun == ascIni ) {
        printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.9),
                t/pow(n, 1.05), t/pow(n, 1.15));
        
    } else if ( fun == descIni ) {
        printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/n,
                t/pow(n, 1.1), t/pow(n, 1.25));

    } else {
        printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/n,
                t/pow(n, 1.15), t/pow(n, 1.3));
    }

}

void ordenar(int v[], int n) {
    int incrementos[16] = {65536, 32767, 16383, 8191, 4095, 2047, 1023, 511, 255, 127, 63, 31, 15, 7, 3, 1};
    int x, i, j, tmp, valor;

    for ( x=0; x<16; x++ ) {
        valor = incrementos[x];
        for ( i = valor; i<n; i++ ) {
            tmp = v[i];
            j = i;
            while ( j >= valor && v[j-valor] > tmp ) {
                v[j] = v[j-valor];
                j = j-valor;
            }
            v[j] = tmp;
        }
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
        ordenar(v, n);
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
    printf("Shell:\n");
    ordenar(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n", isSort(v, 20));

    printf("\n\n---> INICIALIZACIÓN ALEATORIA\n\n");
    randomIni(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n\n", isSort(v, 20));
    printf("Shell:\n");
    ordenar(v, 20);
    printVector(v, 20);
    printf("\n- Ordenado: %d\n\n", isSort(v, 20));
}

double microsegundos() {
    struct timeval t;
    if ( gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}
