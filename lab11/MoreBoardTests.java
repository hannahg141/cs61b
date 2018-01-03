
public class MoreBoardTests {
	
	// the string representation of this is
	// "  b b b b b\n  b b b b b\n  b b - w w\n  w w w w w\n  w w w w w"
	// feel free to modify this to however you want to represent your board.
    private final char[][] boardRepr = new char[][]{
        {'b', 'b', 'b', 'b', 'b'},
        {'b', 'b', 'b', 'b', 'b'},
        {'b', 'b', '-', 'w', 'w'},
        {'w', 'w', 'w', 'w', 'w'},
        {'w', 'w', 'w', 'w', 'w'}
    };

    private final PieceColor currMove = PieceColor.WHITE;

    /**
     * @return the String representation of the initial state. This will
     * be a string in which we concatenate the values from the bottom of 
     * board upwards, so we can pass it into setPieces. Read the comments
     * in Board#setPieces for more information.
     * 
     * For our current boardRepr, the String returned by getInitialRepresentation is
     * "  w w w w w\n  w w w w w\n  b b - w w\n  b b b b b\n  b b b b b"
     *
     * We use a StringBuilder to avoid recreating Strings (because Strings
     * are immutable).
     */
    private String getInitialRepresentation() {
    	StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = boardRepr.length - 1; i >= 0; i--) {
            for (int j = 0; j < boardRepr[0].length; j++) {
                sb.append(boardRepr[i][j] + " ");
            }
            sb.deleteCharAt(sb.length() - 1);
            if (i != 0) {
                sb.append("\n  ");
            }
        }
        return sb.toString();
    }

    // create a new board with the initial state.
    private Board getBoard() {
    	Board b = new Board();
    	b.setPieces(getInitialRepresentation(), currMove);
    	return b;
    }

    // reset board b to initial state.
    private void resetToInitialState(Board b) {
    	b.setPieces(getInitialRepresentation(), currMove);
    }

    @Test
    public void testSomething() {
    	Board b = getBoard();
    	// write things to test here
    }
    @Test
    public void testLegalMove() {
        Board b0 = new Board();
        assertTrue(b0.legalMove(move1));
        assertFalse(b0.legalMove(move2));

    }

    @Test
    public void testInternalCopy() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        assertEquals(b0, b1);
    }
    private static Move moveA = Move.parseMove("c2-c3");
    private static Move moveB = Move.parseMove("c4-c2");
    private static Move moveC = Move.parseMove("d3-c3");

    @Test
    public void testIntegration() {
        Board b0 = new Board();
        b0.makeMove(moveA);
        b0.makeMove(moveB);

        assertFalse(b0.legalMove(moveC));



    }

}