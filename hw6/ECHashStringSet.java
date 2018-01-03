
import java.util.LinkedList;


/** A set of String values.
 *  @author hannahgrossman
 */
@SuppressWarnings("unchecked")
class ECHashStringSet extends BSTStringSet {

    /** max load factor.*/
    private static double maxLoad = 5;

    /** min load factor*/
    private static double minLoad = 0.2;

    /** my buckets of data. */
    private LinkedList<String>[] buckets;
    /**the amount of items in my buckets of data. */
    private int _size;

    /**constructor.*/
    public ECHashStringSet() {
        _size = 0;
        buckets = new LinkedList[10];
    }

     /** @return _size of my list.
     */
    public int size() {
        return _size;
    }


    /** Puts s in list if not already there.
     * @param s the string to be added to my list.
     */
    public void put(String s) {
        if (size() > buckets.length * maxLoad) {
            resize();
        }
        int code = s.hashCode() & 0x7fffffff;
        int theBucket = code % buckets.length;
        LinkedList<String> bucket = buckets[theBucket];
        if (bucket == null) {
            bucket = new LinkedList<String>();
            bucket.add(s);
            _size++;
        } else if (!bucket.contains(s)) {
            bucket.add(s);
            _size++;
        }

    }

    /** checks if S is in the list.
     * @param s in list?
     * @return if s is in the list.
     */
    public boolean contains(String s) {
        int code = s.hashCode() & 0x7fffffff;
        code = code % buckets.length;
        LinkedList<String> bucket = buckets[code];
        return (bucket.contains(s));


    }

    /** Doubles the size of my list.
     */
    public void resize() {
        LinkedList<String>[] oldBucket = buckets;
        buckets = new LinkedList[2 * oldBucket.length];
        _size = 0;
        for (LinkedList<String> items : oldBucket) {
            if (items != null) {
                for (String s : items) {
                    this.put(s);
                }
            }

        }

    }
}

