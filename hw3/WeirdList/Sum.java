/** my summing class.
 * @author hannahgrossman
 */
public class Sum implements IntUnaryFunction {
    /** stores the total value of my total sum.*/
    private int total = 0;

    /** how far am i in summing thus far.
     * @param thisFar **mid summing result
     */
    public Sum(int thisFar) {
        total = thisFar;
    }

    /** does the summing.
     * @param x ** initial input.
     * @return the summed result.
     */
    public int apply(int x) {
        total += x;
        return total;
    }

    /** final value.
     * @return result of summing
     */
    public int result() {
        return total;
    }


}

