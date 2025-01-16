package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.controller.PromotionPopup;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceType;

public class PromotionCommand extends AbstractCommand {

    public PromotionCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

        ChessPiece pawn = move.getOriginalPosition().getPiece();

        PromotionPopup promotionPopup = new PromotionPopup();
        promotionPopup.setInitialState(pawn.getColor());
        String selectedPiece = promotionPopup.showPromotionPopup();


        ChessPiece piece = ChessPiece.generateChessPiece(pawn.getColor(), PieceType.getPieceType(selectedPiece.toLowerCase().charAt(0)));
        chessBoard.clearPosition(move.getOriginalPosition());
        chessBoard.placePiece(move.getNewPosition(), piece);
    }

    @Override
    public void undo() {
        chessBoard.clearPosition(move.getNewPosition());
        chessBoard.placePiece(move.getOriginalPosition(), move.getOriginalPosition().getPiece());
    }
}
