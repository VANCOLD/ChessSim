package at.chess.chesssimulator.board;

import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.piece.movement.PawnMovement;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Setter;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;


@Setter
public class ChessBoard {

    private static final Logger logger = LoggerFactory.getLogger(ChessBoard.class);

    private Position[][] board; // 8x8 array for the chessboard
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

    public static ChessBoard getInstance() {
        if (instance == null) {
            instance = new ChessBoard(); // Create the instance if it doesn't exist
        }
        return instance; // Return the single instance
    }


    public void placePiece(int row, int col, ChessPiece piece, ChessBoardTilePane tile) {
        if(isInBounds(row, col)) {
            board[row][col].setPiece(piece);
            board[row][col].setOccupied(true);
            piece.setTile(tile);
            tile.setImage(new ImageView(piece.getImage()));
        }
    }

    public void placePiece(Position pos, ChessPiece piece, ChessBoardTilePane tile) {
        placePiece(pos.getRow(), pos.getCol(), piece, tile);
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
        if (isInBounds(pos) && isOccupied(pos)) {
            return board[pos.getRow()][pos.getCol()].getPiece();
        } else {
            return null;
        }
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
            board[row][col].getPiece().getTile().resetImage();
            board[row][col].getPiece().setTile(null);
            board[row][col].setPiece(null);
            board[row][col].setOccupied(false);
        }
    }

    private void clearPosition(Position pos) {
        clearPosition(pos.getRow(), pos.getCol());
    }

    public void movePiece(Position originalPosition, Position newPosition, ChessBoardTilePane tile) {
        if (isInBounds(originalPosition) && isInBounds(newPosition)) {
            ChessPiece piece = getPieceAt(originalPosition);
            clearPosition(originalPosition);
            placePiece(newPosition, piece, tile);

            if (piece.getType() == PieceType.PAWN) {
                ((PawnMovement) piece.getMovementStrategy()).setFirstMove(false);
            }
        }
    }
}
