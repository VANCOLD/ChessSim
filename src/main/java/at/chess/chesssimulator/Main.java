package at.chess.chesssimulator;

import at.chess.chesssimulator.board.config.ChessBoardConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {

        logger.info("Starting ChessBoardSim");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/chessboard.fxml"));
        primaryStage.setTitle("Chessboard Application");
        primaryStage.setScene(new Scene(root));

        logger.info("Loading MainController into JavaFX stage");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}