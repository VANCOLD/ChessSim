package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import lombok.Setter;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;


@Setter
public class ChessBoard {

    private Position[][] board; // 8x8 array for the chessboard

    private static ChessBoard instance;

    private ChessBoard() {

        board = new Position[getRows()][getCols()];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                var position = new Position(i,j);
                board[i][j] = position;
            }
        }
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
        }
    }

    public void placePiece(Position pos, ChessPiece piece) {
        if(isInBounds(pos)) {
            board[pos.getRow()][pos.getCol()].setPiece(piece);
        }
    }

    public boolean isOccupied(int row, int col) {
        if(isInBounds(row, col)) {
            return board[row][col].getPiece() != null;
        } else {
            return false;
        }
    }

    public boolean isOccupied(Position pos) {
        if (isInBounds(pos)) {
            return board[pos.getRow()][pos.getCol()].getPiece() != null;
        } else {
            return false;
        }
    }

    public ChessPiece getPieceAt(int row, int col) {
        if (isInBounds(row, col)) {
            return board[row][col].getPiece();
        } else {
            return null;
        }
    }

    public ChessPiece getPieceAt(Position pos) {
        if (isInBounds(pos)) {
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

    public void clearPosition(int row, int col) {
        if (isInBounds(row, col)) {
            board[row][col].setPiece(null);
        }
    }

    public void clearPosition(Position pos) {
        if (isInBounds(pos)) {
            board[pos.getRow()][pos.getCol()].setPiece(null);
        }
    }
}
