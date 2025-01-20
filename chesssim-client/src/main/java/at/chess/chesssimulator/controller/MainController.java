package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.network.NetworkPlayer;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.utils.FxmlFiles;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static at.chess.chesssimulator.utils.AlertUtil.showError;

/**
 * The {@code MainController} class handles the main menu logic of the Chess Simulator application.
 * It manages the actions for buttons like Local Play, Online Play, Replay, Settings, and Exit.
 * Additionally, it establishes a connection to a server for online play functionality.
 */
public class MainController {

    protected static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button localPlay;

    @FXML
    private Button onlinePlay;

    @FXML
    private Button replay;

    @FXML
    private Button settings;

    @FXML
    private Button exit;

    /**
     * Initializes the controller by setting up button actions and checking the server connection.
     */
    @FXML
    public void initialize() {

        localPlay.setOnAction(this::handleLocalPlay);
        onlinePlay.setOnAction(this::handleOnlinePlay);
        replay.setOnAction(this::watchReplay);
        settings.setOnAction(this::handleSettings);
        exit.setOnAction(this::handleExit);
    }

    /**
     * Handles the action for starting a local game.
     * Loads the chessboard UI and initializes the game logic.
     *
     * @param event The event triggered by clicking the Local Play button.
     */
    private void handleLocalPlay(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(FxmlFiles.BOARD.getFile());
            Parent newRoot = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.setTitle("Local Play");

            BoardController boardController = loader.getController();
            GameMaster gameMaster = new GameMaster(boardController, boardController);
            boardController.setGameMaster(gameMaster);
            boardController.setOnlyOnePlayer(false);
            boardController.setStage(stage);
            gameMaster.startGame();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOnlinePlay(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) {
            showError("Username Required", "Please enter a username to play online.");
            return;
        }

        Alert waitingAlert = new Alert(Alert.AlertType.INFORMATION);
        waitingAlert.setTitle("Waiting for Player");
        waitingAlert.setHeaderText("Waiting for another player to join...");
        waitingAlert.setContentText("Please wait while we find another player.");
        waitingAlert.show();

        NetworkPlayer networkPlayer = new NetworkPlayer();

        new Thread(() -> {
            boolean connected = networkPlayer.connectToServer("localhost", 27615, 5000, username);

            if (connected) {
                networkPlayer.sendCommand("HOST_GAME");

                networkPlayer.waitingForGameStart(waitingAlert, (PieceColor assignedColor) -> Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(FxmlFiles.BOARD.getFile());
                        Parent newRoot = loader.load();

                        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(newRoot);
                        stage.setScene(scene);
                        stage.setTitle("Online Play - " + username);

                        BoardController boardController = loader.getController();
                        GameMaster gameMaster = new GameMaster(boardController, networkPlayer);
                        boardController.setGameMaster(gameMaster);
                        boardController.setOnlyOnePlayer(true);
                        boardController.setStage(stage);
                        boardController.setMyTurn(assignedColor);

                        networkPlayer.setGameMaster(gameMaster);

                        gameMaster.startGame();

                        stage.show();
                    } catch (IOException e) {
                        logger.error("Error loading the board: {}", e.getMessage());
                        showError("Error", "Failed to load the chessboard.");
                    }
                }));
            } else {
                Platform.runLater(() -> {
                    waitingAlert.close();
                    showError("Connection Failed", "Unable to connect to the server.");
                });
            }
        }).start();
    }

    /**
     * Handles the action for watching a replay.
     *
     * @param event The event triggered by clicking the Replay button.
     */
    private void watchReplay(ActionEvent event) {
        // Handle replay (no changes here)
    }

    /**
     * Handles the action for opening the settings menu.
     *
     * @param event The event triggered by clicking the Settings button.
     */
    private void handleSettings(ActionEvent event) {
        // Handle settings (no changes here)
    }

    /**
     * Handles the action for exiting the application.
     *
     * @param event The event triggered by clicking the Exit button.
     */
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Loads a new stage based on the provided {@link FxmlFiles} enum.
     * The stage corresponds to a specific screen, such as the main menu or the chessboard.
     *
     * @param file The {@link FxmlFiles} enum representing the FXML file to load.
     */
    public static void loadStage(FxmlFiles file) {
        switch(file) {
            case MAIN:
                try {
                    FXMLLoader loader = new FXMLLoader(file.getFile());
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Chess Simulator");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case BOARD:
                try {
                    FXMLLoader loader = new FXMLLoader(file.getFile());
                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);

                    stage.setTitle("Local Play");

                    BoardController boardController = loader.getController();
                    GameMaster gameMaster = new GameMaster(boardController, boardController);
                    boardController.setGameMaster(gameMaster);
                    boardController.setOnlyOnePlayer(false);
                    boardController.setStage(stage);
                    gameMaster.startGame();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}