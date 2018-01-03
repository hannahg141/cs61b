/* NOTE: The file ArrayUtil.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author hannahgrossman
 */
class Arrays {
    /* C. */

    /**
     * Returns a new array consisting of the elements of A followed by the
     * the elements of B.
     */
    static int[] catenate(int[] A, int[] B) {
        int[] C = new int[A.length + B.length];
        int i = 0;
        int j = 0;
        if (A == null) {
            C = B;
        }
        else if (B == null) {
            C = A;
        }
        while (i < A.length) {
            C[i] = A[i];
            i += 1;
        }
        while (j < B.length) {
            C[i] = B[j];
            j += 1;
            i += 1;
        }
        return C;
    }

    /**
     * Returns the array formed by removing LEN items from A,
     * beginning with item #START.
     */
    static int[] remove(int[] A, int start, int len) {
        if (A.length == 0 || len <= 0) {
            return A;
        }

        int[] result = new int[A.length - len];
        System.arraycopy(A, 0, result, 0, start - 1);
        System.arraycopy(A, len + 1, result, start, A.length - len - 1);
        return result;
    }



    /* E. */

    /**
     * Returns the array of arrays formed by breaking up A into
     * maximal ascending lists, without reordering.
     * For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     * returns the three-element array
     * {{1, 3, 7}, {5}, {4, 6, 9, 10}}.
     */
    static int[][] naturalRuns(int[] A) {
        return finalNaturalRuns(A, 0, 0);

    }


    /**
     * Counting the first level of my matrix.
     *
     * @return the number of numbers of increasing values (1, 3, 7)
     * therefore 3.
     * keeping track of i (doNext) values to start at, we aren't changing A
     * if count grows, we need to know where we started, subtract i
     * @param matrix ** A **
     * @param start **where i begin**
     */
    static int helpCount(int[] matrix, int start) {
        /*start is the position that I START comparing values at
        * count is doing this work
        * important: want NUMBER of numbers, not how far along count is*/
        int count = start + 1;
        while (count < matrix.length && matrix[count] > matrix[count - 1]) {
            count += 1;
        }
        return count - start;
    }


    /** building my final matrix.
     *
     * @param matrix is the matrix (A) I refer to
     * @param index keeps track of where I am within my NEW MATRIX
     * @param doNext is where to start storing natural runs in next recursion
     * @return final, the new matrix
     * calling subarray that builds me a matrix from the parameters I give it.
     * numInts is # ints/ "mini-matrix,"
     * where I start search in next recursive call?
     */
    static int[][] finalNaturalRuns(int[] matrix, int index, int doNext) {
        if (matrix.length <= doNext) {
            return new int[index][];
        }
        int numInts = helpCount(matrix, doNext);
        int[][] last = finalNaturalRuns(matrix, index + 1, doNext + numInts);
        int[] miniArray = Utils.subarray(matrix, doNext, numInts);
        last[index] = miniArray;
        return last;
    }

}


