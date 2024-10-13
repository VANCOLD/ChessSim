package at.chess.chesssimulator.board;

import at.chess.chesssimulator.board.enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a move on the chessboard, containing the original and new positions,
 * as well as the type of move being made.
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
@AllArgsConstructor
public class Move {

    /** The original position of the piece before the move. */
    Position originalPosition;

    /** The new position of the piece after the move. */
    Position newPosition;

    /** The type of move being made (e.g., regular move, capture, castling). */
    MoveType moveType;
}
