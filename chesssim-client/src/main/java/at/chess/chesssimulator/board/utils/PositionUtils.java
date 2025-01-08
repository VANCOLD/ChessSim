package at.chess.chesssimulator.board.utils;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.config.ChessBoardConfig;
import javafx.scene.paint.Color;

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
       return isInBounds(pos.getRow(), pos.getCol());
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
     * If it hits a out of bound square it moves back one square and returns the last valid position.
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
                var posToCheck = chessBoard.getPosition(bound);
                isOccupied = chessBoard.isOccupied(posToCheck);
                if (isOccupied) {
                    return bound; // Return the first occupied position
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

    /**
     * Determines the color of a tile on the chessboard based on its row and column indices.
     * <p>
     * The chessboard alternates colors in a checkerboard pattern. This method calculates the color
     * based on the sum of the row and column indices.
     * </p>
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     * @return The color of the tile at the specified row and column.
     */
    public static Color getTileColor(int row, int col) {
        return (row + col) % 2 == 0 ? ChessBoardConfig.getTileColor1() : ChessBoardConfig.getTileColor2();
    }

    /**
     * Loads a position from the chessboard and creates a deep copy of it.
     *
     * @param position The position to load and copy.
     * @return A deep copy of the loaded position, or {@code null} if the position is not valid.
     */
    public static Position loadAndCopyPosition(Position position) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        Position originalPosition = chessBoard.getPosition(position);

        if (originalPosition == null) {
            return null;
        }

        // Create a deep copy of the position
        Position copiedPosition = new Position(originalPosition.getRow(), originalPosition.getCol());
        if (originalPosition.getPiece() != null) {
            copiedPosition.setPiece(originalPosition.getPiece().clone());
        }

        return copiedPosition;
    }
}