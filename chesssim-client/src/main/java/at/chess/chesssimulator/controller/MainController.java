package at.chess.chesssimulator.controller;


import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.utils.FmxlFiles;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainController {

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
            gameMaster.startGame();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOnlinePlay(ActionEvent event) {
    }

    private void watchReplay(ActionEvent event) {
    }

    private void handleSettings(ActionEvent event) {
    }

    private void handleExit(ActionEvent event) {
        Platform.exit();
    }


    private boolean checkServerConnection() {
        String hostname = "localhost";  // Server address
        int port = 27615;                // Server port number

        try (Socket socket = new Socket(hostname, port)) {
            // Create output stream to send data to the server
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Create input stream to receive data from the server
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

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
}