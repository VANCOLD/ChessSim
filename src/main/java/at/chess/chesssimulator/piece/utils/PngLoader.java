package at.chess.chesssimulator.piece.utils;

import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PngLoader {

    public static ImageView loadImage(PieceColor color, PieceType type) {

        String imagePath = "/pieces/" + color.name().toLowerCase() + "_" + type.name().toLowerCase() + ".png";
        Image imageFile = new Image(PngLoader.class.getResourceAsStream(imagePath));

        ImageView image = new ImageView(imageFile);
        image.setScaleX(1);
        image.setScaleY(1);
        image.setSmooth(false);
        return image;
    }
}
