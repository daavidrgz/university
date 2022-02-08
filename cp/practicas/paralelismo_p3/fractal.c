/*The Mandelbrot set is a fractal that is defined as the set of points c
in the complex plane for which the sequence z_{n+1} = z_n^2 + c
with z_0 = 0 does not tend to infinity.*/

/*This code computes an image of the Mandelbrot set.*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <mpi/mpi.h>
#include <math.h>

#define  DEBUG 1
#define  ROOT 0

#define		X_RESN  1025  /* x resolution */
#define		Y_RESN  1025  /* y resolution */

/* Boundaries of the mandelbrot set */
#define		X_MIN  -2.0
#define		X_MAX   2.0
#define		Y_MIN  -2.0
#define		Y_MAX   2.0

/* More iterations -> more detailed image & higher computational cost */
#define		maxIterations  2000

typedef struct complextype {
  float real, imag;
} Compl;

static inline double get_seconds(struct timeval t_ini, struct timeval t_end) {
  return (t_end.tv_usec - t_ini.tv_usec) / 1E6 +
         (t_end.tv_sec - t_ini.tv_sec);
}

int main(int argc, char* argv[]) {
  /* Mandelbrot variables */
  int i, j, k;
  Compl z, c;
  float lengthsq, temp;
  int *vres, *res[Y_RESN];
	int numProcs, rank;
	long totalFlops;

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &numProcs);

  /* Timestamp variables */
  struct timeval  ti, tf;

	if ( rank == ROOT ) {
		/* Allocate result matrix of Y_RESN x X_RESN */
		vres = (int *) malloc(ceil((float)Y_RESN / numProcs) * numProcs * X_RESN * sizeof(int));
		if ( !vres ) {
			fprintf(stderr, "Error allocating memory\n");
			return 1;
		}
		for ( i=0; i < Y_RESN; i++ )
			res[i] = vres + i*X_RESN;
	}

	/* GET BLOCK SIZE */
	int BLOCK_Y = ceil((float)Y_RESN / numProcs);
	int *vresLocal, *resLocal[BLOCK_Y];
	long flops = 0;

	vresLocal = (int *)malloc(BLOCK_Y * X_RESN * sizeof(int));
	if ( !vresLocal ) {
		fprintf(stderr, "Error allocating memory\n");
		return 1;
	}
	for ( i=0; i < BLOCK_Y; i++ )
		resLocal[i] = vresLocal + i*X_RESN; 
  
  int start = BLOCK_Y * rank;
  int end = (rank == numProcs - 1 ? Y_RESN : BLOCK_Y * (rank + 1));
  /* Start measuring time */
  gettimeofday(&ti, NULL);

  /* Calculate and draw points */
  for ( i=start; i < end; i++ ) {
    for ( j=0; j < X_RESN; j++ ) {
      z.real = z.imag = 0.0;
      c.real = X_MIN + j * (X_MAX - X_MIN)/X_RESN;
      c.imag = Y_MAX - i * (Y_MAX - Y_MIN)/Y_RESN;
      k = 0;

      do { /* iterate for pixel color */
        temp = z.real*z.real - z.imag*z.imag + c.real;
        z.imag = 2.0*z.real*z.imag + c.imag;
        z.real = temp;
        lengthsq = z.real*z.real+z.imag*z.imag;
        k++;

      } while ( lengthsq < 4.0 && k < maxIterations );

			flops += k * 10;

      if ( k >= maxIterations )
				resLocal[i%BLOCK_Y][j] = 0;
      else
				resLocal[i%BLOCK_Y][j] = k;
    }
  }

  /* MEASUREMENTS */
  gettimeofday(&tf, NULL);
  fprintf(stderr, "Time (seconds) ---  %-13s -> %10lfs (PROC %d)\n",
    "COMPUTATION", get_seconds(ti,tf), rank);

	/* GATHER RESULTS */
	gettimeofday(&ti, NULL);

	MPI_Gather(resLocal[0], BLOCK_Y * X_RESN, MPI_INT, res[0], BLOCK_Y * X_RESN, MPI_INT, ROOT, MPI_COMM_WORLD);
  
	gettimeofday(&tf, NULL);
  fprintf(stderr, "Time (seconds) ---  %-13s -> %10lfs (PROC %d)\n",
    "COMMUNICATION", get_seconds(ti,tf), rank);

	MPI_Allreduce(&flops, &totalFlops, 1, MPI_LONG, MPI_SUM, MPI_COMM_WORLD);
	fprintf(stderr, "Operations -> %12ld flops // Relative Flops -> %10lf (PROC %d)\n", 
    flops, (double)totalFlops/(flops*numProcs), rank);

  /* Print result out */
  if ( DEBUG && rank == ROOT ) {
    for ( i=0; i < Y_RESN; i++ ) {
      for ( j=0; j < X_RESN; j++ )
        printf("%3d ", res[i][j]);
      printf("\n");
    }

		free(vres);
  }

	free(vresLocal);

	MPI_Finalize();

  return 0;
}
