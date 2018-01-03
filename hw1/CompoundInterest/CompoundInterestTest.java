import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {


//does num years obey the specifications given in copmpuntinterest
    //use 2 assertEquals
    //num years returns an int
    @Test
    public void testNumYears() {
        // Sample assert statement for comparing integers.
        assertEquals(1, CompoundInterest.numYears(2016));
        assertEquals(3, CompoundInterest.numYears(2018));
        
    }


    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12,2017), tolerance);
        assertEquals(138.787996893, CompoundInterest.futureValue(19, 22, 2025), tolerance);
        assertEquals(78974.6956799, CompoundInterest.futureValue(100, 10, 2085), tolerance);
    }


    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2017, 3), tolerance);
        assertEquals(83.0975004627, CompoundInterest.futureValueReal(19, 22, 2025, 5), tolerance);
        assertEquals(9002277.55954, CompoundInterest.futureValueReal(100,
                10, 2085, -7), tolerance);
    }
/** Suppose you invest PERYEAR dollars at the end of every year until
     *  TARGETYEAR, with growth compounded annually at RATE. This method
     *  returns the total value of your savings in TARGETYEAR.
     *
     *  For example, if PERYEAR is 5000, TARGETYEAR is 2017, and RATE is 10,
     *  then the result will be 5000*1.1*1.1 + 5000*1.1 + 5000 =
     *  16550. */

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2017, 10), tolerance);
        assertEquals(9500, CompoundInterest.totalSavings(5000, 2016, -10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(13405.5, CompoundInterest.totalSavingsReal(5000, 2017, 10, 10), tolerance);
        assertEquals(8550, CompoundInterest.totalSavingsReal(5000, 2016, -10, 10), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
