
public class IntDList {

    protected DNode _front, _back;

    public IntDList() {
        _front = _back = null;
    }

    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /** Returns the first item in the IntDList. */
    public int getFront() {
        return _front._val;
    }

    /** Returns the last item in the IntDList. */
    public int getBack() {
        return _back._val;
    }

    /** Return value #I in this list, where item 0 is the first, 1 is the
     *  second, ...., -1 is the last, -2 the second to last.... */
    public int get(int i) {
        DNode pointer = null;
        if (i < 0){
            pointer = _back;
            while (i != -1 && pointer._prev != null){
                i += 1;
                pointer = pointer._prev;
            }
        }
        if (i >=0){
            pointer = _front;
            while (i != 0 && pointer._next != null) {
                pointer = pointer._next;
                i -= 1;
            }

        }

        return pointer._val;

    }

    /** The length of this list. */
    public int size() {

        DNode pointer = _front;
        int total = 0;
        while (pointer != null){
            total += 1;
            pointer = pointer._next;
        }
        return total;

    }


    /** Adds D to the front of the IntDList. */
    public void insertFront(int d) {
        DNode inserted = new DNode(d);
        if (_front == null && _back == null){
            _front = inserted;
            _back = inserted;
        }
        else {
            _front._prev = inserted;
            inserted._next = _front;
            _front = inserted;
        }


    }

    /** Adds D to the back of the IntDList. */
    public void insertBack(int d) {
        DNode insert = new DNode(d);
        if (_front == null && _back == null){
            _front = insert;
            _back = insert;
        }
        else {
            _back._next = insert;
            insert._prev = _back;
            _back = insert;
        }



    }

    /** Removes the last item in the IntDList and returns it.
     * This is an extra challenge problem. */
    public int deleteBack() {
        return 0;   // Your code here

    }

    /** Returns a string representation of the IntDList in the form
     *  [] (empty list) or [1, 2], etc. 
     * This is an extra challenge problem. */
    public String toString() {
        return null;   // Your code here
    }

    /* DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information! */
    protected static class DNode {
        protected DNode _prev;
        protected DNode _next;
        protected int _val;

        private DNode(int val) {

            this(null, val, null);
        }

        private DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
