package at.chess.chesssimulator.piece.enums;

/**
 * Enum representing the different types of chess pieces.
 * <p>
 * The types include:
 * <ul>
 *   <li>{@link #PAWN}</li>
 *   <li>{@link #ROOK}</li>
 *   <li>{@link #KNIGHT}</li>
 *   <li>{@link #BISHOP}</li>
 *   <li>{@link #QUEEN}</li>
 *   <li>{@link #KING}</li>
 * </ul>
 * </p>
 */
public enum PieceType {

    /** Represents a pawn chess piece. */
    PAWN,

    /** Represents a rook chess piece. */
    ROOK,

    /** Represents a knight chess piece. */
    KNIGHT,

    /** Represents a bishop chess piece. */
    BISHOP,

    /** Represents a queen chess piece. */
    QUEEN,

    /** Represents a king chess piece. */
    KING;

    /**
     * Returns the corresponding {@code PieceType} based on the provided character representation.
     * <p>
     * The character values are mapped as follows:
     * <ul>
     *   <li>'p' - {@link #PAWN}</li>
     *   <li>'r' - {@link #ROOK}</li>
     *   <li>'n' - {@link #KNIGHT}</li>
     *   <li>'b' - {@link #BISHOP}</li>
     *   <li>'q' - {@link #QUEEN}</li>
     *   <li>'k' - {@link #KING}</li>
     * </ul>
     * </p>
     *
     * @param type A character representing the type of the chess piece.
     * @return The corresponding {@code PieceType}.
     * @throws IllegalStateException if the provided character does not match any piece type.
     */
    public static PieceType getPieceType(char type) {
        return switch(type) {
            case 'n' -> KNIGHT;
            case 'b' -> BISHOP;
            case 'p' -> PAWN;
            case 'r' -> ROOK;
            case 'q' -> QUEEN;
            case 'k' -> KING;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}