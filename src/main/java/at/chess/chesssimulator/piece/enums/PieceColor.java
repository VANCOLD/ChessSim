package at.chess.chesssimulator.piece.enums;

/**
 * Simple representation of colors of pieces. Used for IO tasks and object generation
 */
public enum PieceColor {
    BLACK, WHITE;

    /**
     * Maps an integer to a Color; Used to load scenarios and default layout.
     * 0 = black
     * 1 = white
     *
     * @param color int; a value between 0-1 (int)
     * @return Returns either black (0) or white (1), if any other number is given, this method will throw an error
     */
    public static PieceColor getPieceColor(int color) {
        return switch(color) {
            case 0 -> BLACK;
            case 1 -> WHITE;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }
}
