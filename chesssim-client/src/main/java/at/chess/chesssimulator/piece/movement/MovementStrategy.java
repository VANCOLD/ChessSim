package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;

import java.util.List;

/**
 * The {@code MovementStrategy} interface defines the contract for different movement strategies
 * for chess pieces. Each piece in chess has a unique way of moving and capturing, and this interface
 * provides the necessary methods to calculate possible moves and validate movement and capture actions.
 */
public interface MovementStrategy {

    /**
     * Returns a list of all possible moves for a piece from a given position.
     *
     * @param position The current position of the piece on the chessboard
     * @return A list of {@code Position} objects representing the possible moves for the piece
     */
    List<Position> getPossibleMoves(Position position);

    /**
     * Determines if a piece can move from its current position to a new position.
     *
     * @param piecePosition The current position of the piece
     * @param newPosition The target position where the piece is attempting to move
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    boolean canMove(Position piecePosition, Position newPosition);

    /**
     * Determines if a piece can capture an opponent's piece at a given position.
     *
     * @param piecePosition The current position of the piece
     * @param capturePosition The position of the opponent's piece to be captured
     * @return {@code true} if the capture is valid, {@code false} otherwise
     */
    boolean canCapture(Position piecePosition, Position capturePosition);
}
