package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class WinCommand extends AbstractCommand {

    public WinCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
