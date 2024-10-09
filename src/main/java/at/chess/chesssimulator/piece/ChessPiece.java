package at.chess.chesssimulator.piece;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.piece.movement.*;
import at.chess.chesssimulator.piece.utils.PngLoader;
import lombok.Getter;

import javafx.scene.image.ImageView;
import java.util.List;

@Getter
public class ChessPiece {

    private final ImageView image;
    protected final MovementStrategy movementStrategy;
    private final PieceColor color;
    private final PieceType type;

    /**
     * Used to generate ChessPieces. Since they are always the same, we wrap them in a generate function.
     * Use generate ChessPiece to get a valid ChessPiece
     *
     * @param image image of the chess piece for display
     * @param movementStrategy the movement strategy the piece is using
     * @param color color of the chess piece
     * @param type type of the chess piece (pawn, rook, bishop, ... etc.)
     */
    protected ChessPiece(ImageView image, MovementStrategy movementStrategy, PieceColor color, PieceType type) {
        this.image = image;
        this.movementStrategy = movementStrategy;
        this.color = color;
        this.type = type;
    }

    public static ChessPiece generateChessPiece(PieceColor color, PieceType type) {

        ImageView image = PngLoader.loadImage(color, type);

        MovementStrategy movement = switch(type) {
            case PAWN -> new PawnMovement();
            case ROOK -> new RookMovement();
            case KING -> new KingMovement();
            case QUEEN -> new QueenMovement();
            case BISHOP -> new BishopMovement();
            case KNIGHT -> new KnightMovement();
        };

        return new ChessPiece(image, movement, color, type);
    }

    public List<Position> getMovementRange(Position posToCheck) {
        return this.movementStrategy.getPossibleMoves(posToCheck);
    }
}
