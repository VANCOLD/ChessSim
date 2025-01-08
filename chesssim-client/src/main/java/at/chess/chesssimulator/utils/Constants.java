package at.chess.chesssimulator.utils;

import java.net.URL;
import java.util.HashMap;

/**
 * A utility class that contains constant values used throughout the chess simulator application.
 * <p>
 * This class is designed to hold various configuration paths, image paths, and other constants
 * that are referenced in different parts of the application. All fields in this class are public
 * and static, allowing them to be accessed without instantiating the class.
 * </p>
 */
public class Constants {

    /**
     * The path to the configuration files.
     * <p>
     * This path needs a dash since the config only contains files that are referenced within it,
     * and we load it as a stream rather than as a resource.
     * </p>
     */
    public static final String CONFIG_PATH = "/config";

    /**
     * The base path for images of chess pieces.
     * <p>
     * The path does not start with a '/' as it only checks for resources without requiring one.
     * </p>
     */
    public static final String IMAGE_PATH = "pieces";

    /**
     * The base path for FXML files.
     * <p>
     * The path does denote the location of the fxml files in the project.
     * </p>
     */
    public static final String FMXL_PATH = "/fxml/";

    /**
     * The path to the default layout CSV file for the chessboard configuration.
     */
    public static final String FEN_FILE_PATH = CONFIG_PATH + "/default_layout.fen";

    /**
     * The filename of the placeholder image used for empty squares on the chessboard.
     */
    public static final String PLACEHOLDER_IMAGE = "placeholder.png";

    /**
     * The location of the board configuration file, which is determined by the user's working directory.
     */
    public static final String CONFIG_LOCATION = System.getProperty("user.dir") + "board.config";

    /**
     * An array containing the image folders to check for loading piece images.
     */
    public static final String[] IMAGE_FOLDERS_TO_CHECK = {IMAGE_PATH};
}