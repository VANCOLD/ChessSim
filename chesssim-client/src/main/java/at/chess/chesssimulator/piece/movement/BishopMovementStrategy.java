package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;

import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.*;

public class BishopMovementStrategy extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> possiblePositions = new ArrayList<>();
        // choosing either up_left / down_right or down_left and up_right is fine, it will check each direction anyway with 1 call
        addDiagonalMoves(possiblePositions, curPos, UP_LEFT);
        addDiagonalMoves(possiblePositions, curPos, UP_RIGHT);

        // Removing the place where the bishop is standing
        possiblePositions.removeIf(toCheck -> toCheck.getRow() == curPos.getRow()
                && toCheck.getCol() == curPos.getCol());

        logger.debug("Bishop movement - found the following possible moves: {}", possiblePositions);
        return possiblePositions;
    }

    protected static void addDiagonalMoves(List<Position> positions, Position curPos, Directions direction) {

        Position bound = getBound(direction, curPos);
        Position oppositeBound = getBound(getOppositeDirection(direction), curPos);
        Position addVector = getOppositeDirectionAsVector(direction);
        oppositeBound = addVector(oppositeBound, addVector); // the bound must be inclusive!

        logger.debug("Bishop movement - {} lower bound {} / upper bound {}", direction, bound, oppositeBound);

        Position posBuff = new Position(bound.getRow(), bound.getCol());
        do {
            positions.add(posBuff);
            posBuff = addVector(addVector, posBuff);
        } while (isInBounds(posBuff) && !sameCoordinates(oppositeBound, posBuff));
    }
}