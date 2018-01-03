import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

public class ArrayHeapTest {

    /** Basic test of adding, checking, and removing two elements from a heap */
    @Test
    public void simpleTest() {
        ArrayHeap<String> pq = new ArrayHeap<>();
        pq.insert("Qir", 2);
        pq.insert("Kat", 1);
        assertEquals(2, pq.size());

        String first = pq.removeMin();
        assertEquals("Kat", first);
        assertEquals(1, pq.size());

        String second = pq.removeMin();
        assertEquals("Qir", second);
        assertEquals(0, pq.size());
    }



    @Test
    public void testPeek() {
        ArrayHeap<String> pq = new ArrayHeap<>();
        pq.insert("Qir", 2);
        pq.insert("Kat", 1);
        ArrayHeap.Node peeked = pq.peek();
        assertEquals("Kat", peeked.item());
        pq.removeMin();
        ArrayHeap.Node peeked2 = pq.peek();
        assertEquals("Qir", peeked2.item());
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArrayHeapTest.class));
    }
}
