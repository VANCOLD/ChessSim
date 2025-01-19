package at.chess.chesssimulator.board.ui;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;

/**
 * Represents a single tile on the chessboard in the user interface.
 * Each tile is a graphical component with a background, optional indicator, and image. It supports various visual updates
 * such as color changes, opacity adjustments, and indicator toggling.
 *
 * <p>This class is a {@link StackPane} that contains:</p>
 * <ul>
 *   <li>A background {@link Rectangle} representing the tile's color.</li>
 *   <li>An optional {@link Circle} indicator that can be toggled on/off to represent possible moves or selections.</li>
 *   <li>An optional {@link ImageView} that displays a chess piece image.</li>
 * </ul>
 *
 * <p>Additionally, it displays row and column labels on the board for reference.</p>
 */
public class ChessBoardTilePane extends StackPane {

    private final Rectangle background;
    private final Circle indicator;
    private final Color defaultColor;

    @Getter
    private ImageView image;

    @Getter
    private boolean indicatorOn;

    /**
     * Constructs a new {@link ChessBoardTilePane} for a given row and column on the chessboard.
     * The tile's color is determined based on the row and column index, and row/column labels are added if applicable.
     *
     * @param row The row index of the tile (0-based).
     * @param col The column index of the tile (0-based).
     */
    public ChessBoardTilePane(int row, int col) {
        super();

        // Determine the default color of the tile based on its position
        this.defaultColor = (row + col) % 2 == 0 ? getTileColor1() : getTileColor2();
        this.indicatorOn = false;
        this.image = null;

        // Initialize background rectangle
        this.background = new Rectangle(getTileWidth(), getTileHeight());
        this.background.setFill(this.defaultColor);
        this.background.setMouseTransparent(true);

        // Initialize indicator circle
        this.indicator = new Circle(getTileWidth() / 8.0);
        this.indicator.setMouseTransparent(true);
        this.indicator.setFill(Color.TRANSPARENT);

        // Add background and indicator to the stack pane
        this.getChildren().addAll(this.background, this.indicator);

        // Add column label (for the last column)
        if (col == getCols() - 1) {
            String textLabel = String.valueOf((char) ('a' + row));
            Color color = row % 2 == 0 ? getTileColor1() : getTileColor2();
            Text text = new Text(textLabel);
            text.setStyle("-fx-font-size: 14px;");
            text.setStroke(color);
            StackPane.setAlignment(text, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(text, new Insets(0, 2.5, 1.125, 0));
            this.getChildren().add(text);
        }

        // Add row label (for the first row)
        if (row == 0) {
            String textLabel = String.valueOf(col + 1);
            Color color = col % 2 == 0 ? getTileColor2() : getTileColor1();
            Text text = new Text(textLabel);
            text.setStyle("-fx-font-size: 14px;");
            text.setStroke(color);
            StackPane.setAlignment(text, Pos.TOP_LEFT);
            StackPane.setMargin(text, new Insets(2.5, 0, 0, 2.5));
            this.getChildren().add(text);
        }
    }

    /**
     * Sets the color of the tile background.
     *
     * @param color The new color to set for the tile background.
     */
    public void setColor(Color color) {
        this.background.setFill(color);
    }

    /**
     * Resets the tile's background color to its default color.
     */
    public void resetColor() {
        this.background.setFill(this.defaultColor);
    }

    /**
     * Toggles the visibility of the indicator on the tile.
     * If the indicator is off, it will be turned on with the color representing a possible move.
     * If the indicator is on, it will be turned off (set to transparent).
     */
    public void toggleIndicator() {
        this.indicatorOn = !this.indicatorOn;

        if (this.indicatorOn) {
            this.indicator.setFill(getPossibleMoveColor());
        } else {
            this.indicator.setFill(Color.TRANSPARENT);
        }
    }

    /**
     * Resets the indicator to its transparent state (off).
     */
    public void resetIndicator() {
        this.indicatorOn = false;
        this.indicator.setFill(Color.TRANSPARENT);
    }

    /**
     * Checks if the tile's background color is still set to the default color.
     *
     * @return {@code true} if the tile's color is the default color; {@code false} otherwise.
     */
    public boolean defaultColorSet() {
        return this.background.getFill().equals(this.defaultColor);
    }

    /**
     * Sets the opacity of the tile's image.
     *
     * @param opacity The opacity value to set for the image (between 0.0 and 1.0).
     */
    public void opacity(double opacity) {
        opacity = Math.clamp(opacity, 0.0, 1.0);
        this.image.setOpacity(opacity);
    }

    /**
     * Sets the image on the tile, replacing any previous image.
     * The image is displayed on top of the tile background.
     *
     * @param image The {@link ImageView} to set as the tile's image.
     */
    public void setImage(ImageView image) {
        this.image = image;
        this.image.setMouseTransparent(true); // Prevent mouse interactions with the image
        this.getChildren().add(image); // Add the image to the tile
    }

    /**
     * Resets the tile's image by removing it from the tile.
     */
    public void resetImage() {
        this.getChildren().remove(this.image);
        this.image = null;
    }

    /**
     * Resets the opacity of the tile's image to full opacity (1.0).
     */
    public void resetOpacity() {
        this.image.setOpacity(1.0);
    }

    /**
     * Sets the background color of the tile to indicate that a king is in check.
     * If the tile is in check, the color is changed; otherwise, the color is reset to its default state.
     *
     * @param inCheck {@code true} if the tile should be marked as in check; {@code false} to reset the color.
     */
    public void setCheck(boolean inCheck) {
        if (inCheck) {
            this.background.setFill(getCheckedTileColor());
        } else {
            this.resetColor();
        }
    }
}