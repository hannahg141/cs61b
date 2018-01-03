package qirkat;


/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author hannahgrossman
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking itsm moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + ": ";
    }

    /** Return a legal move for me. Assumes that
     *  board.whoseMove() == myColor and that !board.gameOver(). */
    @Override
    Move myMove() {
        Move mov = null;
        Command first = game().getMoveCmnd(_prompt);

        if (first != null) {
            mov = Move.parseMove(first.operands()[0]);
            if (!board().legalMove(mov)) {
                System.out.print("Illegal Move.");
                mov = null;
            }
        }

        return mov;
    }

    /** Identifies the player serving as a source of input commands. */
    private String _prompt;
}

