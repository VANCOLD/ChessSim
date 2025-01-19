package at.chess.chesssimulator.board;

import at.chess.chesssimulator.board.enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
public class Move {

    /** The original position of the piece before the move. */
    Position originalPosition;

    /** The new position of the piece after the move. */
    Position newPosition;

    /** The type of move being made (e.g., regular move, capture, castling). */
    MoveType moveType;

    /** Extra data for the move (e.g., promotion piece for pawn promotion). */
    String extraData;


    public Move(Position originalPosition, Position newPosition, MoveType moveType) {
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
        this.moveType = moveType;
    }

    @Override
    public String toString() {
        // If the move is castling (kingside or queenside)
        if (this.moveType == MoveType.KCASTLING) {
            return "0-0"; // Kingside castling notation
        }
        if (this.moveType == MoveType.QCASTLING) {
            return "0-0-0"; // Queenside castling notation
        }

        // If it's a pawn move
        if (originalPosition.getPiece().getType() == PAWN) {
            // If it's a regular pawn move (no capture)
            if (this.moveType == MoveType.MOVE) {
                return newPosition.toString(); // Just the destination for a pawn move
            }
            // If it's a pawn capture
            if (this.moveType == MoveType.CAPTURE) {
                return originalPosition.toString().charAt(0) + "x" + newPosition.toString(); // Capture notation (e.g., exd5)
            }
            // If it's a pawn promotion
            if (this.moveType == MoveType.PROMOTE) {
                return newPosition.toString() + "=" + extraData; // Pawn promotion
            }
        }

        // For all other pieces (non-pawn moves)
        String piece = "";
        switch (originalPosition.getPiece().getType()) {
            case KING:
                piece = "K"; // King
                break;
            case QUEEN:
                piece = "Q"; // Queen
                break;
            case ROOK:
                piece = "R"; // Rook
                break;
            case BISHOP:
                piece = "B"; // Bishop
                break;
            case KNIGHT:
                piece = "N"; // Knight (explicitly using "N" to avoid confusion with King)
                break;
            default:
                break;
        }

        // If it's a capture move (non-pawn)
        if (this.moveType == MoveType.CAPTURE) {
            return piece + "x" + newPosition.toString(); // Regular capture move
        }

        // If it's a regular move (non-pawn)
        if (this.moveType == MoveType.MOVE) {
            return piece + newPosition.toString(); // Regular move notation
        }

        // If it's a check move
        if (this.moveType == MoveType.CHECK) {
            return piece + newPosition.toString() + "+"; // Check notation
        }

        // If it's a checkmate move
        if (this.moveType == MoveType.CHECKMATE) {
            return piece + newPosition.toString() + "#"; // Checkmate notation
        }

        // Default case (shouldn't happen unless the moveType is undefined)
        return "";
    }
}
