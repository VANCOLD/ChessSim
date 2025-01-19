package at.chess.chesssimulator.board;

import at.chess.chesssimulator.board.enums.MoveType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static at.chess.chesssimulator.piece.enums.PieceType.PAWN;

/**
 * Represents a move on the chessboard, containing the original and new positions,
 * as well as the type of move being made. If we play locally this will be validated locally,
 * if we play online this will be validated on the server and locally.
 * <p>
 * This class stores the necessary information about a move, including:
 * <ul>
 *   <li>The original position where the piece is before the move.</li>
 *   <li>The new position where the piece is after the move.</li>
 *   <li>The type of move being made, as described by {@link MoveType}.</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
public class Move implements Serializable {

    /** The original position of the piece before the move. */
    Position originalPosition;

    /** The new position of the piece after the move. */
    Position newPosition;

    /** The type of move being made (e.g., regular move, capture, castling). */
    MoveType moveType;

    /** Extra data for the move (e.g., promotion piece for pawn promotion). */
    String extraData;

    /**
     * Constructs a new move with the specified original position, new position, and move type.
     * The extra data is set to an empty string by default.
     *
     *
     * @param originalPosition The original position of the piece.
     * @param newPosition The new position of the piece.
     * @param moveType The type of move being made.
     */
    public Move(Position originalPosition, Position newPosition, MoveType moveType) {
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
        this.moveType = moveType;
    }

    /**
     * String representation of the move in algebraic notation.
     * This method returns the move in standard algebraic notation, including special cases like castling and pawn promotion
     *
     * @return The string representation of the move in algebraic notation.
     */
    @Override
    public String toString() {

        if (this.moveType == MoveType.KCASTLING) {
            return "0-0";
        }
        if (this.moveType == MoveType.QCASTLING) {
            return "0-0-0";
        }

        if (originalPosition.getPiece().getType() == PAWN) {

            if (this.moveType == MoveType.MOVE) {
                return newPosition.toString();
            }

            if (this.moveType == MoveType.CAPTURE) {
                return originalPosition.toString().charAt(0) + "x" + newPosition.toString(); // Capture notation (e.g., exd5)
            }

            if (this.moveType == MoveType.PROMOTE) {
                return newPosition.toString() + "=" + extraData; // Pawn promotion
            }
        }


        String piece = "";
        switch (originalPosition.getPiece().getType()) {
            case KING:
                piece = "K";
                break;
            case QUEEN:
                piece = "Q";
                break;
            case ROOK:
                piece = "R";
                break;
            case BISHOP:
                piece = "B";
                break;
            case KNIGHT:
                piece = "N"; // Knight (explicitly using "N" to avoid confusion with King)
                break;
            default:
                break;
        }

        if (this.moveType == MoveType.CAPTURE) {
            return piece + "x" + newPosition.toString(); // Regular capture move
        }

        if (this.moveType == MoveType.MOVE) {
            return piece + newPosition.toString(); // Regular move notation
        }

        if (this.moveType == MoveType.CHECK) {
            return piece + newPosition.toString() + "+"; // Check notation
        }

        if (this.moveType == MoveType.CHECKMATE) {
            return piece + newPosition.toString() + "#"; // Checkmate notation
        }

        return "";
    }
}
