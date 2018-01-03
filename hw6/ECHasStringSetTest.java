
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
class ECHashStringSetTest {

    ECHashStringSet mine =  new ECHashStringSet();

    @Test
    public void putSizeTest() {
        mine.put("Hannah");
        assertEquals(1, mine.size());
        mine.put("Is");
        assertEquals(2, mine.size());
        mine.put("The");
        assertEquals(3, mine.size());
        mine.put("Best");
        assertEquals(4, mine.size());
        mine.put("Hannah");
        assertEquals(5, mine.size());
        assertTrue(mine.contains("Hannah"));
        assertFalse(mine.contains("Sucks"));
        System.out.print(mine);
    }
}
