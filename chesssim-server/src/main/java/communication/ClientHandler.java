package communication;

import lombok.extern.slf4j.Slf4j;
import managament.GameManager;
import managament.GameRoom;
import managament.Player;
import managament.RoomStates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * The {@code ClientHandler} class is responsible for handling communication with a client.
 * It processes incoming messages, interprets commands, and sends responses back to the client.
 * This class also logs all commands received from the client for debugging and tracking purposes.
 *
 * <p>
 * It operates in a separate thread to handle the client-server interaction concurrently. The
 * client can register, host a game, join an existing game, and interact with the game room.
 * </p>
 *
 * <p>
 * The client handler listens for commands sent by the client and processes them accordingly.
 * It uses the {@link GameManager} to manage the game state and handle game-related actions.
 * </p>
 */
@Slf4j
public class ClientHandler implements Runnable {

    /** The output stream to send messages to the client */
    private final PrintWriter out;

    /** The input stream to receive messages from the client */
    private final BufferedReader in;

    /** The socket associated with the client */
    private final Socket clientSocket;

    /** The player associated with this client */
    private final Player player;

    /** The game manager responsible for managing game rooms and players */
    private final GameManager gameManager;

    /**
     * Constructs a new {@code ClientHandler} with the specified socket and player.
     *
     * @param socket the socket through which the client communicates
     * @param player the player associated with this client handler
     * @throws IOException if an I/O error occurs while creating the input/output streams
     */
    public ClientHandler(Socket socket, Player player) throws IOException {
        this.clientSocket = socket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.player = player;
        this.gameManager = GameManager.getInstance();
    }

    /**
     * Listens for incoming messages from the client, processes them, and responds accordingly.
     * <p>
     * The method continuously reads lines from the client's input stream. It interprets commands,
     * such as player registration, game hosting, or joining a game, and interacts with the
     * {@link GameManager} to manage the game state.
     * </p>
     * <p>
     * If an I/O error occurs during communication, the exception is caught and the stack trace is printed.
     * </p>
     */
    public void run() {
        player.setClientHandler(this);

        try {
            String line;
            while ((line = in.readLine()) != null && !line.isBlank()) {

                log.info("Sent from the client: {}", line);
                String command = line.split(" ")[0];
                String message = line.substring(command.length()).trim();

                Command commandIssued = new Command(CommandType.fromString(command), message);
                log.info("The following command was interpreted: {}", CommandType.fromString(command));

                if (player.getName().isEmpty() && commandIssued.getCommandType() == CommandType.REGISTER) {
                    player.setName(commandIssued.getMessage());
                    log.info("Player {} has registered on the server.", player.getName());

                } else {
                    if (Objects.requireNonNull(commandIssued.getCommandType()) == CommandType.HOST_GAME) {
                        RoomStates state = gameManager.joinableGameRoom();

                        if (state == RoomStates.FULL) {
                            String roomName = gameManager.createGameRoom();
                            gameManager.addPlayerToGameRoom(player, roomName);
                            log.info("Created GameRoom {}", roomName);
                        } else {
                            gameManager.findGameRoomForPlayer(player);
                            log.info("SECOND_PLAYER_JOINED {}", player.getName());
                            out.println("GAME_START WHITE");
                            gameManager.sendCommand(gameManager.getGameRoomForPlayer(player), player, new Command(CommandType.GAME_START, "GAME_START BLACK"));
                        }
                    } else {
                        GameRoom gameRoom = gameManager.getGameRoomForPlayer(player);
                        gameManager.sendCommand(gameRoom, player, commandIssued);
                    }
                }
            }
        } catch (IOException e) {
            log.error("An error occurred while communicating with the client: {}", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                log.error("An error occurred while closing the connection: {}", e.getMessage());
            }
        }
    }

    /**
     * Sends a command message to the client.
     *
     * @param command the command that needs to be sent to the client
     */
    public void sendCommand(Command command) {
        out.println(command.getMessage());
    }

    /**
     * Closes the connection with the client, including input/output streams and the socket.
     *
     * @throws IOException if an I/O error occurs while closing the connection
     */
    public void close() throws IOException {
        out.close();
        in.close();
        clientSocket.close();
    }
}