package at.chess.chesssimulator.gamelogic.command;


import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class CaptureCommand extends AbstractCommand {

    public CaptureCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {
        chessBoard.capturePiece(move.getOriginalPosition(), move.getNewPosition());
    }

    @Override
    public void undo() {
        chessBoard.movePiece(move.getNewPosition(), move.getOriginalPosition());
        chessBoard.placePiece(move.getNewPosition(), move.getNewPosition().getPiece());
    }
}