package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;

public class SelectCommand implements Command {

    private final ChessBoard chessBoard;
    private final Move move;

    public SelectCommand(ChessBoard chessBoard, Move move) {
        this.chessBoard = chessBoard;
        this.move = move;
    }

    @Override
    public void execute() {
        Position pos = move.getOriginalPosition();
        chessBoard.selectPosition(pos);
        pos.getPiece().getMovementStrategy().getPossibleMoves(pos).forEach(chessBoard::toggleIndicator);
    }

    @Override
    public void undo() {
        this.execute(); // just execute again, as it will reset the selection
    }
}
