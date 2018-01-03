/** my class for an empty list.
 * @author hannahgrossman
 */
public class EmptyWeirdList extends WeirdList {

    /**constructor.
     *
     */
    public EmptyWeirdList() {
        super(0, null);
    }

    /** returns me the length.
     *
     * @return the length
     */
    public int length() {
        return 0;
    }

    /** the words.
     *
     * @return a string that I want of my values
     */
    public String toString() {
        return "";
    }

    /** applys the function.
     *
     * @param func **the input function
     * @return the result
     */
    public WeirdList map(IntUnaryFunction func) {
        return new EmptyWeirdList();
    }

    /**prints.
     *
     */
    public void print() {


    }
}
