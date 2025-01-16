package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.*;

@Getter
@Setter
public class RookMovement extends AbstractStrategy {

    // needed for casteling
    private boolean firstMove = true;

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> possiblePositions = new ArrayList<>();
        // choosing either left / up or down and right is fine, it will check each direction anyways with 1 call
        addStraightLineMoves(possiblePositions, curPos, LEFT);
        addStraightLineMoves(possiblePositions, curPos, UP);

        // Removing the place where the rook is standing
        possiblePositions.removeIf(toCheck -> toCheck.getRow() == curPos.getRow()
                && toCheck.getCol() == curPos.getCol());

        logger.debug("Rook movement - found the following possible moves: {}", possiblePositions);
        return possiblePositions;
    }

    protected static void addStraightLineMoves(List<Position> positions, Position curPos, Directions direction) {
        Position bound = getBound(direction, curPos);
        Position oppositeBound = getBound(getOppositeDirection(direction), curPos);
        Position addVector = getOppositeDirectionAsVector(direction);
        oppositeBound = addVector(oppositeBound, addVector); // the bound must be inclusive!

        logger.debug("Rook movement - {} lower bound {} / upper bound {}", direction, bound, oppositeBound);

        Position posBuff = new Position(bound.getRow(), bound.getCol());
        do {
            positions.add(posBuff);
            posBuff = addVector(addVector, posBuff);
        } while (isInBounds(posBuff) && !sameCoordinates(oppositeBound, posBuff));
    }
}