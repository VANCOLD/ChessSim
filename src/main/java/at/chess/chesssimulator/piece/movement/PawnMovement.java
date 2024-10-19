package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;

import static at.chess.chesssimulator.board.utils.PositionUtils.addVector;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;
import static at.chess.chesssimulator.piece.enums.PieceColor.*;
import static at.chess.chesssimulator.board.utils.Directions.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position currentPosition) {

        List<Position> possiblePositions = new ArrayList<>();

        PieceColor pieceColor = currentPosition.getPiece().getColor();
        Position direction = pieceColor == BLACK ? UP.getVector() : DOWN.getVector();
        Position diagonalLeftDir = LEFT.getVector();
        Position diagonalRightDir = RIGHT.getVector();
        Position move1  = addVector(currentPosition, direction);
        Position move2  = addVector(move1,direction);

        if(isInBounds(move1) && !chessBoard.isOccupied(move1)) {
            possiblePositions.add(move1);
        }

        var leftDiagonalCheck = addVector(diagonalLeftDir,move2);
        var rightDiagonalCheck = addVector(diagonalRightDir,move2);
        if (isInBounds(move2) && !chessBoard.isOccupiedByColor(move2, getOppositeColor(pieceColor))
          && !chessBoard.isOccupiedByColor(leftDiagonalCheck, getOppositeColor(pieceColor))
          && !chessBoard.isOccupiedByColor(rightDiagonalCheck, getOppositeColor(pieceColor))) {
            possiblePositions.add(move2);
        }

        logger.debug("Pawn movement - found the following possible moves: {}", possiblePositions);

        return possiblePositions;
    }
}