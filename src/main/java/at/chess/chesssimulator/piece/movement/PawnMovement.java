package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;
import lombok.Setter;

import static at.chess.chesssimulator.board.utils.PositionUtils.addVector;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;
import static at.chess.chesssimulator.piece.enums.PieceColor.*;
import static at.chess.chesssimulator.board.utils.Directions.*;

import java.util.ArrayList;
import java.util.List;

@Setter
public class PawnMovement extends AbstractStrategy {

    private boolean firstMove = true;

    @Override
    public List<Position> getPossibleMoves(Position currentPosition) {

        List<Position> possiblePositions = new ArrayList<>();

        PieceColor pieceColor = currentPosition.getPiece().getColor();
        Position direction = pieceColor == BLACK ? UP.getVector() : DOWN.getVector();
        Position move1  = addVector(currentPosition, direction);
        Position move2  = addVector(move1,direction);

        if(isInBounds(move1) && !chessBoard.isOccupied(move1)) {
            possiblePositions.add(move1);
        }

        if (firstMove) {
            if (isInBounds(move2) && !chessBoard.isOccupied(move2)) {
                possiblePositions.add(move2);
            }
        }

        logger.debug("Pawn movement - found the following possible moves: {}", possiblePositions);

        return possiblePositions;
    }

    @Override
    public boolean canCapture(Position position) {
        return false;
    }
}