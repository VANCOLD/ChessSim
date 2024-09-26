package at.chess.chesssimulator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainController {

    private static final int   TILE_SIZE    = 80;
    private static final int   BOARD_SIZE   = 8;
    private static final Color TILE_COLOR_1 = new Color(0.490,0.580,0.365,1);
    private static final Color TILE_COLOR_2 = new Color(0.93, 0.93, 0.835,1);

    @FXML
    private GridPane chessBoard;

    @FXML
    public void initialize() {
        // Initialize the chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? TILE_COLOR_1 : TILE_COLOR_2);
                chessBoard.add(tile, col, row);
            }
        }
    }
}