package at.chess.chesssimulator.piece.enums;

/**
 * Enum representing the color of a chess piece, either {@link #BLACK} or {@link #WHITE}.
 */
public enum PieceColor {

    /** The black color for chess pieces. */
    BLACK,

    /** The white color for chess pieces. */
    WHITE;

    /**
     * Returns the corresponding {@code PieceColor} based on the provided integer value.
     * <p>
     * The integer values are mapped as follows:
     * <ul>
     *   <li>0 - {@link #BLACK}</li>
     *   <li>1 - {@link #WHITE}</li>
     * </ul>
     * </p>
     *
     * @param color An integer representing the piece color, where 0 represents black and 1 represents white.
     * @return The corresponding {@code PieceColor}.
     * @throws IllegalStateException if the provided value is not 0 or 1.
     */
    public static PieceColor getPieceColor(int color) {
        return switch(color) {
            case 0 -> BLACK;
            case 1 -> WHITE;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }

    public static PieceColor getOppositeColor(PieceColor color) {
        return switch(color) {
            case BLACK -> WHITE;
            case WHITE -> BLACK;
        };
    }
}
