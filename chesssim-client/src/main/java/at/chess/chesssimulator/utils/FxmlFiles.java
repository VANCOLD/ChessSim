package at.chess.chesssimulator.utils;

import java.net.URL;

import static at.chess.chesssimulator.utils.Constants.FXML_PATH;

/**
 * Enum representing various FXML files used in the application.
 * Provides a method to retrieve the URL of the associated FXML file.
 */
public enum FxmlFiles {

    /**
     * Represents the main application layout.
     */
    MAIN(FXML_PATH + "main.fxml"),

    /**
     * Represents the chessboard view.
     */
    BOARD(FXML_PATH + "chessboard.fxml"),

    /**
     * Represents the settings view.
     */
    SETTINGS(FXML_PATH + "settings.fxml"),

    /**
     * Represents the replay view.
     */
    REPLAY(FXML_PATH + "replay.fxml");

    /**
     * The file path of the FXML file.
     */
    private final String fileName;

    /**
     * Constructs an {@code FxmlFiles} enum constant with the specified file name.
     *
     * @param fileName The path to the FXML file relative to the resource folder.
     */
    FxmlFiles(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retrieves the {@link URL} of the associated FXML file.
     *
     * @return The {@link URL} pointing to the FXML file, or {@code null} if the file cannot be found.
     */
    public URL getFile() {
        return getClass().getResource(fileName);
    }
}
