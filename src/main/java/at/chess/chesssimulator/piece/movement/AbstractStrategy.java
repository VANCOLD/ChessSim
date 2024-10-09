package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;

public abstract class AbstractStrategy implements MovementStrategy {

    protected final ChessBoard chessBoard;

    public AbstractStrategy() {
        this.chessBoard = ChessBoard.getInstance();
    }
}
