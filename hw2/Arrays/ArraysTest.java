import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author hannahgrossman
 */

public class ArraysTest {

    @Test
    public void testCatenate() {
        int[] one = {1, 2, 3};
        int[] two = {4, 5, 6, 7, 8, 9};
        int[] empty = {};
        int[] result = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertArrayEquals(result, Arrays.catenate(one, two));
        assertArrayEquals(two, Arrays.catenate(two, empty));
    }

    @Test
    public void testRemove() {
        int [] first = {0, 1, 2, 3, 4, 5};
        int [] firstResult = {0, 4, 5};
        int [] nothing = {};

        assertArrayEquals(firstResult, Arrays.remove(first, 1, 3));
        assertArrayEquals(first, Arrays.remove(first, 3, 0));
        assertArrayEquals(nothing, Arrays.remove(nothing, 3, 10));
    }

    @Test
    public void testNaturalRuns() {
        int[] A = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] B = {{1, 3, 7}, {5}, {4, 6, 9, 10}};

        assertArrayEquals(B, Arrays.naturalRuns(A));
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
