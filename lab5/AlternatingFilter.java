import java.util.Iterator;
import java.util.List;

import utils.Filter;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author hannahgrossman
 *
 *  is doIT like a global variable?
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that lets through every other
     *  value.
     *
     *  start at False, so the first time you can switch it to true
     *  and include the value, then it switches back to false and skip*/
    AlternatingFilter(Iterator<Value> input) {
        super(input);
        doIt = false;




    }

    @Override
    protected boolean keep() {
        doIt = !doIt;
        return doIt;

    }

    private boolean doIt;

}
