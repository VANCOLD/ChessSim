package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
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

    /** Defines if a Position is occupied by a chess piece */
    private boolean occupied;

    /** Defines if a Position is selected (ui only) */
    private boolean selected;

    /** Defines if a Position should show an indicator (ui only) */
    private boolean indicator;

    /** Defines if a King (inside the position) is in check */
    private boolean inCheck;

    /**e
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
        this.occupied = piece != null;
        this.selected = false;
        this.indicator = false;
        this.inCheck = false;
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
        return (char)('a' + row) + "" + (col + 1);
    }

    /**
     * Checks if the position contains a chess piece.
     * <p>
     *     This method returns {@code true} if the position contains a chess piece, and {@code false} otherwise.
     *     A position is considered to contain a piece if the {@link #piece} field is not {@code null}.
     *     This method is useful for checking if a position is empty or occupied.
     * </p>
     *
     * @return {@code true} if the position contains a chess piece; {@code false} otherwise.
     */
    public boolean containsPiece() {
        return piece != null;
    }

    /**
     * Returns the color of the chess piece occupying this position.
     * <p>
     *     If the position is empty, this method returns {@code null}.
     *     Otherwise, it returns the color of the chess piece occupying the position.
     *     This method is useful for determining the color of a piece on the board.
     * </p>
     *
     * @return The color of the chess piece occupying this position, or {@code null} if the position is empty.
     */
    public PieceColor getColor() {

        if(piece == null) {
            return null;
        }
        return piece.getColor();
    }
}