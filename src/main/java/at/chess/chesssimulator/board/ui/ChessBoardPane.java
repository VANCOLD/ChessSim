package at.chess.chesssimulator.board.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

public class ChessBoardPane extends GridPane {

    private final ChessBoardTilePane[][] tiles;

    public ChessBoardPane() {
        super();
        this.tiles = new ChessBoardTilePane[getRows()][getCols()];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                var tile = new ChessBoardTilePane(i,j);
                tiles[i][j] = tile;
                this.add(tile,i,j);
            }
        }
    }

    public void setImageOfTile(int row, int col, ImageView image) {
        tiles[row][col].setImage(image);
    }

    public ChessBoardTilePane get(int row, int col) {
        return tiles[row][col];
    }
}
