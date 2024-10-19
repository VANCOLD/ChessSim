package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class KingMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> directions = Arrays.stream(Directions.values()).map(Directions::getVector).toList();
        List<Position> bufferList = directions
                                    .stream()
                                    .map(pos -> PositionUtils.addVector(pos,curPos))
                                    .filter(PositionUtils::isInBounds)
                                    .filter(Predicate.not(chessBoard::isOccupied))
                                    .toList();

        logger.debug("King movement - found the following possible moves: {}", bufferList);

        return bufferList;
    }
}