package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.piece.enums.PieceColor;

public class CheckCommand extends AbstractCommand{

    public CheckCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {
        chessBoard.movePiece(move.getOriginalPosition(), move.getNewPosition());
        chessBoard.setCheck(true);
    }

    @Override
    public void undo() {

        chessBoard.movePiece(move.getNewPosition(), move.getOriginalPosition());

        // it is possible to capture when placing a check so we need to check for that as well
        if(move.getNewPosition().getPiece() != null) {
            chessBoard.placePiece(move.getNewPosition(), move.getNewPosition().getPiece());
        }
        PieceColor turn = chessBoard.getTurn();
        chessBoard.getPosition(chessBoard.getKingPosition(turn)).setInCheck(false);
    }
}
