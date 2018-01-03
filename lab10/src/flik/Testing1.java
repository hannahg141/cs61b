/** testing for this code
 * @author hannahgrossman
 */
package flik;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class Testing1 {

    @Test
    public void isSameNumberTest() {
        assertTrue(Flik.isSameNumber(2, 2));
        assertFalse(Flik.isSameNumber(1, 200));
    }


}