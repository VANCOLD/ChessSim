package at.chess.chesssimulator.board;

import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.utils.FenNotation;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Setter;

import java.util.Stack;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.isInBounds;


@Setter
public class ChessBoard {

    /**
     * Logger for logging important events and debug information.
     */
    private static final Logger logger = LoggerFactory.getLogger(ChessBoard.class);

    /**
     * A 2D array representing the board, where each cell contains a {@code Position}.
     */
    private Position[][] board;

    /**
     * The currently selected position on the board, if any.
     */
    @Getter
    private Position selectedPosition;

    /**
     * A stack of positions marked as indicated (e.g., valid move targets).
     */
    private Stack<Position> indicatedPositions;

    /**
     * The color of the player whose turn it is to move.
     */
    @Getter
    private PieceColor turn;

    /**
     * Singleton instance of the chessboard.
     */
    private static ChessBoard instance;

    /**
     * Flag indicating whether the current player is in check.
     */
    @Getter
    private boolean inCheck;

    /**
     * Private constructor for initializing the chessboard with default dimensions.
     * The board is initialized with empty positions.
     */
    private ChessBoard() {
        board = new Position[getRows()][getCols()];
        this.selectedPosition = null;
        this.indicatedPositions = new Stack<>();
        this.inCheck = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                var position = new Position(i, j);
                board[i][j] = position;
            }
        }
        logger.info("Created ChessBoard with {} rows and {} columns", getRows(), getCols());
    }

    /**
     * Resets the singleton instance of the chessboard, allowing a new one to be created.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Loads the board state from the given FEN notation.
     *
     * @param fen The {@code FenNotation} object representing the board configuration.
     */
    public void loadBoard(FenNotation fen) {
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

    /**
     * Retrieves the singleton instance of the chessboard.
     *
     * @return The singleton {@code ChessBoard} instance.
     */
    public static ChessBoard getInstance() {
        if (instance == null) {
            instance = new ChessBoard();
        }
        return instance;
    }

    /**
     * Places a piece on the board at the specified position.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param piece The {@code ChessPiece} to place.
     */
    public void placePiece(int row, int col, ChessPiece piece) {
        if (isInBounds(row, col)) {
            board[row][col].setPiece(piece);
            board[row][col].setOccupied(true);
        }
    }

    /**
     * Places a piece on the board at the specified position.
     *
     * @param pos   The {@code Position} where the piece should be placed.
     * @param piece The {@code ChessPiece} to place.
     */
    public void placePiece(Position pos, ChessPiece piece) {
        placePiece(pos.getRow(), pos.getCol(), piece);
    }

    /**
     * Checks if the specified position is occupied by a piece.
     *
     * @param row The row index.
     * @param col The column index.
     * @return {@code true} if the position is occupied, {@code false} otherwise.
     */
    public boolean isOccupied(int row, int col) {
        return isInBounds(row, col) && board[row][col].isOccupied();
    }

    /**
     * Checks if the specified position is occupied by a piece of the given color.
     *
     * @param pos   The {@code Position} to check.
     * @param color The {@code PieceColor} to check against.
     * @return {@code true} if the position is occupied by a piece of the given color, {@code false} otherwise.
     */
    public boolean isOccupiedByColor(Position pos, PieceColor color) {
        return isOccupied(pos) && getPieceAt(pos) != null && getPieceAt(pos).getColor() == color;
    }

    /**
     * Retrieves the piece at the specified position.
     *
     * @param pos The {@code Position} to check.
     * @return The {@code ChessPiece} at the position, or {@code null} if the position is empty.
     */
    public ChessPiece getPieceAt(Position pos) {
        return getPieceAt(pos.getRow(), pos.getCol());
    }

    /**
     * Moves a piece from one position to another.
     *
     * @param originalPosition The original {@code Position} of the piece.
     * @param newPosition      The new {@code Position} of the piece.
     */
    public void movePiece(Position originalPosition, Position newPosition) {
        if (isInBounds(originalPosition) && isInBounds(newPosition)) {
            ChessPiece piece = getPieceAt(originalPosition);
            clearPosition(originalPosition);
            placePiece(newPosition, piece);

            if (piece.isFirstMove()) {
                piece.setFirstMove(false);
            }
        }
    }

    /**
     * Captures a piece by moving another piece to its position.
     *
     * @param originalPosition The original {@code Position} of the capturing piece.
     * @param capturePosition  The {@code Position} of the piece to be captured.
     */
    public void capturePiece(Position originalPosition, Position capturePosition) {
        if (isInBounds(originalPosition) && isInBounds(capturePosition)) {
            ChessPiece piece = getPieceAt(originalPosition);
            clearPosition(originalPosition);
            clearPosition(capturePosition);
            placePiece(capturePosition, piece);

            if (piece.isFirstMove()) {
                piece.setFirstMove(false);
            }
        }
    }


    /**
     * Retrieves the position of the king for the specified color.
     *
     * @param turn The {@code PieceColor} of the king to locate.
     * @return The {@code Position} of the king, or {@code null} if no king is found.
     */
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

    /**
     * Selects or deselects a position on the board. Only one position can be selected at a time.
     *
     * @param pos The {@code Position} to select or deselect.
     */
    public void selectPosition(Position pos) {
        resetTile();
        Position toSelect = this.getPosition(pos);
        toSelect.setSelected(!toSelect.isSelected());
        this.selectedPosition = toSelect;
    }

    /**
     * Toggles the indicator state of a position to show it as a valid move target.
     * The position is also added to the stack of indicated positions.
     *
     * @param pos The {@code Position} to mark as indicated.
     */
    public void toggleIndicator(Position pos) {
        Position toSelect = this.getPosition(pos);
        toSelect.setIndicator(true);
        this.indicatedPositions.push(toSelect);
    }

    /**
     * Resets the selection and indicators on the board.
     * Deselects the currently selected position and clears all indicated positions.
     */
    public void resetTile() {
        if (this.selectedPosition != null) {
            this.selectedPosition.setSelected(false);
            this.indicatedPositions.forEach(p -> p.setIndicator(false));
            this.indicatedPositions.clear();
        }
    }

    /**
     * Sets the check status for the current player and updates the king's position accordingly.
     *
     * @param inCheck {@code true} if the current player is in check, {@code false} otherwise.
     */
    public void setCheck(boolean inCheck) {
        this.inCheck = inCheck;
        PieceColor playerInCheck = PieceColor.getOppositeColor(turn);
        Position kingPos = this.getKingPosition(playerInCheck);
        board[kingPos.getRow()][kingPos.getCol()].setInCheck(inCheck);
    }

    /**
     * Clears the piece and resets the state of a position on the board.
     *
     * @param row The row index of the position to clear.
     * @param col The column index of the position to clear.
     */
    private void clearPosition(int row, int col) {
        if (isInBounds(row, col)) {
            board[row][col].setPiece(null);
            board[row][col].setOccupied(false);
            board[row][col].setSelected(false);
            board[row][col].setIndicator(false);
        }
    }

    /**
     * Clears the piece and resets the state of a position on the board.
     *
     * @param pos The {@code Position} to clear.
     */
    public void clearPosition(Position pos) {
        clearPosition(pos.getRow(), pos.getCol());
    }

    /**
     * Checks if a specific position is occupied.
     *
     * @param pos The {@code Position} to check.
     * @return {@code true} if the position is occupied, {@code false} otherwise.
     */
    public boolean isOccupied(Position pos) {
        return isOccupied(pos.getRow(), pos.getCol());
    }

    /**
     * Retrieves the piece at a specific row and column.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The {@code ChessPiece} at the specified position, or {@code null} if the position is empty.
     */
    public ChessPiece getPieceAt(int row, int col) {
        if (isInBounds(row, col)) {
            return board[row][col].getPiece();
        } else {
            return null;
        }
    }

    /**
     * Retrieves a position on the board based on its row and column indices.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The {@code Position} at the specified coordinates, or {@code null} if out of bounds.
     */
    public Position getPosition(int row, int col) {
        if (isInBounds(row, col)) {
            return board[row][col];
        } else {
            return null;
        }
    }

    /**
     * Retrieves a position on the board based on another position.
     *
     * @param pos The {@code Position} to locate.
     * @return The {@code Position} on the board, or {@code null} if out of bounds.
     */
    public Position getPosition(Position pos) {
        if (isInBounds(pos)) {
            return board[pos.getRow()][pos.getCol()];
        } else {
            return null;
        }
    }
}
