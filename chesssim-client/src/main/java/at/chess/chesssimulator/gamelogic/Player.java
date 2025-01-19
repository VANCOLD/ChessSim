package at.chess.chesssimulator.gamelogic;

import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;

public interface Player {
    void notifyTurn(PieceColor turn);
    void receiveMoveResult(Move move);
    void updateBoard();
    void sendMove(Position origin, Position target);
}