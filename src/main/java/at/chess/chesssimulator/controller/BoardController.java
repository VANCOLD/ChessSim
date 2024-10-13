package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.utils.Constants.CSV_FILE_PATH;

public class BoardController {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private ChessBoard chessBoard;
    private ChessBoardTilePane selectedTile;
    private List<ChessBoardTilePane> possibleTiles;

    @FXML
    private ChessBoardPane chessBoardPane;

    @FXML
    public void initialize() {

        this.chessBoard   = ChessBoard.getInstance();
        this.selectedTile = null;
        this.possibleTiles = null;
        this.loadBoard();

        this.chessBoardPane.setOnMouseClicked(this::mouseClicked);
        this.chessBoardPane.setOnMouseReleased(this::mouseReleased);
        this.chessBoardPane.setOnMouseDragged(this::mouseDragged);

    }

    private void loadBoard() {

        String csvFile = CSV_FILE_PATH;
        logger.info("Loading piece placements from {}", csvFile);

        String line;
        String csvSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile)))) {

            PieceColor color;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                logger.info("Reading line from csv: {}", line);
                color = PieceColor.getPieceColor(Integer.parseInt(values[0]));

                for (int i = 1; i < values.length; i++) {

                    PieceType type = PieceType.getPieceType(values[i].charAt(0));
                    int row = Integer.parseInt("" + values[i].charAt(1));
                    int col = Integer.parseInt("" + values[i].charAt(2));
                    ChessPiece piece = ChessPiece.generateChessPiece(color,type);

                    chessBoardPane.setImageOfTile(row, col, piece.getImage());
                    chessBoard.placePiece(row, col, piece);
                    logger.info("Placing piece {} {} row: {} - col: {}", color.name(), type.name(), row, col);
                }
            }
        } catch (IOException e) {
            logger.error("An error occurred while reading data from the csv {}", csvFile);
        }

        logger.info("Finished loading piece placements from the csv: {}", csvFile);
    }

    public void mouseClicked(MouseEvent click) {
        int rowClick = (int) click.getSceneX() / getTileWidth();
        int colClick = (int) click.getSceneY() / getTileHeight();


        if (selectedTile != null) {
            selectedTile.resetColor();

            if(possibleTiles != null) {
                possibleTiles.forEach(ChessBoardTilePane::toggleIndicator);
            }
        }

        if(!chessBoard.isOccupied(rowClick,colClick)) {
            System.out.println("Clicked at: (" + rowClick + "/" + colClick +")");
            selectedTile = null;
            possibleTiles = null;
            return;
        }

        selectedTile = chessBoardPane.get(rowClick,colClick);
        selectedTile.setColor(getSelectedTileColor());

        var piece = chessBoard.getPieceAt(rowClick,colClick);
        System.out.println(piece.getColor().name().toLowerCase() + " " + piece.getType().name().toLowerCase() + " (" + rowClick + "/" + colClick +")");

        var possiblePositions = piece.getMovementRange(chessBoard.getPosition(rowClick,colClick));

        /*
         * 1. filter all the position we acquired, and remove the ones that are occupied by a piece
         * 2. get the panes to toggle the indicators.
         */
        possibleTiles = possiblePositions
                .stream()
                .map(pos -> chessBoardPane.get(pos.getRow(), pos.getCol()))
                .toList();

        possibleTiles.forEach(ChessBoardTilePane::toggleIndicator);

    }

    public void mouseDragged(MouseEvent drag) {
        int rowDrag = (int) drag.getX() / getTileWidth();
        int colDrag = (int) drag.getY() / getTileHeight();
        logger.debug("Dragged mouse over field ({}/{})", rowDrag, colDrag);


    }

    public void mouseReleased(MouseEvent release) {
        int rowRelease = (int) release.getSceneX() / getTileWidth();
        int colRelease = (int) release.getSceneY() / getTileHeight();
        logger.debug("Released mouse over field ({}/{})", rowRelease, colRelease);
    }


}
