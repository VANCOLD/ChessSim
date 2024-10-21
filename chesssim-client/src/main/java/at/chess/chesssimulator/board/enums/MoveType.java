package at.chess.chesssimulator.board.enums;

import lombok.Getter;

@Getter
public enum MoveType {
    DEFAULT(""), CAPTURE("x"), CHECK("+"), KCASTELING("0-0"), QCASTELING("0-0-0"),
    PROMOTION("/"), WHITE_WIN("1-0"), BLACK_WON("0-1");

    private final String notation;

    MoveType(String notation) {
        this.notation = notation;
    }
}
