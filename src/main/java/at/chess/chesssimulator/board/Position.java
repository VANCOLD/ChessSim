package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a specific position on the chessboard, defined by its row and column.
 * <p>
 * This class also holds an optional reference to a {@link ChessPiece} if one occupies the position.
 * </p>
 */
@Setter
@Getter
public class Position {

    /** The chess piece that occupies this position, or {@code null} if the position is empty. */
    private ChessPiece piece;

    /** The row index of the position on the chessboard (0-based). */
    private final int row;

    /** The column index of the position on the chessboard (0-based). */
    private final int col;

    /**
     * Constructs a position with a specified row, column, and an optional chess piece.
     *
     * @param row   The row index of the position.
     * @param col   The column index of the position.
     * @param piece The chess piece occupying the position, or {@code null} if the position is empty.
     */
    public Position(int row, int col, ChessPiece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    /**
     * Constructs a position with a specified row and column, without any chess piece.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     */
    public Position(int row, int col) {
        this(row, col, null);
    }

    /**
     * Returns a string representation of the position.
     * <p>
     * The format of the string is as follows:
     * <ul>
     *   <li>If the position contains a piece, the first character will be the piece's type (e.g., 'K' for King).</li>
     *   <li>The second character is the file (column) in algebraic notation (e.g., 'a', 'b', etc.).</li>
     *   <li>The third character is the rank (row) in standard 1-based chessboard notation.</li>
     * </ul>
     * </p>
     *
     * @return A string representation of the position in chess notation.
     */
    @Override
    public String toString() {
        String piece = this.piece != null ? "" + this.piece.getType().name().charAt(0) : "";
        return piece + ((char)('a' + row)) + (col + 1);
    }
}
