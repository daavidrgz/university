package e2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RowColumnIterator implements Iterator<Integer> {
    private final Matrix matrix;
    private int row;
    private int column;

    public RowColumnIterator(Matrix matrix) {
        this.matrix = matrix;
        row = -1;
        column = 0;
    }

    @Override
    public boolean hasNext() {
        return this.row != matrix.getNumRows() - 1 || this.column != matrix.getNumColumns() - 1;
    }

    @Override
    public Integer next() throws NoSuchElementException {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException("There's no more elements");
        }
        if ( this.row == matrix.getNumRows() - 1 ) {
            this.row = 0;
            this.column++;
        } else {
            this.row++;
        }

        return this.matrix.matrix[row][column];
    }
}
