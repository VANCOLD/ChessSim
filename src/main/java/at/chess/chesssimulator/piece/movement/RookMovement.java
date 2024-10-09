package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;

import java.util.ArrayList;
import java.util.List;

public class RookMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> possiblePositions = new ArrayList<>();

        int pieceX = curPos.getRow();
        int pieceY = curPos.getCol();
        int pieceXLower = PositionUtils.getBound(Directions.LEFT, curPos).getRow();
        int pieceXUpper = PositionUtils.getBound(Directions.RIGHT, curPos).getRow();
        int pieceYLower = PositionUtils.getBound(Directions.UP, curPos).getCol();
        int pieceYUpper = PositionUtils.getBound(Directions.DOWN, curPos).getCol();

        logger.debug("Rook movement - x-axis lower bound {} / upper bound {}", pieceXLower, pieceXUpper);
        logger.debug("Rook movement - y-axis lower bound {} / upper bound {}", pieceYLower, pieceYUpper);


        // Getting all positions in the same row
        for (int i = pieceXLower; i <= pieceXUpper; i++) {
            possiblePositions.add(new Position(i,pieceY));
        }

        // Getting all position in the same col
        for (int i = pieceYLower; i <= pieceYUpper; i++) {
            possiblePositions.add(new Position(pieceX, i));
        }

        // Removing the place where the rook is standing
        possiblePositions.removeIf(toCheck -> toCheck.getRow() == curPos.getRow()
                                    && toCheck.getCol() == curPos.getCol());


        logger.debug("Rook movement - found the following possible moves: {}", possiblePositions);


        return possiblePositions;
    }
}
