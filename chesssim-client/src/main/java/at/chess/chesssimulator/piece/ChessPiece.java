package at.chess.chesssimulator.piece;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.piece.movement.*;
import at.chess.chesssimulator.utils.PngLoader;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a chess piece on the chessboard, encapsulating its color, type, movement strategy, and visual representation.
 * <p>
 * The class provides methods for generating a chess piece and determining its possible movements based on its type.
 * </p>
 */
@Getter
public class ChessPiece implements Cloneable {

    protected static final Logger logger = LoggerFactory.getLogger(ChessPiece.class);

    /** The visual representation of the chess piece. */
    private final Image image;

    /** The strategy used to determine valid movements for this chess piece. */
    protected final MovementStrategy movementStrategy;

    /** The color of the chess piece (either black or white). */
    private final PieceColor color;

    /** The type of the chess piece (e.g., pawn, rook, etc.). */
    private final PieceType type;

    /**
     * Constructs a chess piece with the specified properties.
     *
     * @param image            The image representation of the chess piece.
     * @param movementStrategy The movement strategy associated with this piece.
     * @param color            The color of the chess piece.
     * @param type             The type of the chess piece.
     */
    protected ChessPiece(Image image, MovementStrategy movementStrategy, PieceColor color, PieceType type) {
        this.image = image;
        this.movementStrategy = movementStrategy;
        this.color = color;
        this.type = type;
    }

    /**
     * Generates a new chess piece based on the specified color and type.
     * <p>
     * This method loads the corresponding image for the piece and initializes its movement strategy.
     * </p>
     *
     * @param color The color of the chess piece.
     * @param type  The type of the chess piece.
     * @return A new instance of {@link ChessPiece}.
     */
    public static ChessPiece generateChessPiece(PieceColor color, PieceType type) {
        logger.debug("Generating new piece {} {}", color.name().toLowerCase(), type.name().toLowerCase());
        String colorName = color.name().toLowerCase();
        String pieceName = type.name().toLowerCase();
        Image pieceImage = PngLoader.getInstance().getImage(colorName, "_", pieceName, ".png");

        MovementStrategy movement = switch(type) {
            case PAWN -> new PawnMovement();
            case ROOK -> new RookMovement();
            case KING -> new KingMovement();
            case QUEEN -> new QueenMovement();
            case BISHOP -> new BishopMovement();
            case KNIGHT -> new KnightMovement();
        };

        return new ChessPiece(pieceImage, movement, color, type);
    }

    /**
     * Retrieves the possible movement range for the chess piece based on its current position.
     *
     * @param posToCheck The position of the piece for which to calculate the movement range.
     * @return A list of possible {@link Position} instances that the piece can move to.
     */
    public List<Position> getMovementRange(Position posToCheck) {
        return this.movementStrategy.getPossibleMoves(posToCheck);
    }


    /**
     * Creates and returns a copy of this chess piece.
     * <p>
     * The method performs a shallow copy of the chess piece, meaning that the piece's fields are copied as-is.
     * This includes the image, movement strategy, color, and type.
     * </p>
     *
     * @return A clone of this chess piece.
     * @throws AssertionError If the cloning operation is not supported.
     */
    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}