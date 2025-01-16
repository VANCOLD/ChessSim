package at.chess.chesssimulator.utils;

import java.net.URL;

import static at.chess.chesssimulator.utils.Constants.FMXL_PATH;

public enum FmxlFiles {
    MAIN( FMXL_PATH + "main.fxml"),
    BOARD(FMXL_PATH + "chessboard.fxml"),
    NETWORK_BROWSER(FMXL_PATH + "network_browser.fxml"),
    SETTINGS(FMXL_PATH + "settings.fxml"),
    REPLAY(FMXL_PATH + "replay.fxml");

    private final String fileName;

    FmxlFiles(String fileName) {
        this.fileName = fileName;
    }

    public URL getFile() {
        return getClass().getResource(fileName);
    }
}
