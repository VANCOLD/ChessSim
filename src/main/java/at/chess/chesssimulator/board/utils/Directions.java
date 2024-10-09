package at.chess.chesssimulator.board.utils;

import at.chess.chesssimulator.board.Position;

public enum Directions {
    UP(0, -1),
    UP_RIGHT(1, -1),
    RIGHT(1, 0),
    DOWN_RIGHT(1, 1),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    LEFT(-1, 0),
    UP_LEFT(-1, -1);

    private final int x;
    private final int y;

    Directions(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Directions getOppositeDirection(Directions direction) {
        return switch(direction) {
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

    public static Position getOppositeDirectionAsVector(Directions direction) {
        return getOppositeDirection(direction).getVector();
    }

    public Position getVector() {
        return new Position(x,y);
    }
}
