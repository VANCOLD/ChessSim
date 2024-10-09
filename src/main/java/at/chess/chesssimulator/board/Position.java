package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Position {

    private ChessPiece piece;

    private final int row;
    private final int col;

    public Position(int row, int col, ChessPiece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    public Position(int row, int col) {
        this(row,col,null);
    }

    @Override
    public String toString() {
        String piece = this.piece != null ? "" + this.piece.getType().name().charAt(0) : "";
        return piece + ((char)('a' + row)) + (col + 1);
    }
}