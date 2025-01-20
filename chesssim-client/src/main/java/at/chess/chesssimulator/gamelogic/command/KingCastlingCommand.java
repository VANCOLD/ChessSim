package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;

public class KingCastlingCommand extends AbstractCommand {

    public KingCastlingCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.RIGHT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.RIGHT.getVector());

        chessBoard.movePiece(move.getOriginalPosition(), kingPosition);
        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.RIGHT.getVector());
        chessBoard.movePiece(move.getNewPosition(), rookPosition);
        move.getOriginalPosition().getPiece().setFirstMove(false);
        move.getNewPosition().getPiece().setFirstMove(false);
    }

    @Override
    public void undo() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.RIGHT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.RIGHT.getVector());

        chessBoard.movePiece(kingPosition, move.getOriginalPosition());
        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.RIGHT.getVector());
        chessBoard.movePiece(rookPosition, move.getNewPosition());
        chessBoard.getPieceAt(move.getNewPosition()).setFirstMove(true);
        chessBoard.getPieceAt(move.getOriginalPosition()).setFirstMove(true);
    }
}
