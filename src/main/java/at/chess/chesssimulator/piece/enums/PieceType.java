package at.chess.chesssimulator.piece.enums;

/**
 * Simple representation of chess pieces. Used for IO tasks and object generation
 */
public enum PieceType {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

    /**
     * Maps a character to a Piece; Used to load scenarios and default layout.
     * n = knight
     * b = bishop
     * p = pawn
     * r = rook
     * q = queen
     * k = king
     *
     * @param type char; returns the corresponding PieceType for a character
     * @return returns the PieceType corresponding to a character; Throws an error if a wrong character is supplied
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
