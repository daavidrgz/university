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

#define K1 1000
#define K2 1000

double microsegundos();
void inicializarSemilla();
void ord_ins(int n, int v[n]);
void ord_shell(int n, int v[n]);
void timeIns();
void timeShell();
int isSort(int n, int v[n]);
void ascIni(int n, int v[n]);
void randomIni(int n, int v[n]);
void descIni(int n, int v[n]);
void printVector(int n, int v[n]);
void testImplIns();
void testImplShell();
void printTextIns(int i);
void printTextShell(int i);
double repeatOrdIns(int n, int k, int v[n]);
double repeatOrdShell(int n, int k, int v[n]);


int main(){
    inicializarSemilla();
    testImplIns();
    testImplShell();

    printf("\n\n");
    timeIns();

    printf("\n\n");
    timeShell();
    return 0;
}

void inicializarSemilla(){
    srand(time(NULL));
}

void ord_ins(int n, int v[n]){
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

void ord_shell(int n, int v[n]){
    int incremento, tmp, j, i;
    bool seguir;

    incremento = n;
    do {
        incremento = incremento / 2;
        for ( i=incremento; i<n; i++ ) {
            tmp = v[i];
            j = i;
            seguir = true;
            while ( j-incremento >= 0 && seguir ) {
                if ( tmp < v[j-incremento] ) {
                    v[j] = v[j-incremento];
                    j=j-incremento;
                } else {
                    seguir = false;
                }
            }
            v[j] = tmp;
        }
    } while ( incremento != 1 );
}

void printTextIns(int i){
    if ( i == 0 ) {
        printf("\n\nORDENACIÓN POR INSERCIÓN CON INICIALIZACIÓN ASCENDENTE:\n\n");
    } else if ( i == 1 ) {
        printf("\n\nORDENACIÓN POR INSERCIÓN CON INICIALIZACIÓN DESCENDENTE:\n\n");
    } else {
        printf("\n\nORDENACIÓN POR INSERCIÓN CON INICIALIZACIÓN ALEATORIA:\n\n");
    }
}

void printTextShell(int i){
    if ( i == 0 ) {
        printf("\n\nORDENACIÓN SHELL CON INICIALIZACIÓN ASCENDENTE:\n\n");
    } else if ( i == 1 ) {
        printf("\n\nORDENACIÓN SHELL CON INICIALIZACIÓN DESCENDENTE:\n\n");
    } else {
        printf("\n\nORDENACIÓN SHELL CON INICIALIZACIÓN ALEATORIA:\n\n");
    }
}

void timeIns(){
    int v[32000];
    int i, n;
    double t1, t2, t;

    printf("\n\n**********INSERCIÓN**********\n\n");
    for ( i=0; i<3; i++ ) {
        printTextIns(i);
        printf("\n%10s%15s%15s%15s%15s\n\n", "n", "t(n)", "t(n)/f(n)",
                "t(n)/g(n)", "t(n)/h(n)");
        for ( n=500; n<=32000; n=2*n ) {
            if ( i == 0 ) ascIni(n, v);
            else if ( i == 1 ) descIni(n, v);
            else randomIni(n, v);

            t1 = microsegundos();
            ord_ins(n, v);
            t2 = microsegundos();
            t = t2 - t1;
            if ( t<500 ) {
                t = repeatOrdIns(n, i, v);
                printf("*");
            } else printf(" ");

            if ( i == 0 ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.8),
                        t/n, t/pow(n, 1.1));
            } else if ( i == 1 ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 1.8),
                        t/pow(n, 2), t/pow(n, 2.1));
            } else {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 1.8),
                        t/pow(n, 2), t/pow(n, 2.1));
            }
        }
    }
}

void timeShell(){
    int v[32000];
    int i, n;
    double t1, t2, t;

    printf("\n\n**********SHELL**********\n\n");
    for ( i=0; i<3; i++ ) {
        printTextShell(i);
        printf("\n%10s%15s%15s%15s%15s\n\n", "n", "t(n)", "t(n)/f(n)",
                "t(n)/g(n)", "t(n)/h(n)");
        for ( n=500; n<=32000; n=2*n ) {
            if ( i == 0 ) ascIni(n, v);
            else if ( i == 1 ) descIni(n, v);
            else randomIni(n, v);

            t1 = microsegundos();
            ord_shell(n, v);
            t2 = microsegundos();
            t = t2 - t1;
            if ( t<500 ) {
                t = repeatOrdShell(n, i, v);
                printf("*");
            } else printf(" ");

            if ( i == 0 ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/n,
                        t/pow(n, 1.1), t/pow(n, 1.3));
            } else if ( i == 1 ) {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/n,
                        t/pow(n, 1.15), t/pow(n, 1.3));
            } else {
                printf("%9d%15.4f%15.6f%15.7f%15.8f\n", n, t, t/n,
                        t/pow(n, 1.2), t/pow(n, 1.3));
            }
        }
    }
}

double repeatOrdIns(int n, int k, int v[n]){
    double t1, t2, ta, tb;
    int i;

    t1 = microsegundos();
    for ( i=0; i<K1; i++ ) {
        if ( k==0 ) ascIni(n, v);
        else if ( k==1 ) descIni(n, v);
        else randomIni(n, v);
        ord_ins(n, v);
    }
    t2 = microsegundos();
    ta = t2 - t1;

    t1 = microsegundos();
    for ( i=0; i<K1; i++ ) {
        if ( k==0 ) ascIni(n, v);
        else if ( k==1 ) descIni(n, v);
        else randomIni(n, v);
    }
    t2 = microsegundos();
    tb = t2 - t1;

    return ((ta-tb)/K1);
}

double repeatOrdShell(int n, int k, int v[n]){
    double t1, t2, ta, tb;
    int i;

    t1 = microsegundos();
    for ( i=0; i<K1; i++ ) {
        if ( k==0 ) ascIni(n, v);
        else if ( k==1 ) descIni(n, v);
        else randomIni(n, v);
        ord_shell(n, v);
    }
    t2 = microsegundos();
    ta = t2 - t1;

    t1 = microsegundos();
    for ( i=0; i<K1; i++ ) {
        if ( k==0 ) ascIni(n, v);
        else if ( k==1 ) descIni(n, v);
        else randomIni(n, v);
    }
    t2 = microsegundos();
    tb = t2 - t1;
    
    return ((ta-tb)/K1);
}

int isSort(int n, int v[n]){
    int i=0;
    while ( i != n-1 && v[i+1] >= v[i] ) {
        i++;
    }
    if ( i == n-1 )
        return 1;
    return 0;
}

void randomIni(int n, int v[n]){
    int m = 2*n+1, i;
    for ( i=0; i<n; i++ ) {
        v[i] = (rand() % m) - n;
    }
}

void descIni(int n, int v[n]){
    int i;
    for ( i=0; i<n; i++ ) {
        v[i] = n-i-1;
    }
}

void ascIni(int n, int v[n]){
    int i;
    for ( i=0; i<n; i++ ) {
        v[i] = i;
    }
}

void printVector(int n, int v[n]){
    int i;
    printf("(");
    for ( i=0; i<n-1; i++ ) {
        printf("%d, ", v[i]);
    }
    printf("%d)", v[n-1]);
    printf("\n");
}

void testImplIns(){
    int v[20];

    printf("\n\n===============TEST INSERCIÓN===============\n\n");
    printf("Inicialización aleatoria:\n");
    randomIni(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
    printf("-------Ordenación por Inserción-------\n");
    ord_ins(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));

    printf("Inicialización descendiente:\n");
    descIni(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
    printf("-------Ordenación por Inserción-------\n");
    ord_ins(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
}

void testImplShell(){
    int v[20];

    printf("\n\n===============TEST SHELL=================\n\n");
    printf("Inicialización aleatoria:\n");
    randomIni(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
    printf("-------Ordenación Shell-------\n");
    ord_shell(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));

    printf("Inicialización descendiente:\n");
    descIni(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
    printf("-------Ordenación Shell-------\n");
    ord_shell(20, v);
    printVector(20, v);

    printf("Ordenado: %d\n\n", isSort(20, v));
}

double microsegundos() {
    struct timeval t;
    if ( gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}
