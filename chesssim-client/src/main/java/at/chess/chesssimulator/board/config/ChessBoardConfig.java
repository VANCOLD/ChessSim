package at.chess.chesssimulator.board.config;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static at.chess.chesssimulator.utils.Constants.CONFIG_LOCATION;
import static at.chess.chesssimulator.utils.Constants.PLACEHOLDER_IMAGE;

/**
 * Configuration class for a chessboard in the chess simulator application.
 * <p>
 * This class is responsible for defining and loading various properties related to the chessboard,
 * such as tile size, board dimensions, colors, and a placeholder image for the pieces.
 * The configuration can either use default values or load settings from an external configuration file.
 * </p>
 *
 * <p>
 * If a config file is found at the specified location, the class loads properties from the file and overwrites
 * the default values. If no config file is found or an error occurs, the class uses default values defined in the static block.
 * </p>
 *
 * I need to denote the getter on the instance variables, otherwise Lombok will not generate the getters.
 */
@AllArgsConstructor
public class ChessBoardConfig {

    /**
     * Logger for the class to record events and information.
     */
    protected static final Logger logger = LoggerFactory.getLogger(ChessBoardConfig.class);

    // Default configuration values
    private static final int TILE_WIDTH = 70;
    private static final int TILE_HEIGHT = 70;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final double DRAG_OPACITY = 0.5;
    private static final Color TILE_COLOR_1 = Color.web("#D2B08B");
    private static final Color TILE_COLOR_2 = Color.web("#A17960");
    private static final Color SELECTED_TILE_COLOR = Color.web("#61673F");
    private static final Color POSSIBLE_MOVE_COLOR = Color.web("#7A8254");
    private static final Color CHECKED_TILE_COLOR = Color.web("#F33E42");

    // Configuration property keys for the external config file
    private static final String TILE_WIDTH_NAME = "tile_width";
    private static final String TILE_HEIGHT_NAME = "tile_height";
    private static final String ROWS_NAME = "rows";
    private static final String COLS_NAME = "cols";
    private static final String DRAG_OPACITY_NAME = "opacity";
    private static final String TILE_COLOR_1_NAME = "tile_color_1";
    private static final String TILE_COLOR_2_NAME = "tile_color_2";
    private static final String SELECTED_TILE_COLOR_NAME = "selected_tile_color";
    private static final String POSSIBLE_MOVE_COLOR_NAME = "possible_move_color";
    private static final String PLACEHOLDER_IMAGE_NAME = "placeholder_image";
    private static final String CHECKED_TILE_COLOR_NAME = "checked_tile_color";

    // Loaded or default configuration values
    @Getter
    private static int tileWidth, tileHeight, rows, cols;

    @Getter
    private static double dragOpacity;

    @Getter
    private static Color tileColor1, tileColor2, selectedTileColor, possibleMoveColor, checkedTileColor;
    @Getter
    private static String placeholderImage;

    static {
        // Initialize default values and attempt to load external configuration
        logger.info("Initializing ChessBoardConfig (static block)");
        tileWidth = TILE_WIDTH;
        tileHeight = TILE_HEIGHT;
        rows = ROWS;
        cols = COLS;
        dragOpacity = DRAG_OPACITY;
        tileColor1 = TILE_COLOR_1;
        tileColor2 = TILE_COLOR_2;
        selectedTileColor = SELECTED_TILE_COLOR;
        possibleMoveColor = POSSIBLE_MOVE_COLOR;
        placeholderImage = PLACEHOLDER_IMAGE;
        checkedTileColor = CHECKED_TILE_COLOR;

        logger.info("Legend: tileWidth - tileHeight - rows - cols - opacity - tileColor1 - tileColor2 - selectedTileColor - possibleMoveColor");
        logger.info("Default values: {} - {} - {} - {} - {} - {} - {} - {} - {} - {}", tileWidth, tileHeight, rows, cols, dragOpacity,
                tileColor1, tileColor2, selectedTileColor, possibleMoveColor, checkedTileColor);

        // Attempt to load configuration from external file
        loadConfig();
    }

    /**
     * Loads the configuration from an external properties file.
     * <p>
     * If the configuration file is found at the {@code CONFIG_LOCATION}, it reads the properties and
     * overwrites the default values. If the file is not found or if an error occurs while reading,
     * the class continues to use the default values defined in the static block.
     * </p>
     */
    private static void loadConfig() {

        logger.info("Trying to load config from {}", CONFIG_LOCATION);
        File configFile = new File(CONFIG_LOCATION);


        if (!configFile.exists()) {
            logger.warn("Config file not found in {}. Using default configuration.", CONFIG_LOCATION);
            return;
        }

        logger.info("Found config, reading contents");
        try (FileInputStream input = new FileInputStream(configFile)) {
            Properties prop = new Properties();
            prop.load(input);

            // Parse and assign the values from the properties file; The values are predefined and others are ignored
            tileWidth = Integer.parseInt(prop.getProperty(TILE_WIDTH_NAME, "" + tileWidth));
            tileHeight = Integer.parseInt(prop.getProperty(TILE_HEIGHT_NAME, "" + tileHeight));
            rows = Integer.parseInt(prop.getProperty(ROWS_NAME, "" + rows));
            cols = Integer.parseInt(prop.getProperty(COLS_NAME, "" + cols));
            dragOpacity = Double.parseDouble(prop.getProperty(DRAG_OPACITY_NAME, "" + dragOpacity));
            tileColor1 = Color.web(prop.getProperty(TILE_COLOR_1_NAME, tileColor1.toString()));
            tileColor2 = Color.web(prop.getProperty(TILE_COLOR_2_NAME, tileColor2.toString()));
            selectedTileColor = Color.web(prop.getProperty(SELECTED_TILE_COLOR_NAME, selectedTileColor.toString()));
            possibleMoveColor = Color.web(prop.getProperty(POSSIBLE_MOVE_COLOR_NAME, possibleMoveColor.toString()));
            placeholderImage = prop.getProperty(PLACEHOLDER_IMAGE_NAME);
            checkedTileColor = Color.web(prop.getProperty(CHECKED_TILE_COLOR_NAME, checkedTileColor.toString()));

            logger.info("Config loaded from external file.");
        } catch (IOException | NumberFormatException ex) {
            logger.error("Error loading configuration from external file. Using default values.");
        }
    }
}