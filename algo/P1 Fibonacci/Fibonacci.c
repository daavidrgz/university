#include <stdio.h>
#include <sys/time.h>
#include <math.h>

#define K1 100000
#define K2 1000
#define K3 100000


double microsegundos();
double repeatFib1(int n);
double repeatFib2(int n);
double repeatFib3(int n);
int fib1(int n);
int fib2(int n);
int fib3(int n);
void timeFib1();
void timeFib2();
void timeFib3();

int main(){
	int i, c;

    printf("\n%9s%11s%11s%11s\n\n", "n", "fib1", "fib2", "fib3");
    for ( c = 1; c <= 20; c++ ) {
        printf("%9d%11d%11d%11d\n", c, fib1(c), fib2(c), fib3(c));
    }
    for ( i = 0; i < 5; i++ ) {
        timeFib1();
        timeFib2();
        timeFib3();
        printf("\n\n");
    }
    return 0;
}

double microsegundos() {
    struct timeval t;
    if ( gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}

int fib1(int n) {
    if ( n < 2 )
        return n;
    else
        return fib1(n-1) + fib1(n-2);
}

int fib2(int n) {
    int i = 1, j=0, k;

    for( k=1; k<=n; k++ ) {
        j = j+i;
        i = j-i;
    }
    return j;
}

int fib3(int n) {
    int i = 1,  j= 0, k = 0, h = 1, t = 0;

    while ( n > 0 ) {
        if ( n%2 != 0 ) {
            t = j*h;
            j = i*h + j*k + t;
            i = i*k + t;
        }
        t = h*h;
        h = 2*k*h + t;
        k = k*k + t;
        n = n/2;
    }
    return j;
}

void timeFib1() {
    double t1, t2, t;
	int n;

    printf("\n- - - - - - - - - - Fib1 - - - - - - - - - - - - - - - - - - - - - - -\n\n");
    printf("\n%10s%15s%15s%15s%15s\n", "n", "t(n)", "t(n)/f(n)", "t(n)/g(n)", "t(n)/h(n)");
    for ( n = 2; n <= 32; n = 2*n ) {
        t1 = microsegundos();
        fib1(n);
        t2 = microsegundos();
        t = t2 - t1;
        if ( t<500 ) {
            t = repeatFib1(n);
            printf("*");
        } else {
            printf(" ");
        }
        printf("%9d%15.3f%15.6f%15.7f%15.8f\n", n, t, t/pow(1.1, n), t/pow((1+sqrt(5))/2, n), t/pow(2, n));
    }

    printf("\n\n");
}

void timeFib2(){
    double t1, t2, t;
	int n;

    printf("- - - - - - - - - - Fib2 - - - - - - - - - - - - - - - - - - - - - - -\n\n");
    printf("\n%10s%15s%15s%15s%15s\n", "n", "t(n)", "t(n)/f(n)", "t(n)/g(n)", "t(n)/h(n)");
    for ( n = 1000; n <= 10000000; n = 10*n ) {
        t1 = microsegundos();
        fib2(n);
        t2 = microsegundos();
        t = t2 - t1;
        if ( t < 500 ) {
            t = repeatFib2(n);
            printf("*");
        } else {
            printf(" ");
        }
        printf("%9d%15.3f%15.6f%15.7f%15.8f\n", n, t, t/pow(n, 0.8), t/n, t/(n*log(n)));
    }

    printf("\n\n");
}

void timeFib3(){
    double t1, t2, t;
	int n;

    printf("- - - - - - - - - - Fib3 - - - - - - - - - - - - - - - - - - - - - - -\n\n");
    printf("\n%10s%15s%15s%15s%15s\n", "n", "t(n)", "t(n)/f(n)", "t(n)/g(n)", "t(n)/h(n)");
    for ( n = 1000; n <= 10000000; n = 10*n ) {
        t1 = microsegundos();
        fib3(n);
        t2 = microsegundos();
        t = t2 - t1;
        if ( t < 500 ) {
            t = repeatFib3(n);
            printf("*");
        }
        printf("%9d%15.5f%15.6f%15.7f%15.8f\n", n, t, t/sqrt(log(n)), t/log(n), t/pow(n, 0.5));
    }
    printf("\n\n");
}

double repeatFib1(int n){
    double t1, t2;
	int i;

    t1 = microsegundos();
    for ( i = 0; i < K1; i++ ) {
        fib1(n);
    }
    t2 = microsegundos();
    return ((t2-t1)/K1);
}

double repeatFib2(int n){
    double t1, t2;
	int i;

    t1 = microsegundos();
    for ( i = 0; i < K2; i++ ) {
        fib2(n);
    }
    t2 = microsegundos();
    return ((t2-t1)/K2);
}

double repeatFib3(int n){
    double t1, t2;
	int i;

    t1 = microsegundos();
    for ( i = 0; i < K3; i++ ) {
        fib3(n);
    }
    t2 = microsegundos();
    return ((t2-t1)/K3);
}