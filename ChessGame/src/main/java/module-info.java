module at.technikumwien.chessgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens at.technikumwien.chessgame to javafx.fxml;
    exports at.technikumwien.chessgame;
}