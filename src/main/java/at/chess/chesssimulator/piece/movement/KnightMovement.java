package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;

import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.*;

public class KnightMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> possiblePositions = new ArrayList<>();
        ChessBoard board = ChessBoard.getInstance();

        // Up directions
        Position upLeftDir = addVector(UP.getVector(),UP_LEFT.getVector());
        Position upLeft = addVector(upLeftDir,curPos);
        if(isInBounds(upLeft) && !board.isOccupied(upLeft)) {
            possiblePositions.add(upLeft);
        }

        Position upRightDir = addVector(UP.getVector(),UP_RIGHT.getVector());
        Position upRight = addVector(upRightDir,curPos);
        if(isInBounds(upRight) && !board.isOccupied(upRight)) {
            possiblePositions.add(upRight);
        }

        // Down directions
        Position downLeftDir = addVector(DOWN.getVector(),DOWN_LEFT.getVector());
        Position downLeft = addVector(downLeftDir,curPos);
        if(isInBounds(downLeft) && !board.isOccupied(downLeft)) {
            possiblePositions.add(downLeft);
        }

        Position downRightDir = addVector(DOWN.getVector(),DOWN_RIGHT.getVector());
        Position downRight = addVector(downRightDir,curPos);
        if(isInBounds(downRight) && !board.isOccupied(downRight)) {
            possiblePositions.add(downRight);
        }

        // Left Directions
        Position leftUpDir = addVector(LEFT.getVector(),UP_LEFT.getVector());
        Position leftUp = addVector(leftUpDir,curPos);
        if(isInBounds(leftUp) && !board.isOccupied(leftUp)) {
            possiblePositions.add(leftUp);
        }

        Position leftDownDir = addVector(LEFT.getVector(),DOWN_LEFT.getVector());
        Position leftDown = addVector(leftDownDir,curPos);
        if(isInBounds(leftDown) && !board.isOccupied(leftDown)) {
            possiblePositions.add(leftDown);
        }

        // Right Directions
        Position rightUpDir = addVector(RIGHT.getVector(),UP_RIGHT.getVector());
        Position rightUp = addVector(rightUpDir,curPos);
        if(isInBounds(rightUp) && !board.isOccupied(rightUp)) {
            possiblePositions.add(rightUp);
        }

        Position rightDownDir = addVector(RIGHT.getVector(),DOWN_RIGHT.getVector());
        Position rightDown = addVector(rightDownDir,curPos);
        if(isInBounds(rightDown) && !board.isOccupied(rightDown)) {
            possiblePositions.add(rightDown);
        }

        logger.debug("Knight movement - found the following possible moves: {}", possiblePositions);

        return possiblePositions;
    }

    @Override
    public boolean canCapture(Position position) {
        return false;
    }
}
