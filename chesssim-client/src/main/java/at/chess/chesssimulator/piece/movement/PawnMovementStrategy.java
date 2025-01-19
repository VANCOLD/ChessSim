package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.config.ChessBoardConfig;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import lombok.Setter;

import static at.chess.chesssimulator.board.utils.PositionUtils.*;
import static at.chess.chesssimulator.piece.enums.PieceColor.*;
import static at.chess.chesssimulator.board.utils.Directions.*;

import java.util.ArrayList;
import java.util.List;

@Setter
public class PawnMovementStrategy extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position currentPosition) {
        List<Position> possiblePositions = new ArrayList<>();

        // depending on the color of the piece, the direction is different
        PieceColor pieceColor = currentPosition.getPiece().getColor();
        Position direction = pieceColor == BLACK ? DOWN.getVector() : UP.getVector();

        boolean firstMove = currentPosition.getPiece().isFirstMove();

        // Getting the possible moves; forward once, twice and diagonal left and right
        Position move1 = addVector(currentPosition, direction);
        Position move2 = addVector(move1, direction);
        Position moveDiag1 = addVector(currentPosition, addVector(direction, RIGHT.getVector()));
        Position moveDiag2 = addVector(currentPosition, addVector(direction, LEFT.getVector()));

        // If the move is in bounds and the position is not occupied, add it to the list of possible moves
        if (isInBounds(move1) && !chessBoard.isOccupied(move1)) {
            possiblePositions.add(move1);
        }

        // If it is the first move of the pawn, it can move two squares forward
        if (firstMove) {
            if (isInBounds(move2) && !chessBoard.isOccupied(move2)) {
                possiblePositions.add(move2);
            }
        }

        // If the diagonal move is in bounds and the position is occupied by an enemy piece, add it to the list of possible moves
        if(isInBounds(moveDiag1) && chessBoard.isOccupied(moveDiag1) && chessBoard.getPieceAt(moveDiag1).getColor() != pieceColor) {
            possiblePositions.add(moveDiag1);
        }

        // If the diagonal move is in bounds and the position is occupied by an enemy piece, add it to the list of possible moves
        if(isInBounds(moveDiag2) && chessBoard.isOccupied(moveDiag2) && chessBoard.getPieceAt(moveDiag2).getColor() != pieceColor) {
            possiblePositions.add(moveDiag2);
        }

        logger.debug("Pawn movement - found the following possible moves: {}", possiblePositions);
        return possiblePositions;
    }

    public boolean canPromote(Position currentPosition, Position newPosition) {

        // If the pawn reaches the end of the board, it can be promoted
        ChessPiece piece = chessBoard.getPieceAt(currentPosition);
        logger.info("Piece: {} Color: {}", piece.getType(), piece.getColor());
        logger.info("New Position: {}; col: {}", newPosition, newPosition.getCol());
        boolean canPromote = (newPosition.getCol() == ChessBoardConfig.getCols() && piece.getColor() == WHITE)
                             || (newPosition.getCol() == ChessBoardConfig.getRows()  && piece.getColor() == BLACK);
        return canPromote;
    }
}