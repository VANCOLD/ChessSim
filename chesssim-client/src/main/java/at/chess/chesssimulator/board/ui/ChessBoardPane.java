package at.chess.chesssimulator.board.ui;

import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.utils.PositionUtils;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

/**
 * Represents the chessboard UI component in a graphical user interface.
 * It manages the grid of tiles on the chessboard and provides methods for interacting with those tiles.
 * This class is a subclass of {@link GridPane}, and each tile is represented by an instance of {@link ChessBoardTilePane}.
 * The board layout consists of rows and columns, and tiles can be toggled, reset, and updated with visual indicators.
 *
 * <p>This class provides methods for:</p>
 * <ul>
 *   <li>Changing the color of tiles based on selection or interaction.</li>
 *   <li>Resetting tile colors and indicators.</li>
 *   <li>Handling the opacity of tiles (e.g., highlighting).</li>
 *   <li>Indicating check on the board.</li>
 * </ul>
 *
 * <p>It also provides utility methods for interacting with individual tiles using either row/column indices or {@link Position} objects.</p>
 */
public class ChessBoardPane extends GridPane {

    private static final Logger logger = LoggerFactory.getLogger(ChessBoardPane.class);
    private final ChessBoardTilePane[][] tiles;

    /**
     * Constructor to initialize the ChessBoardPane and set up the grid of tiles.
     * It creates a grid of {@link ChessBoardTilePane} objects and adds them to the {@link GridPane}.
     */
    public ChessBoardPane() {
        super();

        logger.info("Initializing ChessBoardPanes");
        this.tiles = new ChessBoardTilePane[getRows()][getCols()];

        // Initialize each tile in the grid
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                var tile = new ChessBoardTilePane(i, j);
                tile.setMouseTransparent(true); // Prevent mouse interaction with the tile
                tiles[i][j] = tile;
                this.add(tile, i, j); // Add the tile to the grid
            }
        }

        logger.info("Created ChessBoardPane with {} rows and {} columns of ChessBoardTilePanes", getRows(), getCols());
    }

    /**
     * Toggles the color of a specific tile based on its current state.
     * If the tile is selected, it will be reset to the default color; otherwise, it will be toggled to the selected color.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    public void toggleTile(int row, int col) {
        boolean isTileSelected = !tiles[row][col].defaultColorSet();
        Color newColor = isTileSelected ? PositionUtils.getTileColor(row, col) : getSelectedTileColor();
        tiles[row][col].setColor(newColor);
    }

    /**
     * Toggles the color of a specific tile based on its current state.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile to be toggled.
     */
    public void toggleTile(Position pos) {
        toggleTile(pos.getRow(), pos.getCol());
    }

    /**
     * Resets the color of a specific tile to its default state.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    public void resetTile(int row, int col) {
        tiles[row][col].resetColor();
    }

    /**
     * Resets the color of a specific tile to its default state.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile to be reset.
     */
    public void resetTile(Position pos) {
        resetTile(pos.getRow(), pos.getCol());
    }

    /**
     * Resets the indicator (if any) on a specific tile.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    public void resetIndicator(int row, int col) {
        tiles[row][col].resetIndicator();
    }

    /**
     * Resets the indicator (if any) on a specific tile.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile whose indicator is to be reset.
     */
    public void resetIndicator(Position pos) {
        resetIndicator(pos.getRow(), pos.getCol());
    }

    /**
     * Toggles the indicator (if any) on a specific tile.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    public void toggleIndicator(int row, int col) {
        this.tiles[row][col].toggleIndicator();
    }

    /**
     * Toggles the indicator (if any) on a specific tile.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile whose indicator is to be toggled.
     */
    public void toggleIndicator(Position pos) {
        toggleIndicator(pos.getRow(), pos.getCol());
    }

    /**
     * Sets the opacity of a specific tile.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     * @param opacity The opacity value to be applied to the tile (range: 0.0 to 1.0).
     */
    public void opacity(int row, int col, double opacity) {
        tiles[row][col].opacity(opacity);
    }

    /**
     * Sets the opacity of a specific tile.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile.
     * @param opacity The opacity value to be applied to the tile (range: 0.0 to 1.0).
     */
    public void opacity(Position pos, double opacity) {
        opacity(pos.getRow(), pos.getCol(), opacity);
    }

    /**
     * Resets the opacity of a specific tile to its default state.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    public void resetOpacity(int row, int col) {
        tiles[row][col].resetOpacity();
    }

    /**
     * Resets the opacity of a specific tile to its default state.
     * Uses the {@link Position} object to identify the tile.
     *
     * @param pos The {@link Position} of the tile whose opacity is to be reset.
     */
    public void resetOpacity(Position pos) {
        resetOpacity(pos.getRow(), pos.getCol());
    }

    /**
     * Gets the {@link ChessBoardTilePane} at a specific row and column.
     *
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     * @return The {@link ChessBoardTilePane} at the specified position.
     */
    public ChessBoardTilePane get(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Gets the {@link ChessBoardTilePane} at a specific position.
     *
     * @param pos The {@link Position} of the tile.
     * @return The {@link ChessBoardTilePane} at the specified position.
     */
    public ChessBoardTilePane get(Position pos) {
        return get(pos.getRow(), pos.getCol());
    }

    /**
     * Sets the "check" status on a specific tile, indicating the king is in check.
     *
     * @param pos The {@link Position} of the tile where check is to be indicated.
     */
    public void setCheck(Position pos) {
        setCheck(pos.getRow(), pos.getCol());
    }

    /**
     * Sets the "check" status on a specific tile, indicating the king is in check.
     *
     * @param row The row index of the tile where check is to be indicated.
     * @param col The column index of the tile where check is to be indicated.
     */
    public void setCheck(int row, int col) {
        tiles[row][col].setCheck(true);
    }
}
