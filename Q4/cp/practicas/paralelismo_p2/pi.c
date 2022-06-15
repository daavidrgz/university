#include <stdio.h>
#include <math.h>
#include <mpi/mpi.h>

#define INTERVALS 10

#ifndef ROOT
#define ROOT 0
#endif

int MPI_FlattreeColectiva(void* buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm) {
    int rank, numProcs;
    MPI_Comm_size(comm, &numProcs);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if ( rank == root ) {
        for ( int i = 1; i<numProcs; i++ )
            MPI_Send(buffer, count, datatype, (root + i)%numProcs, INTERVALS, comm);
    } else
        MPI_Recv(buffer, count, datatype, root, INTERVALS, comm, MPI_STATUS_IGNORE);

    return MPI_SUCCESS;
}

int MPI_BinomialColectiva(void* buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm) {
    int rank, numProcs;
    MPI_Comm_size(comm, &numProcs);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    int logicRank = (numProcs + rank - root)%numProcs;

    if ( rank != root )
        MPI_Recv(buffer, count, datatype, MPI_ANY_SOURCE, INTERVALS, comm, MPI_STATUS_IGNORE);

    for ( int i = 1;;i *= 2 ) {
        if ( logicRank < i ) {
            if ( logicRank + i >= numProcs )
                break;
            MPI_Send(buffer, count, datatype, (rank + i)%numProcs, INTERVALS, comm);
        }
    }
    return MPI_SUCCESS;
}

int main(int argc, char* argv[]) {      
    int rank, numProcs;
    long n;
    double PI25DT = 3.141592653589793238462643;
    double pi, h, hHalf, sum, x, totalSum;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

    while ( 1 ) {
        if ( rank == ROOT ) {
            printf("Enter the number of intervals: (0 quits) \n");
            scanf("%ld",&n);
        }
        
        MPI_BinomialColectiva(&n, 1, MPI_LONG, ROOT, MPI_COMM_WORLD);
        if ( n == 0 ) break;

        h = 1.0 / n;
        sum = 0.0;
        hHalf = h * 0.5;
        for ( int i = rank+1; i <= n; i += numProcs ) {
            x = h * (double)i - hHalf;
            sum += 4.0 / (1.0 + x*x);
        }

        MPI_Reduce(&sum, &totalSum, 1, MPI_DOUBLE, MPI_SUM, ROOT, MPI_COMM_WORLD);
        if ( rank == ROOT ) {
            pi = h * totalSum;
            printf("pi is approximately %.16f, Error is %.16f\n", pi, fabs(pi - PI25DT));
        }
    }

    MPI_Finalize();
}
