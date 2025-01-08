package at.chess.chesssimulator.board.utils;

import at.chess.chesssimulator.board.Position;

/**
 * Enum representing the possible movement directions on a chessboard.
 * <p>
 * The directions are defined in terms of X and Y coordinate changes, where positive X moves
 * right and positive Y moves down (standard Cartesian plane). This enum can also provide
 * the opposite direction for any given direction.
 * </p>
 * <p>
 * The directions are as follows:
 * <ul>
 *   <li>UP: Moves up on the board (negative Y direction).</li>
 *   <li>UP_RIGHT: Moves diagonally up-right on the board (positive X, negative Y).</li>
 *   <li>RIGHT: Moves right on the board (positive X).</li>
 *   <li>DOWN_RIGHT: Moves diagonally down-right on the board (positive X, positive Y).</li>
 *   <li>DOWN: Moves down on the board (positive Y).</li>
 *   <li>DOWN_LEFT: Moves diagonally down-left on the board (negative X, positive Y).</li>
 *   <li>LEFT: Moves left on the board (negative X).</li>
 *   <li>UP_LEFT: Moves diagonally up-left on the board (negative X, negative Y).</li>
 * </ul>
 * </p>
 */
public enum Directions {
    UP(0, -1),
    UP_RIGHT(1, -1),
    RIGHT(1, 0),
    DOWN_RIGHT(1, 1),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    LEFT(-1, 0),
    UP_LEFT(-1, -1);

    private final int x;  // The change in the X direction (horizontal)
    private final int y;  // The change in the Y direction (vertical)

    /**
     * Constructs a new direction with the specified X and Y changes.
     *
     * @param x The change in the X direction.
     * @param y The change in the Y direction.
     */
    Directions(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the opposite direction of the given direction.
     * <p>
     * For example, the opposite of {@code UP} is {@code DOWN}, and the opposite of {@code LEFT} is {@code RIGHT}.
     * </p>
     *
     * @param direction The direction for which to find the opposite.
     * @return The opposite direction.
     */
    public static Directions getOppositeDirection(Directions direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case UP_LEFT -> DOWN_RIGHT;
            case DOWN_RIGHT -> UP_LEFT;
            case UP_RIGHT -> DOWN_LEFT;
            case DOWN_LEFT -> UP_RIGHT;
        };
    }

    /**
     * Returns the opposite direction as a {@link Position} vector.
     * <p>
     * This method converts the opposite direction of the input direction into a {@link Position}
     * that represents the X and Y changes.
     * </p>
     *
     * @param direction The direction for which to get the opposite as a vector.
     * @return A {@link Position} representing the opposite direction's movement vector.
     */
    public static Position getOppositeDirectionAsVector(Directions direction) {
        return getOppositeDirection(direction).getVector();
    }

    /**
     * Returns the current direction as a {@link Position} vector.
     * <p>
     * The {@code Position} will contain the X and Y changes associated with the direction.
     * </p>
     *
     * @return A {@link Position} representing this direction's movement vector.
     */
    public Position getVector() {
        return new Position(x, y);
    }

    /**
     * Converts a given vector (Position) into the corresponding direction.
     *
     * @param position The position vector to be converted into a direction.
     * @return The corresponding direction from the enum, or null if no match is found.
     * @throws IllegalArgumentException if the vector does not correspond to any valid direction.
     */
    public static Directions getDirectionFromPosition(Position position) {
        int x = Integer.signum(position.getRow());
        int y = Integer.signum(position.getCol());

        for (Directions direction : Directions.values()) {
            if (direction.x == x && direction.y == y) {
                return direction;
            }
        }

        throw new IllegalArgumentException("Invalid vector: " + position + " does not match any direction.");
    }
}