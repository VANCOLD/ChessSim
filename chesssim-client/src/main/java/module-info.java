module at.chess.chesssimulator {
        requires org.slf4j;
        requires javafx.controls;
        requires javafx.fxml;
        requires static lombok;
    requires ch.qos.logback.classic;
    requires java.desktop;
    requires javafx.media;

    // Export the board package for FXML access
        exports at.chess.chesssimulator.board to javafx.fxml;

        // Export the main package if needed for starting the application
        exports at.chess.chesssimulator;

        // Open the package to reflection (if FXML needs more access, like private fields or methods)
        opens at.chess.chesssimulator.board to javafx.fxml;
        opens at.chess.chesssimulator.piece;
    opens at.chess.chesssimulator.controller to javafx.fxml;
    exports at.chess.chesssimulator.board.ui to javafx.fxml;
    opens at.chess.chesssimulator.board.ui to javafx.fxml;
    exports at.chess.chesssimulator.controller;
    exports at.chess.chesssimulator.controller.popup;
    opens at.chess.chesssimulator.controller.popup to javafx.fxml;
}
