package e2;

import java.util.Iterator;

public abstract class MatrixAddition {
    public static Matrix add(Matrix matrix1, Matrix matrix2) throws ArithmeticException {

        if ( matrix1.getNumRows() != matrix2.getNumRows()
                || matrix1.getNumColumns() != matrix2.getNumColumns() ) {
            throw new ArithmeticException("Arrays Conflict");
        }

        int[][] auxMatrix = new int[matrix1.getNumRows()][matrix1.getNumColumns()];
        Iterator<Integer> it1 = matrix1.columnRowIteration();
        Iterator<Integer> it2 = matrix2.columnRowIteration();

        for ( int i=0; i<matrix1.getNumRows(); i++ ) {
            for ( int j=0; j< matrix1.getNumColumns(); j++ ) {
                auxMatrix[i][j] = it1.next() + it2.next();
            }
        }

        return new Matrix(auxMatrix);
    }
}
