import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class MatrixUtilsTest {

    @Test
    public void testAccumulateVertical() {
        double[][] A = {{1000000,   1000000,   1000000,   1000000},
            {1000000,     75990,     30003,   1000000},
            {1000000,     30002,    103046,   1000000},
            {1000000,     29515,     38273,   1000000},
            {1000000,     73403,     35399,   1000000},
            {1000000,   1000000,   1000000,   1000000}};


        double[][] newA = {{1000000,   1000000,   1000000,   1000000},
            {2000000,   1075990,   1030003,   2000000},
            {2075990,   1060005,   1133049,   2030003},
            {2060005,   1089520,   1098278,   2133049},
            {2089520,   1162923,   1124919,   2098278},
            {2162923,   2124919,   2124919,   2124919}};

        assertArrayEquals(newA, MatrixUtils.accumulateVertical(A));
    }


    @Test
    public void testMakeVertical() {
        double[][] B = {{10, 10, 10, 10},
            {10, 8, 5, 10},
            {10, 6, 3, 10},
            {10, 10, 10, 10}};


        double[][] newB = {{10, 20, 28, 31},
            {10, 18, 21, 29},
            {10, 16, 19, 29},
            {10, 20, 26, 29}};


        assertArrayEquals(newB, MatrixUtils.accumulate(B, MatrixUtils.Orientation.HORIZONTAL));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}

