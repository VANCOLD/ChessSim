package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import javafx.fxml.FXML;
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
import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;
import static at.chess.chesssimulator.utils.Constants.CSV_FILE_PATH;

public class BoardController {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private ChessBoard chessBoard;
    private List<ChessBoardTilePane> possibleTiles;
    private List<Position> possiblePositions;

    private ChessBoardTilePane selectedTile;
    private Position selectedPosition;
    private ChessBoardTilePane lastTile = null;
    private Position lastPosition;

    @FXML
    private Pane container;
    @FXML
    private ImageView draggedImage;
    @FXML
    private ChessBoardPane chessBoardPane;

    @FXML
    public void initialize() {
        chessBoard = ChessBoard.getInstance();
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
            chessBoardPane.setImageOfTile(row, col, piece.getImage());
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

        if(selectedTile != null && selectedPosition != null) {

            this.container.getChildren().remove(draggedImage);
            this.draggedImage = null;
            selectedTile.resetOpacity();

            lastPosition = selectedPosition;
            lastTile = selectedTile;
        }

        Position clickedPosition = getPositionFromMouseEvent(pressed);
        selectedPosition = clickedPosition;
        selectedTile = chessBoardPane.get(clickedPosition.getRow(), clickedPosition.getCol());
        selectedTile.opacity(getDragOpacity());

        prepareDragImage(selectedTile, pressed);
        setupPossibleMoves(clickedPosition);
    }

    private void mouseReleased(MouseEvent release) {

    }

    private void mouseMoved(MouseEvent moved) {
        if (draggedImage != null) {
            draggedImage.setTranslateX(moved.getX() - (draggedImage.getImage().getWidth() / 2.0));
            draggedImage.setTranslateY(moved.getY() - (draggedImage.getImage().getHeight() / 2.0));
        }
    }


    private Position getPositionFromMouseEvent(MouseEvent event) {
        int row = (int) event.getX() / getTileWidth();
        int col = (int) event.getY() / getTileHeight();
        return new Position(row, col);
    }

    private void prepareDragImage(ChessBoardTilePane tile, MouseEvent pressed) {
        draggedImage = new ImageView(tile.getImage().getImage());
        draggedImage.setTranslateX(pressed.getX() - (draggedImage.getImage().getWidth() / 2.0));
        draggedImage.setTranslateY(pressed.getY() - (draggedImage.getImage().getHeight() / 2.0));
        draggedImage.setVisible(true);
        container.getChildren().add(draggedImage);
    }

    private void setupPossibleMoves(Position newPos) {
        ChessPiece piece = chessBoard.getPieceAt(newPos.getRow(), newPos.getCol());
        possiblePositions = piece.getMovementRange(chessBoard.getPosition(newPos.getRow(), newPos.getCol()));
        possibleTiles = possiblePositions.stream()
                .map(pos -> chessBoardPane.get(pos.getRow(), pos.getCol()))
                .toList();
        possibleTiles.forEach(ChessBoardTilePane::toggleIndicator);
    }
}
