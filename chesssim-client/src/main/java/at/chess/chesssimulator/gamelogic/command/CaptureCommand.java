package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class CaptureCommand implements Command {
    private final ChessBoard chessBoard;
    private final Move move;

    public CaptureCommand(ChessBoard chessBoard, Move move) {
        this.chessBoard = chessBoard;
        this.move = move;
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