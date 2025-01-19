package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.Directions;
import at.chess.chesssimulator.board.utils.PositionUtils;

public class QueenCastlingCommand extends AbstractCommand {

    public QueenCastlingCommand(ChessBoard chessBoard, Move move) {
        super(chessBoard, move);
    }

    @Override
    public void execute() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.LEFT.getVector());
        chessBoard.movePiece(move.getOriginalPosition(), kingPosition);

        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        chessBoard.movePiece(move.getNewPosition(), rookPosition);
        chessBoard.getPieceAt(rookPosition).setFirstMove(false);
        chessBoard.getPieceAt(kingPosition).setFirstMove(false);
    }

    @Override
    public void undo() {

        Position kingPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        kingPosition = PositionUtils.addVector(kingPosition, Directions.LEFT.getVector());

        chessBoard.movePiece(kingPosition, move.getOriginalPosition());
        Position rookPosition = PositionUtils.addVector(move.getOriginalPosition(), Directions.LEFT.getVector());
        chessBoard.movePiece(rookPosition, move.getNewPosition());
        chessBoard.getPieceAt(rookPosition).setFirstMove(true);
        chessBoard.getPieceAt(kingPosition).setFirstMove(true);
    }
}
