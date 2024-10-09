package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStrategy implements MovementStrategy {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractStrategy.class);

    protected final ChessBoard chessBoard;

    public AbstractStrategy() {
        this.chessBoard = ChessBoard.getInstance();
    }
}
