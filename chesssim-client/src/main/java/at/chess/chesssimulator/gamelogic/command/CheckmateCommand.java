package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.piece.enums.PieceColor;

public class CheckmateCommand extends AbstractCommand {

    public CheckmateCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {
        PieceColor loser = PieceColor.getOppositeColor(chessBoard.getTurn());
        chessBoard.movePiece(move.getOriginalPosition(), move.getNewPosition());
        chessBoard.getKingPosition(loser).setInCheck(true);

        logger.info("Checkmate! Player {} wins!", move.getOriginalPosition().getPiece().getColor());

    }

    @Override
    public void undo() {
        // Can't undo winning a game
    }
}
