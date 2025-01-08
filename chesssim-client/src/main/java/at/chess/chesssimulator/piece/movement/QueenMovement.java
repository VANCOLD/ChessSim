package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;

import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;

public class QueenMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {
        List<Position> possiblePositions = new ArrayList<>();

        // Add straight-line moves
        // choosing either left / up or down and right is fine, it will check each direction anyways with 1 call
        RookMovement.addStraightLineMoves(possiblePositions, curPos, LEFT);
        RookMovement.addStraightLineMoves(possiblePositions, curPos, UP);

        // Add diagonal moves
        // choosing either up_left / down_right or down_left and up_right is fine, it will check each direction anyways with 1 call
        BishopMovement.addDiagonalMoves(possiblePositions, curPos, UP_LEFT);
        BishopMovement.addDiagonalMoves(possiblePositions, curPos, UP_RIGHT);

        // Removing the place where the queen is standing
        possiblePositions.removeIf(toCheck -> toCheck.getRow() == curPos.getRow()
                && toCheck.getCol() == curPos.getCol());

        logger.debug("Queen movement - found the following possible moves: {}", possiblePositions);
        return possiblePositions;
    }
}