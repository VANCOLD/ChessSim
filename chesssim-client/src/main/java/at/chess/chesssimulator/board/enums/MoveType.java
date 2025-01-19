package at.chess.chesssimulator.board.enums;

import lombok.Getter;

/**
 * Enum representing the different types of moves in a chess game.
 * Each move type corresponds to a specific notation used in chess games.
 *
 * <p>This enum includes the following move types:</p>
 * <ul>
 *   <li><b>MOVE</b>: A regular move, represented by an empty string ("").</li>
 *   <li><b>CAPTURE</b>: A capture move, represented by "x".</li>
 *   <li><b>CHECK</b>: A move that places the opponent's king in check, represented by "+".</li>
 *   <li><b>KCASTLING</b>: Kingside castling, represented by "0-0".</li>
 *   <li><b>QCASTLING</b>: Queenside castling, represented by "0-0-0".</li>
 *   <li><b>PROMOTE</b>: Pawn promotion, represented by "/".</li>
 *   <li><b>INVALID</b>: An invalid move, represented by an empty string ("").</li>
 *   <li><b>CHECKMATE</b>: A move that places the opponent's king in checkmate, represented by "#".</li>
 * </ul>
 *
 * <p>The <b>notation</b> field contains the standard chess notation for the corresponding move type.</p>
 *
 * Example:
 * <pre>
 *   MoveType move = MoveType.CAPTURE;
 *   System.out.println(move.getNotation()); // Output: "x"
 * </pre>
 */
@Getter
public enum MoveType {
    /** A regular move (no special action), represented by an empty string (""). */
    MOVE(""),

    /** A capture move, represented by "x". */
    CAPTURE("x"),

    /** A move that places the opponent's king in check, represented by "+". */
    CHECK("+"),

    /** Kingside castling, represented by "0-0". */
    KCASTLING("0-0"),

    /** Queenside castling, represented by "0-0-0". */
    QCASTLING("0-0-0"),

    /** Pawn promotion, represented by "/". */
    PROMOTE("/"),

    /** An invalid move, represented by an empty string (""). */
    INVALID(""),

    /** A move that places the opponent's king in checkmate, represented by "#". */
    CHECKMATE("#");

    /** The notation associated with the move type. */
    private final String notation;

    /**
     * Constructor to initialize the move type with its corresponding notation.
     *
     * @param notation The chess notation for the move type.
     */
    MoveType(String notation) {
        this.notation = notation;
    }
}
