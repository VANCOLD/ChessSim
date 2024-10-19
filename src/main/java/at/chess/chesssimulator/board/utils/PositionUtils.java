package at.chess.chesssimulator.board.utils;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.config.ChessBoardConfig;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Utility class for operations related to positions on a chessboard.
 * <p>
 * This class provides static helper methods to perform common operations on {@link Position} objects, such as adding vectors,
 * checking bounds, and determining valid movements on the chessboard.
 * </p>
 */
public class PositionUtils {

    /**
     * Adds two positions together by adding their row and column values.
     * <p>
     * This can be used to move a position by applying a direction vector.
     * </p>
     *
     * @param direction  The direction vector to add.
     * @param moveOption The position vector to move.
     * @return A new {@link Position} representing the sum of the direction and moveOption vectors.
     */
    public static Position addVector(Position direction, Position moveOption) {
        int newRow = direction.getRow() + moveOption.getRow();
        int newCol = direction.getCol() + moveOption.getCol();
        return new Position(newRow, newCol);
    }

    /**
     * Checks if a given position is within the bounds of the chessboard.
     * <p>
     * The chessboard size is determined by {@link ChessBoardConfig}.
     * </p>
     *
     * @param pos The position to check.
     * @return {@code true} if the position is within bounds; {@code false} otherwise.
     */
    public static boolean isInBounds(Position pos) {
        return pos.getCol() >= 0 && pos.getCol() <= ChessBoardConfig.getCols() - 1
                && pos.getRow() >= 0 && pos.getRow() <= ChessBoardConfig.getRows() - 1;
    }

    /**
     * Checks if the given row and column values are within the bounds of the chessboard.
     * <p>
     * This method directly checks bounds without requiring a {@link Position} object.
     * </p>
     *
     * @param row The row index to check.
     * @param col The column index to check.
     * @return {@code true} if the row and column are within bounds; {@code false} otherwise.
     */
    public static boolean isInBounds(int row, int col) {
        return col >= 0 && col <= ChessBoardConfig.getCols() - 1
                && row >= 0 && row <= ChessBoardConfig.getRows() - 1;
    }

    /**
     * Finds the farthest in-bounds position in the given direction starting from a specific position.
     * <p>
     * The method moves in the given direction until it either hits an out-of-bounds location or an occupied square on the chessboard.
     * It then steps back to the last valid position.
     * </p>
     *
     * @param direction The direction in which to move.
     * @param position  The starting position.
     * @return The last valid {@link Position} before going out of bounds or encountering an occupied square.
     */
    public static Position getBound(Directions direction, Position position) {
        Position bound = new Position(position.getRow(), position.getCol());
        Position dirVector = direction.getVector();
        ChessBoard chessBoard = ChessBoard.getInstance();

        boolean inBounds;
        boolean isOccupied;

        do {
            bound = addVector(dirVector, bound);
            inBounds = isInBounds(bound);

            if (inBounds) {
                var posToCheck = chessBoard.getPosition(bound.getRow(), bound.getCol());
                isOccupied = chessBoard.isOccupied(posToCheck);
                if (isOccupied) {
                    break;
                }
            }

        } while (inBounds);

        // Undo last step to move back to the last valid position
        Position oppositeDir = Directions.getOppositeDirectionAsVector(direction);
        bound = addVector(oppositeDir, bound);

        return bound;
    }

    /**
     * Compares two positions to check if they have the same coordinates.
     *
     * @param pos1 The first position to compare.
     * @param pos2 The second position to compare.
     * @return {@code true} if both positions have the same row and column values; {@code false} otherwise.
     */
    public static boolean sameCoordinates(Position pos1, Position pos2) {
        if(pos1 == null || pos2 == null) {
            return false;
        }

        return pos1.getRow() == pos2.getRow()
                && pos1.getCol() == pos2.getCol();
    }

    public static Color getTileColor(int row, int col) {
        return (row + col) % 2 == 0 ? ChessBoardConfig.getTileColor1() : ChessBoardConfig.getTileColor2();
    }

    public static Color getTielsColor(Position pos) {
        return getTileColor(pos.getRow(), pos.getCol());
    }

    public static boolean containsPosition(List<Position> positions, Position pos) {
        return positions.stream().anyMatch(p -> sameCoordinates(p, pos));
    }
}