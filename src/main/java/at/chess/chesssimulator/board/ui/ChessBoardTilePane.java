package at.chess.chesssimulator.board.ui;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;


public class ChessBoardTilePane extends StackPane {


    private final Rectangle background;
    private final Circle indicator;
    private final Color defaultColor;

    @Getter
    private ImageView image;

    @Getter
    private boolean indicatorOn;

    public ChessBoardTilePane(int row, int col) {
        super();

        this.defaultColor = (row + col) % 2 == 0 ? getTileColor1() : getTileColor2();
        this.indicatorOn = false;
        this.image = null;

        this.background = new Rectangle(getTileWidth(), getTileHeight());
        this.background.setFill(this.defaultColor);
        this.background.setMouseTransparent(true);

        this.indicator = new Circle(getTileWidth() / 8.0);
        this.indicator.setMouseTransparent(true);
        this.indicator.setFill(Color.TRANSPARENT);
        this.indicator.setMouseTransparent(true);

        this.getChildren().addAll(this.background, this.indicator);


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

    public void setColor(Color color) {
        this.background.setFill(color);
    }

    public void toggleIndicator() {
        this.indicatorOn = !this.indicatorOn;

        if(this.indicatorOn) {
            this.indicator.setFill(getPossibleMoveColor());
        } else {
            this.indicator.setFill(Color.TRANSPARENT);
        }
    }

    public boolean defaultColorSet() {
        return this.background.getFill().equals(this.defaultColor);
    }

    public void opacity(double opacity) {
        opacity = Math.clamp(opacity, 0.0, 1.0);
        this.image.setOpacity(opacity);
    }

    public void setImage(ImageView image) {
        this.image = image;
        this.image.setMouseTransparent(true);
        this.getChildren().add(image);
    }

    public void resetImage() {
        this.getChildren().remove(this.image);
        this.image = null;
    }

    public void resetOpacity() {
        this.image.setOpacity(1.0);
    }

    public void mouseDragged(MouseDragEvent drag) {
        int rowDrag = (int) drag.getX() / getTileWidth();
        int colDrag = (int) drag.getY() / getTileHeight();
        System.out.println("Dragged mouse over field (" + rowDrag + "/" + colDrag + ")");
    }
}
