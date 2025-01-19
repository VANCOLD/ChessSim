package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;

import java.util.List;

public interface MovementStrategy {

    List<Position> getPossibleMoves(Position position);
    boolean canMove(Position piecePosition, Position newPosition);
    boolean canCapture(Position piececPosition, Position capturePosition);
}
