module at.chess.chesssimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.chess.chesssimulator to javafx.fxml;
    exports at.chess.chesssimulator;
}