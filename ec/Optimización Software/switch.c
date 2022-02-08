#include <time.h>
#include <stdio.h>
#include <sys/time.h>
#include <stdlib.h>
#include <math.h>

int main() {
	int i;
	srand(time(NULL));
	i = rand() % 4;
	if ( i == 0 ) {
		printf("i = 0");
	} else if ( i == 1 ) {
		printf("i = 1");
	} else if ( i == 2 ) {
		printf("i = 2");
	} else {
		printf("i = 3");
	}
	return 0;
} 
