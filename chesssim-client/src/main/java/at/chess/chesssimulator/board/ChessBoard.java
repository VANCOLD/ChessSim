package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.piece.movement.PawnMovement;
import at.chess.chesssimulator.utils.FenNotation;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Setter;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;


@Setter
public class ChessBoard {

    private static final Logger logger = LoggerFactory.getLogger(ChessBoard.class);

    private Position[][] board;
    @Getter
    private PieceColor turn;
    private static ChessBoard instance;



    private ChessBoard() {

        board = new Position[getRows()][getCols()];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                var position = new Position(i, j);
                board[i][j] = position;
            }
        }
        logger.info("Created ChessBoard with {} rows and {} columns", getRows(), getCols());
    }

    public void loadBoard(FenNotation fen) {

        // Load the board from the given fen notation
        var boardConfig = fen.getBoard();

        for (int i = 0; i < boardConfig.length; i++) {
            for (int j = 0; j < boardConfig[i].length; j++) {
                var piece = boardConfig[i][j];
                if (piece != null) {
                    placePiece(i, j, piece);
                }
            }
        }

        turn = fen.getTurn();
    }

    public static ChessBoard getInstance() {
        if (instance == null) {
            instance = new ChessBoard(); // Create the instance if it doesn't exist
        }
        return instance; // Return the single instance
    }


    public void placePiece(int row, int col, ChessPiece piece) {
        if(isInBounds(row, col)) {
            board[row][col].setPiece(piece);
            board[row][col].setOccupied(true);
        }
    }

    public void placePiece(Position pos, ChessPiece piece) {
        placePiece(pos.getRow(), pos.getCol(), piece);
    }

    public boolean isOccupied(int row, int col) {
        if(isInBounds(row, col)) {
            return board[row][col].isOccupied();
        } else {
            return false;
        }
    }

    public boolean isOccupied(Position pos) {
        return isOccupied(pos.getRow(), pos.getCol());
    }

    public boolean isOccupiedByColor(Position pos, PieceColor color) {
        return isOccupied(pos) && getPieceAt(pos) != null && getPieceAt(pos).getColor() == color;
    }

    public ChessPiece getPieceAt(int row, int col) {
        if (isInBounds(row, col)) {
            return board[row][col].getPiece();
        } else {
            return null;
        }
    }

    public ChessPiece getPieceAt(Position pos) {
        return getPieceAt(pos.getRow(), pos.getCol());
    }

    public Position getPosition(int row, int col) {
        if (isInBounds(row, col)) {
            return board[row][col];
        } else {
            return null;
        }
    }

    public Position getPosition(Position pos) {
        if (isInBounds(pos)) {
            return board[pos.getRow()][pos.getCol()];
        } else {
            return null;
        }
    }

    private void clearPosition(int row, int col) {
        if (isInBounds(row, col)) {
            board[row][col].setPiece(null);
            board[row][col].setOccupied(false);
            board[row][col].setSelected(false);
            board[row][col].setIndicator(false);
        }
    }

    public void clearPosition(Position pos) {
        clearPosition(pos.getRow(), pos.getCol());
    }

    public void movePiece(Position originalPosition, Position newPosition) {
        if (isInBounds(originalPosition) && isInBounds(newPosition)) {
            ChessPiece piece = getPieceAt(originalPosition);
            clearPosition(originalPosition);
            placePiece(newPosition, piece);

            if (piece.getType() == PieceType.PAWN) {
                ((PawnMovement) piece.getMovementStrategy()).setFirstMove(false);
            }
        }
    }

    public void capturePiece(Position originalPosition, Position capturePosition) {
        if (isInBounds(originalPosition) && isInBounds(capturePosition)) {
            ChessPiece piece = getPieceAt(originalPosition);
            clearPosition(originalPosition);
            clearPosition(capturePosition);
            placePiece(capturePosition, piece);

            if (piece.getType() == PieceType.PAWN) {
                // This seems redundant but if the pawn hasnt moved and it's first move is a capture we have this extra if clause
                ((PawnMovement) piece.getMovementStrategy()).setFirstMove(false);
            }
        }
    }

    public Position getKingPosition(PieceColor turn) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                var position = getPosition(i, j);
                var piece = position.getPiece();
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == turn) {
                    return position;
                }
            }
        }
        return null;
    }

    public void selectPosition(Position pos) {
        Position toSelect = this.getPosition(pos);
        toSelect.setSelected(!toSelect.isSelected());
    }

    public void toggleIndicator(Position pos) {
        Position toSelect = this.getPosition(pos);
        toSelect.setIndicator(!toSelect.isIndicator());
    }
}
