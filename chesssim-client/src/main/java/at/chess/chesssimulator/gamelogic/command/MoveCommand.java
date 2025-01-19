package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class MoveCommand extends AbstractCommand {

    public MoveCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {
        chessBoard.movePiece(move.getOriginalPosition(), move.getNewPosition());
    }

    @Override
    public void undo() {
        chessBoard.movePiece(move.getNewPosition(), move.getOriginalPosition());
    }
}