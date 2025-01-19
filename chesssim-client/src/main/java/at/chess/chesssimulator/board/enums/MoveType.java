package at.chess.chesssimulator.board.enums;

import lombok.Getter;

@Getter
public enum MoveType {
    MOVE(""), CAPTURE("x"), CHECK("+"), KCASTLING("0-0"), QCASTLING("0-0-0"),
    PROMOTE("/"), INVALID(""), CHECKMATE("#");

    private final String notation;

    MoveType(String notation) {
        this.notation = notation;
    }
}
