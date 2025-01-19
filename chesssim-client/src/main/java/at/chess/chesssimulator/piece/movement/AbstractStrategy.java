package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.piece.enums.PieceColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;

public abstract class AbstractStrategy implements MovementStrategy {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractStrategy.class);
    protected final ChessBoard chessBoard;

    public AbstractStrategy() {
        this.chessBoard = ChessBoard.getInstance();
    }

    @Override
    public boolean canMove(Position piecePosition, Position newPosition) {

        if (chessBoard.isOccupied(newPosition)) {
            return false;
        }

        // We always have to get the actual piece on the board, the params are always just empty positions
        Position boardPiece = chessBoard.getPosition(piecePosition);

        return getPossibleMoves(boardPiece).stream().anyMatch(pos -> sameCoordinates(pos, newPosition));
    }

    @Override
    public boolean canCapture(Position piececPosition, Position capturePosition) {

        if (!chessBoard.isOccupied(capturePosition)) {
            return false;
        }

        PieceColor pieceColor = piececPosition.getColor();
        PieceColor captureColor = capturePosition.getColor();

        if (captureColor == null || pieceColor == captureColor) {
            return false;
        }

        return getPossibleMoves(piececPosition).stream().anyMatch(pos ->
            sameCoordinates(pos, capturePosition)
        );
    }
}
