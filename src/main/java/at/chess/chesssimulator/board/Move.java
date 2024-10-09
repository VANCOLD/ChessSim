package at.chess.chesssimulator.board;

import at.chess.chesssimulator.board.enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Move {

    Position originalPosition;
    Position newPosition;
    MoveType moveType;
}
