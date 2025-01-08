package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;

public class MoveCommand implements Command {
    private final ChessBoard chessBoard;
    private final Move move;

    public MoveCommand(ChessBoard chessBoard, Move move) {
        this.chessBoard = chessBoard;
        this.move = move;
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