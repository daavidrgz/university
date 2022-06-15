#include <stdio.h>
#include <math.h>
#include <mpi/mpi.h>

#define INTERVALS 10
#define SUM 20

int main(int argc, char* argv[])
{   
    int rank, numProcs;
    long n;
    double PI25DT = 3.141592653589793238462643;
    double pi, h, hHalf, sum, x;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

    while (1) {   
        if ( rank == 0 ) {
            printf("Enter the number of intervals: (0 quits) \n");
            scanf("%ld",&n);

            for ( int i = 1; i<numProcs; i++ )
                MPI_Send(&n, 1, MPI_DOUBLE, i, INTERVALS, MPI_COMM_WORLD);

        } else {
            MPI_Recv(&n, 1, MPI_DOUBLE, 0, INTERVALS, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        if (n == 0) break;
        
        h = 1.0 / n;
        sum = 0.0;
        hHalf = h * 0.5;
        for (int i = rank+1; i <= n; i += numProcs) {
            x = h * (double)i - hHalf;
            sum += 4.0 / (1.0 + x*x);
        }

        if ( rank == 0 ) {
            for ( int j = 1; j<numProcs; j++ ) {
                double partialSum;
                MPI_Recv(&partialSum, 1, MPI_DOUBLE, j, SUM, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                sum += partialSum;
            }

            pi = h * sum;
            printf("pi is approximately %.16f, Error is %.16f\n", pi, fabs(pi - PI25DT));

        } else {
            MPI_Send(&sum, 1, MPI_DOUBLE, 0, SUM, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
}
