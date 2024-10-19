package at.chess.chesssimulator.board.ui;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.PositionUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

public class ChessBoardPane extends GridPane {

    private static final Logger logger = LoggerFactory.getLogger(ChessBoardPane.class);
    private final ChessBoardTilePane[][] tiles;

    public ChessBoardPane() {
        super();

        logger.info("Initializing ChessBoardPanes");
        this.tiles = new ChessBoardTilePane[getRows()][getCols()];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                var tile = new ChessBoardTilePane(i,j);
                tile.setMouseTransparent(true);
                tiles[i][j] = tile;
                this.add(tile,i,j);


            }
        }

        logger.info("Created ChessBoardPane with {} rows and {} columns of ChessBoardTilePanes", getRows(), getCols());
    }

    public void toggleTile(int row, int col) {

        boolean isTileSelected = !tiles[row][col].defaultColorSet();
        Color newColor = isTileSelected ? PositionUtils.getTileColor(row, col) : getSelectedTileColor();
        tiles[row][col].setColor(newColor);
    }

    public void toggleTile(Position pos) {
        toggleTile(pos.getRow(), pos.getCol());
    }

    public void toggleIndicator(int row, int col) {
        this.tiles[row][col].toggleIndicator();
    }

    public void toggleIndicator(Position pos) {
        toggleIndicator(pos.getRow(), pos.getCol());
    }

    public void setImage(int row, int col, Image image) {
        tiles[row][col].setImage(new ImageView(image));
    }

    public void setImage(Position pos, Image image) {
        setImage(pos.getRow(), pos.getCol(), image);
    }

    public void resetImage(int row, int col) {
        tiles[row][col].resetImage();
    }

    public void resetImage(Position pos) {
        resetImage(pos.getRow(), pos.getCol());
    }

    public boolean isSelected(Position releasedPosition) {
        return !tiles[releasedPosition.getRow()][releasedPosition.getCol()].defaultColorSet();
    }
}
