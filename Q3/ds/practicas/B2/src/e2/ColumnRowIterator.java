package e2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ColumnRowIterator implements Iterator<Integer> {
    private final Matrix matrix;
    private int row;
    private int column;

    public ColumnRowIterator(Matrix matrix) {
        this.matrix = matrix;
        row = 0;
        column = -1;
    }

    @Override
    public boolean hasNext() {
        return this.column != matrix.getNumColumns() - 1 || this.row != matrix.getNumRows() - 1;
    }

    @Override
    public Integer next() throws NoSuchElementException {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException("There's no more elements");
        }
        if ( this.column == matrix.getNumColumns() - 1 ) {
            this.column = 0;
            this.row++;
        } else {
            this.column++;
        }

        return this.matrix.matrix[row][column];
    }
}
