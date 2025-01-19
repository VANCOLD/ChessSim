package at.chess.chesssimulator.sound;

import at.chess.chesssimulator.board.enums.MoveType;

public enum SoundType {
    GAME_START,
    MOVE,
    CAPTURE,
    PROMOTE,
    CASTLE,
    CHECK,
    CHECKMATE;

    public static SoundType getSound(MoveType moveType) {
        return switch(moveType) {
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
