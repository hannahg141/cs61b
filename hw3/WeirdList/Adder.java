/** this function is a uniary function make adder.
 * @author hannahgrossman
 *
 */
public class Adder implements IntUnaryFunction {
    /** my main variable.
     *
     */
    private int _n;

    /** constructor.
     *
     * @param n * input
     */
    public Adder(int n) {
        _n = n;
    }

    /** apply.
     *
     * @param x *input value
     * @return the result of applying.
     */
    public int apply(int x) {
        return x + _n;
    }
}

