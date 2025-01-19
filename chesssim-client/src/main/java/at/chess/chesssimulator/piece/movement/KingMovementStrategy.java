package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;
import static at.chess.chesssimulator.piece.movement.MovementStrategyRegistry.getStrategy;

@Getter
@Setter
public class KingMovementStrategy extends AbstractStrategy {

    @Override
    public List<Position> getPossibleMoves(Position curPos) {

        List<Position> directions = Arrays.stream(Directions.values()).map(Directions::getVector).toList();
        List<Position> bufferList = directions
                .stream()
                .map(pos -> PositionUtils.addVector(pos, curPos))
                .filter(PositionUtils::isInBounds)
                .filter(Predicate.not(chessBoard::isOccupied))
                .collect(Collectors.toCollection(ArrayList::new)); // Collect into a mutable ArrayList


        logger.debug("King movement - found the following possible moves: {}", bufferList);

        return bufferList;
    }

    public boolean canQueenSideCastle(Position rookPos) {
        return rookPos.containsPiece() && canCastle(rookPos, Directions.LEFT);
    }

    public boolean canKingSideCastle(Position rookPos) {
        return rookPos.containsPiece() && canCastle(rookPos, Directions.RIGHT);
    }

    private boolean canCastle(Position rookPos, Directions direction) {

        Position kingPosition = chessBoard.getKingPosition(chessBoard.getTurn());
        Position rookPosition = chessBoard.getPosition(rookPos);

        boolean kingFirstMove = kingPosition.getPiece().isFirstMove();
        boolean rookFirstMove = rookPosition.getPiece().isFirstMove();
        Position rookDirection = PositionUtils.getBound(direction, rookPos);
        if (!kingFirstMove || !rookFirstMove || !sameCoordinates(rookPos, rookDirection)) {
            return false;
        }

        List<Position> positionsToCheck = PositionUtils.getPositionsBetweenRow(kingPosition, rookPosition, direction);

        return positionsToCheck.stream().noneMatch(chessBoard::isOccupied);
    }

    public boolean isCheckmate(ChessPiece piece, Position piecePosition) {

        PieceColor turn = PieceColor.getOppositeColor(chessBoard.getTurn());

        Position kingPosition = chessBoard.getKingPosition(turn);
        List<Position> possibleMoves = getPossibleMoves(kingPosition);

        Position checkPosition = PositionUtils.loadAndCopyPosition(piecePosition);
        checkPosition.setPiece(piece);

        boolean isCheck = getStrategy(piece.getType()).canCapture(checkPosition, kingPosition);
        boolean listNotEmpty = !possibleMoves.isEmpty();
        boolean isBlocked = possibleMoves.stream().allMatch(pos -> {
            List<Position> possibleChecks = getPossibleMoves(pos);

            // First we get all possible moves for each position that the king can take to get out of check
            for (Position checkPos : possibleChecks) {
                // For each of these moves we check all directions if any piece is checking the king as well
                List<Directions> directions = Arrays.stream(Directions.values()).toList();
                for (Directions direction : directions) {
                    Position bound = PositionUtils.getBound(direction, checkPos);

                    // If the position is occupied by a piece of the opponent, and it can capture the king, remove it from the list
                    if (chessBoard.isOccupied(bound)) {
                        Position piecePos = chessBoard.getPosition(bound);
                        if (piecePos.getColor() != turn) {
                            return getStrategy(piecePos.getPiece().getType()).canCapture(piecePos, checkPos);
                        }
                    }
                }
            }

            return false;
        });

        return isCheck && listNotEmpty && !isBlocked;

    }


    public boolean isCheck(ChessPiece piece, Position newPosition) {

        Position checkPosition = PositionUtils.loadAndCopyPosition(newPosition);
        checkPosition.setPiece(piece);

        PieceColor currentPlayer = chessBoard.getTurn();
        PieceColor otherPlayer = PieceColor.getOppositeColor(currentPlayer);
        Position kingPos = chessBoard.getKingPosition(otherPlayer);
        return getStrategy(piece.getType()).canCapture(checkPosition, kingPos);
    }
}