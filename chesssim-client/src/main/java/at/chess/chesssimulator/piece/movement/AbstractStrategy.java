package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;

/**
 * The {@code AbstractStrategy} class provides a base implementation for the movement strategies of chess pieces.
 * It implements common logic for determining whether a piece can move or capture on the chessboard.
 * Concrete piece movement strategies should extend this class and define their specific movement logic.
 */
public abstract class AbstractStrategy implements MovementStrategy {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractStrategy.class);
    protected final ChessBoard chessBoard;

    /**
     * Constructor to initialize the chess board instance.
     */
    public AbstractStrategy() {
        this.chessBoard = ChessBoard.getInstance();
    }

    /**
     * Checks if a piece can move from its current position to a new position.
     * A move is valid if the new position is not occupied by another piece.
     *
     * @param piecePosition The current position of the piece on the chessboard
     * @param newPosition The target position where the piece is attempting to move
     * @return {@code true} if the piece can move to the new position, {@code false} otherwise
     */
    @Override
    public boolean canMove(Position piecePosition, Position newPosition) {

        if (chessBoard.isOccupied(newPosition)) {
            return false;
        }

        Position boardPiece = chessBoard.getPosition(piecePosition);

        return getPossibleMoves(boardPiece).stream().anyMatch(pos -> sameCoordinates(pos, newPosition));
    }

    /**
     * Checks if a piece can capture an opponent's piece at a given position.
     * A capture is valid if the target position is occupied by an opponent's piece.
     *
     * @param piecePosition The current position of the piece on the chessboard
     * @param capturePosition The position where the opponent's piece is located
     * @return {@code true} if the piece can capture the opponent's piece, {@code false} otherwise
     */
    @Override
    public boolean canCapture(Position piecePosition, Position capturePosition) {

        if (!chessBoard.isOccupied(capturePosition)) {
            return false;
        }

        PieceColor pieceColor = piecePosition.getColor();
        PieceColor captureColor = capturePosition.getColor();

        if (captureColor == null || pieceColor == captureColor) {
            return false;
        }

        return getPossibleMoves(piecePosition).stream().anyMatch(pos -> sameCoordinates(pos, capturePosition));
    }
}
