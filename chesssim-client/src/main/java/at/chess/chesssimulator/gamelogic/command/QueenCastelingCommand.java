package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;
import at.chess.chesssimulator.piece.movement.KingMovement;
import at.chess.chesssimulator.piece.movement.RookMovement;

public class QueenCastelingCommand extends AbstractCommand {

    public QueenCastelingCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.LEFT.getVector());
        chessBoard.movePiece(move.getOriginalPosition(), kingPosition);

        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        chessBoard.movePiece(move.getNewPosition(), rookPosition);
        ((KingMovement) move.getOriginalPosition().getPiece().getMovementStrategy()).setFirstMove(false);
        ((RookMovement)move.getNewPosition().getPiece().getMovementStrategy()).setFirstMove(false);
    }

    @Override
    public void undo() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.LEFT.getVector());

        chessBoard.movePiece(kingPosition, move.getOriginalPosition());
        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        chessBoard.movePiece(rookPosition, move.getNewPosition());
        ((KingMovement) move.getOriginalPosition().getPiece().getMovementStrategy()).setFirstMove(true);
        ((RookMovement)move.getNewPosition().getPiece().getMovementStrategy()).setFirstMove(true);
    }
}
