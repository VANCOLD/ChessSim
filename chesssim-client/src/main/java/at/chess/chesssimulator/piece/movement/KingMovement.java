package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;

@Getter
@Setter
public class KingMovement extends AbstractStrategy {

    // needed for casteling
    private boolean firstMove = true;

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> directions = Arrays.stream(Directions.values()).map(Directions::getVector).toList();
        List<Position> bufferList = directions
                                    .stream()
                                    .map(pos -> PositionUtils.addVector(pos,curPos))
                                    .filter(PositionUtils::isInBounds)
                                    .toList();

        logger.debug("King movement - found the following possible moves: {}", bufferList);

        return bufferList;
    }

    public boolean canQueenSideCastle(Position kingPos, Position rookPos) {
        return canCastle(kingPos, rookPos, Directions.LEFT);
    }

    public boolean canKingSideCastle(Position kingPos, Position rookPos) {
        return canCastle(kingPos, rookPos, Directions.RIGHT);
    }

    private boolean canCastle(Position kingPos, Position rookPos, Directions direction) {

        Position kingPosition = chessBoard.getPosition(kingPos);
        Position rookPosition = chessBoard.getPosition(rookPos);

        boolean rookFirstMove = ((RookMovement) rookPosition.getPiece().getMovementStrategy()).isFirstMove();
        Position rookDirection = PositionUtils.getBound(direction, rookPos);
        if (!firstMove || !rookFirstMove || !sameCoordinates(rookPos, rookDirection)) {
            return false;
        }

        List<Position> positionsToCheck = PositionUtils.getPositionsBetweenRow(kingPosition, rookPosition, direction);

        return positionsToCheck.stream().noneMatch(chessBoard::isOccupied);
    }
}