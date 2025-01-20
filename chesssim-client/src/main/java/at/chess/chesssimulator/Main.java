package at.chess.chesssimulator;

import at.chess.chesssimulator.utils.FxmlFiles;
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

        printLogoAndVersion();
        Parent root = FXMLLoader.load(FxmlFiles.MAIN.getFile());
        primaryStage.setTitle("Chessboard Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        logger.info("Loading MainController into JavaFX stage");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void printLogoAndVersion() {

        String logo = """ 
                
                
                  /$$$$$$  /$$                                           /$$$$$$  /$$                                    /$$$$$$  /$$ /$$                       /$$
                 /$$__  $$| $$                                          /$$__  $$|__/                                   /$$__  $$| $$|__/                      | $$
                | $$  \\__/| $$$$$$$   /$$$$$$   /$$$$$$$ /$$$$$$$      | $$  \\__/ /$$ /$$$$$$/$$$$                     | $$  \\__/| $$ /$$  /$$$$$$  /$$$$$$$  /$$$$$$
                | $$      | $$__  $$ /$$__  $$ /$$_____//$$_____/      |  $$$$$$ | $$| $$_  $$_  $$       /$$$$$$      | $$      | $$| $$ /$$__  $$| $$__  $$|_  $$_/
                | $$      | $$  \\ $$| $$$$$$$$|  $$$$$$|  $$$$$$        \\____  $$| $$| $$ \\ $$ \\ $$      |______/      | $$      | $$| $$| $$$$$$$$| $$  \\ $$  | $$
                | $$    $$| $$  | $$| $$_____/ \\____  $$\\____  $$       /$$  \\ $$| $$| $$ | $$ | $$                    | $$    $$| $$| $$| $$_____/| $$  | $$  | $$ /$$
                |  $$$$$$/| $$  | $$|  $$$$$$$ /$$$$$$$//$$$$$$$/      |  $$$$$$/| $$| $$ | $$ | $$                    |  $$$$$$/| $$| $$|  $$$$$$$| $$  | $$  |  $$$$/
                 \\______/ |__/  |__/ \\_______/|_______/|_______/        \\______/ |__/|__/ |__/ |__/                     \\______/ |__/|__/ \\_______/|__/  |__/   \\___/
                (Version 1.0.0)
                """;

        logger.info(logo);

    }
}