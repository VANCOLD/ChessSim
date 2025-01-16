package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.network.GameRoom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static at.chess.chesssimulator.controller.BoardController.logger;

public class NetworkBrowserController extends VBox {

    @FXML
    private ListView<GameRoom> gameListView;
    @FXML
    private ObservableList<GameRoom> gameRooms;
    @FXML
    private Button hostGame;
    @FXML
    private Button joinGame;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public NetworkBrowserController() {
        gameRooms = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        gameListView.setItems(gameRooms);
        gameListView.setPrefSize(300, 200);
        this.setSpacing(10);
        this.setStyle("-fx-padding: 20; -fx-alignment: center;");
    }

    // New method to set the socket connection from MainController
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Start a new thread to listen for server messages
            new Thread(this::listenForServerMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Could not connect to the server.");
        }
    }

    private void listenForServerMessages() {
        try {
            String serverMessage;
            while ((serverMessage = reader.readLine()) != null) {
                System.out.println("Received from server: " + serverMessage); // Debugging line
                if (serverMessage.startsWith("GAME_ROOMS")) {
                    updateGameRooms(serverMessage);
                } else if (serverMessage.startsWith("GAME_HOSTED")) {

                    Platform.runLater(() -> {
                        showAlert("Game Hosted", "A new game room has been created.");
                        try {
                            // Log that the LIST_ROOMS request is being sent
                            System.out.println("Sending LIST_ROOMS to server...");
                            writer.println("LIST_ROOMS");
                            writer.flush();
                        } catch (Exception e) {
                            System.out.println("Error sending LIST_ROOMS: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error reading server messages.");
        }
    }

    private void updateGameRooms(String serverMessage) {
        logger.info("Updating game rooms from server message: " + serverMessage);
        String[] roomNames = serverMessage.split(":")[1].split(",");

        // Debugging log to see room names
        logger.info("Room names received: " + String.join(", ", roomNames));

        Platform.runLater(() -> {
            // Clear the list and update it
            gameRooms.clear();

            // Debugging log to see if we are adding the rooms
            logger.info("Adding rooms to gameRooms list.");

            for (String roomName : roomNames) {
                GameRoom gameRoom = new GameRoom(roomName.trim(), "Player 1");
                gameRooms.add(gameRoom);
                logger.info("Added room: " + gameRoom.getName());
            }
        });
    }

    @FXML
    private void hostGame() {
        writer.println("HOST_GAME");
        writer.flush();
    }

    @FXML
    private void joinGame() {
        GameRoom selectedRoom = gameListView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            writer.println("JOIN_GAME:" + selectedRoom.getName());
        } else {
            showAlert("No Selection", "Please select a game room to join.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
