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
 * The {@code ClientHandler} class is responsible for handling the communication with a client.
 * It reads the input from the client and sends it back to the client.
 * The class also logs the command that was interpreted.
 */
@Slf4j
public class ClientHandler implements Runnable {

    /** The output stream to the client */
    private PrintWriter out;

    /** The input stream from the client */
    private BufferedReader in;

    /** The client socket */
    private final Socket clientSocket;

    /** The player associated with the client */
    private Player player;
    
    private GameManager gameManager;

    /**
     * Constructs a new {@code ClientHandler} object with the specified socket.
     *
     * @param socket the socket to be used
     * @throws IOException if an I/O error occurs when creating the input and output streams
     */
    public ClientHandler(Socket socket, Player player) throws IOException {
        this.clientSocket = socket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.player = player;
        this.gameManager = GameManager.getInstance();
    }

    /**
     * Reads the input from the client and sends it back to the client.
     * The method also logs the command that was interpreted.
     * If an I/O error occurs, the method prints the stack trace.
     */
    public void run()
    {

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
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param command the command that needs to be sent
     */
    public void sendComand(Command command) {
        out.println(command.getMessage());
    }

    /**
     * Closes the connection with the client.
     * @throws IOException if an I/O error occurs when closing the connection
     */
    public void close() throws IOException {
        out.close();
        in.close();
        clientSocket.close();
    }
}