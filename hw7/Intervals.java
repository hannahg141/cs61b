import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Arrays;

/** HW #8, Problem 3.
 *  @author hannahgrossman
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort((start, end) -> start[0] - end[0]);
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        int distance = 0;
        for (int i = 0; i < intervals.size(); i++) {
            if (intervals.get(i)[0] > second) {
                distance += second - first;
                first = intervals.get(i)[0];
                second = intervals.get(i)[1];
            } else if (intervals.get(i)[0] <= second) {
                if (intervals.get(i)[1] > second) {
                    second = intervals.get(i)[1];
                }
            }
        }
        distance += second - first;
        return distance;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
