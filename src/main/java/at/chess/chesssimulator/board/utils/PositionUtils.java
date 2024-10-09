package at.chess.chesssimulator.board.utils;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.config.ChessBoardConfig;

public class PositionUtils {

    public static Position addVector(Position direction, Position moveOption) {
        int newRow = direction.getRow() + moveOption.getRow();
        int newCol = direction.getCol() + moveOption.getCol();
        return new Position(newRow,newCol);
    }

    public static boolean isInBounds(Position pos) {
        return pos.getCol() >= 0 && pos.getCol() <= ChessBoardConfig.getCols()-1
            && pos.getRow() >= 0 && pos.getRow() <= ChessBoardConfig.getRows()-1;
    }

    public static boolean isInBounds(int row, int col) {
        return col >= 0 && col <= ChessBoardConfig.getCols()-1
                && row >= 0 && row <= ChessBoardConfig.getRows()-1;
    }

    public static Position getBound(Directions direction, Position position) {

        Position bound = new Position(position.getRow(), position.getCol());
        Position dirVector = direction.getVector();
        ChessBoard chessBoard = ChessBoard.getInstance();

        boolean inBounds;
        boolean isOccupied = false;

        do {

            bound = addVector(dirVector, bound);
            inBounds = isInBounds(bound);

            if(inBounds) {
                var posToCheck = chessBoard.getPosition(bound.getRow(), bound.getCol());
                isOccupied = chessBoard.isOccupied(posToCheck);
            }

        } while (inBounds && !isOccupied);

        Position oppositeDir = Directions.getOppositeDirectionAsVector(direction);
        bound = addVector(oppositeDir, bound); // Undo last step; otherwise we'd be out of bounds!

        return bound;
    }

    public static boolean sameCoordinates(Position pos1, Position pos2) {
        return pos1.getRow() == pos2.getRow()
            && pos1.getCol() == pos2.getCol();
    }
}
