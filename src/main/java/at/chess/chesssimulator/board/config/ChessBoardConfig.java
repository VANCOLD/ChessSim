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

@AllArgsConstructor
public class ChessBoardConfig {

    protected static final Logger logger = LoggerFactory.getLogger(ChessBoardConfig.class);

    private static final int   TILE_WIDTH    = 70;
    private static final int   TILE_HEIGHT = 70;
    private static final int   ROWS = 8;
    private static final int   COLS = 8;
    private static final Color TILE_COLOR_1 = Color.web("#D2B08B");
    private static final Color TILE_COLOR_2 = Color.web("#A17960");
    private static final Color SELECTED_TILE_COLOR = Color.web("#61673F");
    private static final Color POSSIBLE_MOVE_COLOR = Color.web("#7A8254");
    private static final String PLACEHOLDER_IMAGE = "/pieces/placeholder.png";

    private static final String TILE_WIDTH_NAME  = "tile_width";
    private static final String TILE_HEIGHT_NAME = "tile_height";
    private static final String ROWS_NAME = "rows";
    private static final String COLS_NAME = "cols";
    private static final String TILE_COLOR_1_NAME = "tile_color_1";
    private static final String TILE_COLOR_2_NAME = "tile_color_2";
    private static final String SELECTED_TILE_COLOR_NAME = "selected_tile_color";
    private static final String POSSIBLE_MOVE_COLOR_NAME = "possible_move_color";
    private static final String PLACEHOLDER_IMAGE_NAME = "placeholder_image";

    private static final String CONFIG_LOCATION = "./config/board.config";


    @Getter
    private static int tileWidth, tileHeight, rows, cols;

    @Getter
    private static Color tileColor1, tileColor2, selectedTileColor, possibleMoveColor;

    @Getter
    private static String placeholderImage;


    static {

        logger.info("Initializing ChessBoardConfig (static block)");
        tileWidth = TILE_WIDTH;
        tileHeight = TILE_HEIGHT;
        rows = ROWS;
        cols = COLS;
        tileColor1 = TILE_COLOR_1;
        tileColor2 = TILE_COLOR_2;
        selectedTileColor = SELECTED_TILE_COLOR;
        possibleMoveColor = POSSIBLE_MOVE_COLOR;
        placeholderImage = PLACEHOLDER_IMAGE;

        logger.info("Legend: tileWidth - tileHeight - rows - cols - tileColor1 - tileColor2 - selectedTileColor - possibleMoveColor");
        logger.info("Default values: {} - {} - {} - {} - {} - {} - {} - {}", tileWidth, tileHeight, rows, cols,
                tileColor1, tileColor2, selectedTileColor, possibleMoveColor);

        loadConfig();
    }

    /**
     * Method is called on startup; If a config is found it simply overwrites the default values set by the static block!
     */
    private static void loadConfig() {

        logger.info("Trying to load config from {}", System.getProperty("user.dir") + CONFIG_LOCATION);
        String currentDir = System.getProperty("user.dir");
        File configFile = new File(currentDir + CONFIG_LOCATION);

        if (!configFile.exists()) {
            logger.warn("Config file not found in {}. Using default configuration.",  CONFIG_LOCATION);
            return;
        }

        logger.info("Found config, reading contents");
        try (FileInputStream input = new FileInputStream(configFile)) {
            Properties prop = new Properties();
            prop.load(input);

            // Parse and assign the values from the properties file
            tileWidth =  Integer.parseInt(prop.getProperty(TILE_WIDTH_NAME, "" + rows));
            tileHeight = Integer.parseInt(prop.getProperty(TILE_HEIGHT_NAME, ""+cols));
            rows = Integer.parseInt(prop.getProperty(ROWS_NAME, "" + rows));
            cols = Integer.parseInt(prop.getProperty(COLS_NAME, "" + cols));
            tileColor1 = Color.web(prop.getProperty(TILE_COLOR_1_NAME, tileColor1.toString()));
            tileColor2 = Color.web(prop.getProperty(TILE_COLOR_2_NAME, tileColor1.toString()));
            selectedTileColor = Color.web(prop.getProperty(SELECTED_TILE_COLOR_NAME, tileColor1.toString()));
            possibleMoveColor = Color.web(prop.getProperty(POSSIBLE_MOVE_COLOR_NAME, tileColor1.toString()));
            placeholderImage  = prop.getProperty(PLACEHOLDER_IMAGE_NAME);

            logger.info("Config loaded from external file.");
        } catch (IOException | NumberFormatException ex) {
            logger.error("Error loading configuration from external file. Using default values. ");
        }
    }
}