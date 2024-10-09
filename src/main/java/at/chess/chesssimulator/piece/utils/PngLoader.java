package at.chess.chesssimulator.piece.utils;

import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;


public class PngLoader {

    protected static final Logger logger = LoggerFactory.getLogger(PngLoader.class);

    public static ImageView loadImage(PieceColor color, PieceType type) {

        logger.info("Trying to load image for piece {} {}", color.name(), type.name());

        String imagePath = "/pieces/" + color.name().toLowerCase() + "_" + type.name().toLowerCase() + ".png";
        Image imageFile;

        try {
            imageFile = new Image(PngLoader.class.getResourceAsStream(imagePath));
        } catch (NullPointerException ex) {
            imageFile = new Image(PngLoader.class.getResourceAsStream(getPlaceholderImage()));
            logger.error("Coudln't load image {}, loaded placeholder {} instead", imagePath, "/pieces/placeholder.png");
        }

        logger.info("Found image: {}", imagePath);
        ImageView image = new ImageView(imageFile);
        image.setScaleX(1);
        image.setScaleY(1);
        image.setSmooth(false);

        logger.info("Successfully loaded the image for {} {} from file {}", color.name(), type.name(), imagePath);
        return image;
    }
}
