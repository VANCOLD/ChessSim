package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractCommand implements Command{

    protected static final Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    protected final ChessBoard chessBoard;
    protected final Move move;

    public AbstractCommand(ChessBoard chessBoard, Move move) {
        this.chessBoard = chessBoard;
        this.move = move;
    }

    @Override
    public void execute() {
        // stub, overwrite this in implementation
    }

    @Override
    public void undo() {
        // stub, overwrite this in implementation
    }

    @Override
    public String toString() {
        return move.toString();
    }
}
