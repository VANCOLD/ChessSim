package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

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

        PieceColor pieceColor = chessBoard.getPieceAt(piececPosition).getColor();
        PieceColor captureColor = chessBoard.getPieceAt(capturePosition).getColor();

        if (captureColor == null || pieceColor == captureColor) {
            return false;
        }

        return getPossibleMoves(chessBoard.getPosition(piececPosition)).stream().anyMatch(pos ->
            sameCoordinates(pos, capturePosition)
        );
    }

    @Override
    public boolean isCheckmate(Position newPosition) {
        PieceColor turn = chessBoard.getTurn();

        return Arrays.stream(Directions.values()).anyMatch(direction -> {
            // We check each direction of the king to see if there is a piece that can capture it
            Position bound = PositionUtils.getBound(direction, newPosition);
            ChessPiece toCheck = chessBoard.getPieceAt(bound);

            return toCheck != null && toCheck.getColor() != turn && canCapture(bound, newPosition);
        });
    }
}
