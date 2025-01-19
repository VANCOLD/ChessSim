package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.*;

public class KnightMovementStrategy extends AbstractStrategy {

    private static final Position[] KNIGHT_MOVES = {
            addVector(UP.getVector(), UP_LEFT.getVector()),
            addVector(UP.getVector(), UP_RIGHT.getVector()),
            addVector(DOWN.getVector(), DOWN_LEFT.getVector()),
            addVector(DOWN.getVector(), DOWN_RIGHT.getVector()),
            addVector(LEFT.getVector(), UP_LEFT.getVector()),
            addVector(LEFT.getVector(), DOWN_LEFT.getVector()),
            addVector(RIGHT.getVector(), UP_RIGHT.getVector()),
            addVector(RIGHT.getVector(), DOWN_RIGHT.getVector())
    };

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> possiblePositions = new ArrayList<>();
        for (Position move : KNIGHT_MOVES) {
            Position newPos = addVector(curPos, move);
            if (isInBounds(newPos)) {
                possiblePositions.add(newPos);
            }
        }
        logger.debug("Knight movement - found the following possible moves: {}", possiblePositions);
        return possiblePositions;
    }
}