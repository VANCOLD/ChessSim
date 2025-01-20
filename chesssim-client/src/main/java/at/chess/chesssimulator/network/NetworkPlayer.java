package at.chess.chesssimulator.network;

import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.enums.MoveType;
import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.gamelogic.Player;
import at.chess.chesssimulator.piece.enums.PieceColor;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

import static at.chess.chesssimulator.utils.AlertUtil.showError;

public class NetworkPlayer implements Player {

    private static final Logger logger = LoggerFactory.getLogger(NetworkPlayer.class);

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    @Setter
    private GameMaster gameMaster;

    public boolean connectToServer(String host, int port, int timeoutMillis, String username) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeoutMillis);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("register " + username);
            logger.info("Sent registration command to server with username {}", username);
            return true;
        } catch (IOException e) {
            logger.error("Failed to connect to server: {}", e.getMessage());
            return false;
        }
    }

    public void sendCommand(String command) {
        if (writer != null) {
            writer.println(command);
            listenForResponse();
        }
    }

    public void sendMove(Move move) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(move);
            objectOutputStream.flush();
            logger.info("Sent move: {}", move);
        } catch (IOException e) {
            logger.error("Error sending move object: {}", e.getMessage());
        }
    }

    public void listenForResponse() {
        new Thread(() -> {
            try {

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Move move = (Move) objectInputStream.readObject();

                logger.info("Received move: {}", move);

                if (move.getMoveType() != null) {
                    logger.info("Move Type: {}", move.getMoveType());
                }

                Platform.runLater(() -> this.receiveMoveResult(move));

            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error receiving move object: {}", e.getMessage());
            }
        }).start();
    }

    public void waitingForGameStart(Alert waitingAlert, Consumer<PieceColor> onGameStartWithColor) {
        new Thread(() -> {
            try {
                String response = reader.readLine();
                logger.info("Received response from server: {}", response);

                if (response.startsWith("GAME_START")) {
                    String[] parts = response.split(" ");
                    if (parts.length > 1) {
                        PieceColor turn = PieceColor.getColorFromName(parts[1]);

                        Platform.runLater(() -> {
                            waitingAlert.close();
                            onGameStartWithColor.accept(turn); // Pass the color to the callback
                        });
                    }
                }
            } catch (IOException e) {
                logger.error("Error while waiting for server response: {}", e.getMessage());
                Platform.runLater(() -> {
                    waitingAlert.close();
                    showError("Error", "Error while waiting for server response.");
                });
            }
        }).start();
    }

    public void closeConnection() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            logger.error("Error closing connection: {}", e.getMessage());
        }
    }

    @Override
    public void notifyTurn(PieceColor turn) {
        this.sendCommand("NOTIFY_TURN " + turn);
        listenForResponse();
    }

    @Override
    public void receiveMoveResult(Move move) {
        if (move.getMoveType() != MoveType.INVALID) {
            this.sendMove(move);
            gameMaster.endTurn();
        }
    }

    @Override
    public void updateBoard() {
    }

    @Override
    public void sendMove(Position originalPosition, Position newPosition) {
        gameMaster.processInput(originalPosition, newPosition);
    }
}
