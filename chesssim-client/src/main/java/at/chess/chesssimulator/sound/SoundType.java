package at.chess.chesssimulator.sound;

import at.chess.chesssimulator.board.enums.MoveType;

public enum SoundType {
    MOVE,
    CAPTURE,
    PROMOTE,
    CASTLE,
    CHECKMATE;

    public static SoundType getSound(MoveType moveType) {
        return switch(moveType) {
            case MOVE -> MOVE;
            case CAPTURE -> CAPTURE;
            case PROMOTE -> PROMOTE;
            case QCASTELING, KCASTELING -> CASTLE;
            case CHECK -> CHECKMATE;
            case WHITE_WIN -> null;
            case BLACK_WIN -> null;
            case INVALID -> null;
        };
    }
}
