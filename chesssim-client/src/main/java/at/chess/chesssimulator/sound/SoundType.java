package at.chess.chesssimulator.sound;

import at.chess.chesssimulator.board.enums.MoveType;

/**
 * Enum representing the different types of sounds that can be played in the chess simulator.
 */
public enum SoundType {
    /**
     * Sound for the start of the game.
     */
    GAME_START,

    /**
     * Sound for a regular move.
     */
    MOVE,

    /**
     * Sound for capturing a piece.
     */
    CAPTURE,

    /**
     * Sound for promoting a pawn.
     */
    PROMOTE,

    /**
     * Sound for castling (both king-side and queen-side).
     */
    CASTLE,

    /**
     * Sound for putting the opponent in check.
     */
    CHECK,

    /**
     * Sound for delivering checkmate.
     */
    CHECKMATE;

    /**
     * Maps a {@link MoveType} to its corresponding {@code SoundType}.
     *
     * @param moveType The type of move to map.
     * @return The corresponding {@code SoundType}, or {@code null} if the move type is {@code INVALID}.
     */
    public static SoundType getSound(MoveType moveType) {
        return switch (moveType) {
            case MOVE -> MOVE;
            case CAPTURE -> CAPTURE;
            case PROMOTE -> PROMOTE;
            case QCASTLING, KCASTLING -> CASTLE;
            case CHECK -> CHECK;
            case CHECKMATE -> CHECKMATE;
            case INVALID -> null;
        };
    }
}
