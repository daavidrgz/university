package e2;

import java.util.Arrays;
import java.util.Iterator;

public class Matrix implements Iterable<Integer> {
    public int[][] matrix;
    private boolean isColumnRow;

    public Matrix(int[][] matrix) throws IllegalArgumentException {
        if ( matrix == null ) {
            throw new IllegalArgumentException("Null matrix");
        }
        for (int[] row : matrix) {
            if (row == null || matrix[0].length != row.length) {
                throw new IllegalArgumentException("Ragged Matrix");
            }
        }

        this.matrix = new int[matrix.length][matrix[0].length];
        for ( int i=0; i< matrix.length; i++ ) {
            for ( int j=0; j<matrix[0].length; j++ ) {
                this.matrix[i][j] = matrix[i][j];
            }
        }

        this.isColumnRow = true;
    }

    public Matrix(int rows, int columns) throws IllegalArgumentException{
        if ( rows < 1 || columns < 1 ) {
            throw new IllegalArgumentException("Invalid Argument");
        }

        this.matrix = new int[rows][columns];
        for ( int i=0; i< matrix.length; i++ ) {
            for ( int j=0; j<matrix[0].length; j++ ) {
                this.matrix[i][j] = 0;
            }
        }
        this.isColumnRow = true;
    }

    public int getNumRows() {
        return this.matrix.length;
    }

    public int getNumColumns() {
        return this.matrix[0].length;
    }

    public void insertNum(int num, int row, int column) throws IllegalArgumentException {
        if ( row >= this.getNumRows() || column >= this.getNumColumns() || row < 0 || column < 0 ) {
            throw new IllegalArgumentException("Index Out Of Bounds");
        }
        this.matrix[row][column] = num;
    }

    public int getNum(int row, int column) throws IllegalArgumentException {
        if ( row >= this.getNumRows() || column >= this.getNumColumns() || row < 0 || column < 0 ) {
            throw new IllegalArgumentException("Index Out Of Bounds");
        }
        return this.matrix[row][column];
    }

    public int[][] getMatrix() {
        int[][] auxMatrix = new int[this.getNumRows()][this.getNumColumns()];
        for ( int i=0; i< this.getNumRows(); i++ ) {
            for ( int j=0; j<this.getNumColumns(); j++ ) {
                auxMatrix[i][j] = this.matrix[i][j];
            }
        }
        return auxMatrix;
    }

    public int[] getRow(int row) throws IllegalArgumentException {
        int[] intArray = new int[this.getNumColumns()];
        if ( row >= this.getNumRows() || row < 0) {
            throw new IllegalArgumentException("Index Out Of Bounds");
        }
        for ( int i=0; i < this.getNumColumns(); i++ ) {
            intArray[i] = this.getNum(row, i);
        }
        return intArray;
    }

    public int[] getColumn(int column) throws IllegalArgumentException {
        int[] intArray = new int[this.getNumRows()];
        if ( column >= this.getNumColumns() || column < 0 ) {
            throw new IllegalArgumentException("Index Out Of Bounds");
        }
        for ( int i=0; i<this.getNumRows(); i++ ) {
            intArray[i] = this.getNum(i, column);
        }
        return intArray;
    }

    public void setIteratorType(boolean isColumnRow) {
        this.isColumnRow = isColumnRow;
    }

    public boolean getIteratorType() {
        return this.isColumnRow;
    }

    public RowColumnIterator rowColumnIterator() {
        return new RowColumnIterator(this);
    }

    public ColumnRowIterator columnRowIteration() {
        return new ColumnRowIterator(this);
    }

    @Override
    public String toString() {
        StringBuilder stringMatrix = new StringBuilder();
        int[][] matrix = this.getMatrix();
        int[] row;
        for ( int i=0; i < this.getNumRows(); i++ ) {
            row = matrix[i];
            stringMatrix.append(Arrays.toString(row)).append("\n");
        }
        return stringMatrix.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        if ( this.isColumnRow ) {
            return new ColumnRowIterator(this);
        } else {
            return new RowColumnIterator(this);
        }
    }
}
