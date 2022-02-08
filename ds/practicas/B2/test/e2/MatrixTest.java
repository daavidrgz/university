package e2;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    @Test
    public void MatrixBuilder() {
        Matrix matrix1 = new Matrix(2, 3);
        Matrix matrix4 = new Matrix(new int[][] {{5, 3}, {1, 2}});

        assertThrows(IllegalArgumentException.class, () -> new Matrix(new int[][] {{2,7,8}, {1,2}}));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(null));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(new int[][] {null, {1}}));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(0, 2));

    }

    @Test
    public void GetRowsAndColumns() {
        Matrix matrix1 = new Matrix(2, 3);

        assertEquals(3, matrix1.getNumColumns());
        assertEquals(2, matrix1.getNumRows());
        matrix1.insertNum(5, 0, 0);
        matrix1.insertNum(1, 1, 0);
        assertArrayEquals(new int[] {5, 0, 0}, matrix1.getRow(0));
        assertArrayEquals(new int[] {5, 1}, matrix1.getColumn(0));
        assertThrows(IllegalArgumentException.class, () -> matrix1.getRow(5));
        assertThrows(IllegalArgumentException.class, () -> matrix1.getColumn(3));
    }

    @Test
    public void Insert() {
        Matrix matrix2 = new Matrix(2, 3);

        matrix2.insertNum(3, 0, 0);
        matrix2.insertNum(1, 0, 1);
        matrix2.insertNum(5, 0, 2);
        matrix2.insertNum(4, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> matrix2.insertNum(5, 1, -1));
        assertThrows(IllegalArgumentException.class, () -> matrix2.insertNum(5, 2, 1));

        assertEquals("[3, 1, 5]\n[0, 4, 0]\n", matrix2.toString());
    }

    @Test
    public void GetNumsAndMatrix() {
        Matrix matrix4 = new Matrix(new int[][] {{5, 3}, {1, 2}});

        assertEquals(5, matrix4.getNum(0, 0));
        assertEquals(2, matrix4.getNum(1, 1));
        assertThrows(IllegalArgumentException.class, () -> matrix4.getNum(2, 0));
        assertThrows(IllegalArgumentException.class, () -> matrix4.getNum(1, -1));

        assertArrayEquals(new int[][] {{5, 3}, {1, 2}}, matrix4.getMatrix());
    }

    @Test
    public void Addition() {
        Matrix matrix1 = new Matrix(new int[][] {{2, 0, 3}, {1, 2, 0}});
        Matrix matrix2 = new Matrix(new int[][] {{6, 1, 2}, {0, 2, 0}});

        assertEquals("[8, 1, 5]\n[1, 4, 0]\n", MatrixAddition.add(matrix1, matrix2).toString());

        Matrix matrix4 = new Matrix(new int[][] {{5, 3}, {1, 2}});
        Matrix matrix5 = new Matrix(new int[][] {{0, 1}, {2, 4}});
        matrix4.setIteratorType(false);
        matrix5.setIteratorType(false);

        assertEquals("[5, 4]\n[3, 6]\n", MatrixAddition.add(matrix4, matrix5).toString());

        assertThrows(ArithmeticException.class, () -> MatrixAddition.add(matrix2, matrix4));
    }

    @Test
    public void IteratorTest() {
        Matrix matrix1 = new Matrix(new int[][] {{2, 0, 3}, {1, 2, 0}});

        assertTrue(matrix1.getIteratorType());

        Iterator<Integer> it = matrix1.iterator();

        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertEquals(0, it.next());
        assertEquals(3, it.next());
        assertEquals(1, it.next());
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
        assertThrows(NoSuchElementException.class, it::next);

        matrix1.setIteratorType(false);
        assertFalse(matrix1.getIteratorType());
    }
}
