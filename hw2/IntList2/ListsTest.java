import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author hannahgrossman
 */

public class ListsTest {
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }

    @Test
    public void testNaturalRuns() {
        IntList one = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        int[][] result = new int[][] {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        IntList2 resultTwo = IntList2.list(result);
        IntList mine = IntList.list(3, 2, 1);
        int[][] result2 = new int[][] {{3}, {2}, {1}};
        IntList2 result2Two = IntList2.list(result2);


        assertEquals(resultTwo, Lists.naturalRuns(one));
        assertEquals(result2Two, Lists.naturalRuns(mine));
    }


}




