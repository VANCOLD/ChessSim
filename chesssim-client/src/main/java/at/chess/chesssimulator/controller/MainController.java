package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.utils.FmxlFiles;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainController {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

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

    @FXML
    public void initialize() {

        boolean canConnect = checkServerConnection();

        localPlay.setOnAction(this::handleLocalPlay);

        if (canConnect) {
            onlinePlay.setDisable(false);
            onlinePlay.setOnAction(this::handleOnlinePlay);
        }

        replay.setOnAction(this::watchReplay);
        settings.setOnAction(this::handleSettings);
        exit.setOnAction(this::handleExit);
    }

    private void handleLocalPlay(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(FmxlFiles.BOARD.getFile());
            Parent newRoot = loader.load();

            // Get the current stage (window) from any node (e.g., the button)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);

            // Optionally, set the stage title and show it again
            stage.setTitle("Local Play");

            // Get the BoardController and set the players
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
        try {
            // Load the new FXML file for online play
            FXMLLoader loader = new FXMLLoader(FmxlFiles.NETWORK_BROWSER.getFile());
            Parent newRoot = loader.load();

            // Get the current stage (window) from any node (e.g., the button)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);

            // Pass the existing socket connection to the NetworkBrowserController
            NetworkBrowserController networkController = loader.getController();
            networkController.setSocket(socket);  // Pass the existing socket connection

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void watchReplay(ActionEvent event) {
        // Handle replay (no changes here)
    }

    private void handleSettings(ActionEvent event) {
        // Handle settings (no changes here)
    }

    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    private boolean checkServerConnection() {
        String hostname = "localhost";  // Server address
        int port = 27615;                // Server port number

        try {
            socket = new Socket(hostname, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send a message to the server
            String message = "Hello Server!";
            writer.println(message);
            System.out.println("Sent to server: " + message);

            // Read the server's response
            String response = reader.readLine();
            System.out.println("Server's response: " + response);

            return true;

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }

        return false;
    }

    public static void loadStage(FmxlFiles file) {
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
                    // Set the new scene
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);

                    // Optionally, set the stage title and show it again
                    stage.setTitle("Local Play");

                    // Get the BoardController and set the players
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
