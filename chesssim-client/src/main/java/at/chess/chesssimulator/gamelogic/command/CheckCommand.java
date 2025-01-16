package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class CheckCommand extends AbstractCommand{

    public CheckCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
