package at.chess.chesssimulator.board.enums;

import lombok.Getter;

@Getter
public enum MoveType {
    MOVE(""), CAPTURE("x"), CHECK("+"), KCASTELING("0-0"), QCASTELING("0-0-0"),
    PROMOTE("/"), WHITE_WIN("1-0"), BLACK_WIN("0-1"), INVALID("");

    private final String notation;

    MoveType(String notation) {
        this.notation = notation;
    }
}
