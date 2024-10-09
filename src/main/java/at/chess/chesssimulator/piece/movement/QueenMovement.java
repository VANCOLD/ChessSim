package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;

import java.util.ArrayList;
import java.util.List;

import static at.chess.chesssimulator.board.utils.Directions.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.*;

public class QueenMovement extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {
        List<Position> bufferList = new ArrayList<>();

        int pieceX = curPos.getRow();
        int pieceY = curPos.getCol();
        int pieceXLower = PositionUtils.getBound(Directions.LEFT, curPos).getRow();
        int pieceXUpper = PositionUtils.getBound(Directions.RIGHT, curPos).getRow();
        int pieceYLower = PositionUtils.getBound(Directions.UP, curPos).getCol();
        int pieceYUpper = PositionUtils.getBound(Directions.DOWN, curPos).getCol();

        // Getting all positions in the same row
        for (int i = pieceXLower; i <= pieceXUpper; i++) {
            bufferList.add(new Position(i,pieceY));
        }

        // Getting all position in the same col
        for (int i = pieceYLower; i <= pieceYUpper; i++) {
            bufferList.add(new Position(pieceX, i));
        }

        // Diagonal Direction
        Directions dia1Dir = UP_LEFT;
        Directions dia1OppDir = getOppositeDirection(dia1Dir);
        Position dia1AddVector = getOppositeDirectionAsVector(dia1Dir);
        Position dia1Lower = getBound(dia1Dir, curPos);
        Position dia1Upper = getBound(dia1OppDir, curPos);
        dia1Upper = addVector(dia1Upper, dia1AddVector); // the bound must be inclusive!

        Directions dia2Dir = DOWN_LEFT;
        Directions dia2OppDir = getOppositeDirection(dia2Dir);
        Position dia2AddVector = getOppositeDirectionAsVector(dia2Dir);
        Position dia2Lower = getBound(dia2Dir, curPos);
        Position dia2Upper = getBound(dia2OppDir, curPos);
        dia2Upper = addVector(dia2Upper, dia2AddVector); // the bound must be inclusive!

        Position posBuff = new Position(dia1Lower.getRow(), dia1Lower.getCol());
        do {
            bufferList.add(posBuff);
            posBuff = addVector(dia1AddVector, posBuff);
        } while(isInBounds(posBuff) && !sameCoordinates(dia1Upper, posBuff));

        posBuff = new Position(dia2Lower.getRow(), dia2Lower.getCol());
        do {
            bufferList.add(posBuff);
            posBuff = addVector(dia2AddVector, posBuff);
        } while(isInBounds(posBuff) && !sameCoordinates(dia2Upper, posBuff));
        // sameCoordinates tells us if cords are the same, we break out of the loop, but the bounds stop a position before we hit a piece!
        // That's why we increased the upper bounds by 1


        // Removing the place where the rook is standing
        bufferList.removeIf(toCheck -> toCheck.getRow() == curPos.getRow()
                && toCheck.getCol() == curPos.getCol());

        return bufferList;
    }
}
