package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.enums.MoveType;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.sound.SoundManager;
import at.chess.chesssimulator.sound.SoundType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.board.utils.PositionUtils.containsPosition;
import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;
import static at.chess.chesssimulator.utils.Constants.CSV_FILE_PATH;

public class BoardController {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private ChessBoard chessBoard;
    private SoundManager soundManager;

    private Position selectedPosition;
    private List<Position> possiblePositions;

    private boolean ignoreInput = false;

    private PieceColor turn = PieceColor.BLACK;

    @FXML
    private Pane container;
    @FXML
    private ImageView draggedImage;
    @FXML
    private ChessBoardPane chessBoardPane;
    private boolean hasSelectionHappened;

    @FXML
    public void initialize() {
        chessBoard = ChessBoard.getInstance();
        soundManager = SoundManager.getInstance();
        loadBoard();
        setMouseHandlers();
    }

    private void loadBoard() {

        logger.info("Loading piece placements from {}", CSV_FILE_PATH);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(CSV_FILE_PATH)))) {

            String line;
            while ((line = br.readLine()) != null) {
                logger.info("Reading line from csv: {}", line);
                initializePieces(line.split(";"));
            }

        } catch (IOException e) {
            logger.error("An error occurred while reading data from the csv {}", CSV_FILE_PATH);
        }
        logger.info("Finished loading piece placements from the csv: {}", CSV_FILE_PATH);
    }

    private void initializePieces(String[] values) {

        PieceColor color = PieceColor.getPieceColor(Integer.parseInt(values[0]));

        for (int i = 1; i < values.length; i++) {

            PieceType type = PieceType.getPieceType(values[i].charAt(0));
            int row = Character.getNumericValue(values[i].charAt(1));
            int col = Character.getNumericValue(values[i].charAt(2));
            ChessPiece piece = ChessPiece.generateChessPiece(color, type);
            chessBoardPane.setImage(row, col, piece.getImage());
            chessBoard.placePiece(row, col, piece);
            logger.info("Placing piece {} {} at row: {} - col: {}", color.name(), type.name(), row, col);

        }
    }

    private void setMouseHandlers() {

        chessBoardPane.setOnMousePressed(this::mousePressed);
        chessBoardPane.setOnMouseReleased(this::mouseReleased);
        chessBoardPane.setOnMouseDragged(this::mouseMoved);
        logger.info("Setting up Mouse Handlers");
    }

    /** ------------------ **/
    /** Mouse Event Logic  **/
    /** ------------------ **/

    private void mousePressed(MouseEvent pressed) {

        Position clickedPosition = getPositionFromMouseEvent(pressed);
        logger.info("Selected tile at position: {}", clickedPosition);

        if (selectedPosition != null) {

            if (containsPosition(possiblePositions, clickedPosition)) {

                makeMove(clickedPosition);
                ignoreInput = true;

            } else {

                if (!chessBoard.isOccupied(clickedPosition) || chessBoard.getPieceAt(clickedPosition).getColor() != turn) {
                    ignoreInput = true;
                    return;
                }

                if(sameCoordinates(selectedPosition, clickedPosition)) {
                    prepareDragImage(selectedPosition, pressed);
                } else if( chessBoard.getPieceAt(clickedPosition).getColor() == turn) {

                    resetSelection();
                    selectedPosition = clickedPosition;
                    chessBoardPane.toggleTile(selectedPosition);
                    setupPossibleMoves(selectedPosition);
                    prepareDragImage(selectedPosition, pressed);
                }
            }

        } else {

            selectedPosition = clickedPosition;
            chessBoardPane.toggleTile(selectedPosition);
            setupPossibleMoves(selectedPosition);
            prepareDragImage(selectedPosition, pressed);

        }

    }

    private void mouseReleased(MouseEvent released) {

        Position releasedPosition = getPositionFromMouseEvent(released);
        logger.info("Released tile at position: {}", releasedPosition);

        if(ignoreInput) {
            ignoreInput = false;
            return;
        }

        boolean isSamePosition = sameCoordinates(releasedPosition, selectedPosition);
        boolean isPossibleMove = containsPosition(possiblePositions, releasedPosition);


        if( !isSamePosition && isPossibleMove) {
            makeMove(releasedPosition);
        } else if(isSamePosition && !hasSelectionHappened) {
            resetDrag();
            hasSelectionHappened = true;
        } else {
            resetSelection();
            hasSelectionHappened = false;
        }
    }

    public Move generateMove(Position originalPosition, Position newPosition) {
        return new Move(originalPosition, newPosition, MoveType.DEFAULT);
    }

    private void makeMove(Position releasedPosition) {
        logger.info("Making move from {} to {}", selectedPosition, releasedPosition);
        chessBoardPane.setImage(releasedPosition, chessBoard.getPieceAt(selectedPosition).getImage());
        chessBoardPane.resetImage(selectedPosition);
        chessBoard.movePiece(selectedPosition, releasedPosition);
        resetSelection();
        soundManager.playSound(SoundType.MOVE);
    }

    private void mouseMoved(MouseEvent moved) {
        if (draggedImage != null && !ignoreInput) {
            draggedImage.setTranslateX(moved.getX() - (draggedImage.getImage().getWidth() / 2.0));
            draggedImage.setTranslateY(moved.getY() - (draggedImage.getImage().getHeight() / 2.0));
        }
    }

    private Position getPositionFromMouseEvent(MouseEvent event) {
        int row = (int) event.getX() / getTileWidth();
        int col = (int) event.getY() / getTileHeight();
        return new Position(row, col);
    }

    private void prepareDragImage(Position pos, MouseEvent pressed) {

        resetDrag();

        Image image = chessBoard.getPieceAt(pos.getRow(), pos.getCol()).getImage();
        draggedImage = new ImageView(image);
        draggedImage.setTranslateX(pressed.getX() - (draggedImage.getImage().getWidth() / 2.0));
        draggedImage.setTranslateY(pressed.getY() - (draggedImage.getImage().getHeight() / 2.0));
        draggedImage.setVisible(true);
        container.getChildren().add(draggedImage);
    }

    private void setupPossibleMoves(Position currentPos) {

        if(possiblePositions != null) {
            possiblePositions.forEach(pos -> chessBoardPane.toggleTile(pos));
            possiblePositions = null;
        }

        ChessPiece piece = chessBoard.getPieceAt(currentPos.getRow(), currentPos.getCol());
        possiblePositions = piece.getMovementRange(chessBoard.getPosition(currentPos.getRow(), currentPos.getCol()));
        possiblePositions.forEach(pos -> chessBoardPane.toggleIndicator(pos));
    }

    public void resetSelection() {

        ignoreInput = false;
        hasSelectionHappened = false;

        chessBoardPane.toggleTile(selectedPosition);
        selectedPosition = null;

        possiblePositions.forEach(pos -> chessBoardPane.toggleIndicator(pos));
        possiblePositions = null;
        resetDrag();
    }

    public void resetDrag() {

        container.getChildren().remove(draggedImage);
        draggedImage = null;
    }
}
