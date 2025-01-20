package managament;

import communication.ClientHandler;
import communication.Command;
import lombok.Getter;
import lombok.Setter;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static managament.RoomStates.JOINABLE;

/**
 * The {@code GameManager} class is a singleton class that manages the game rooms and connected players.
 * It is responsible for creating and managing game rooms, adding and removing players, and handling
 * the communication between players in a game room.
 *
 * <p>
 * The {@code GameManager} ensures that players can join or host a game, and facilitates the management
 * of game state by keeping track of the connected players and the rooms they are in.
 * </p>
 */
@Getter
@Setter
public class GameManager {

    /** The list of game rooms managed by the GameManager. */
    private final Map<String, GameRoom> gameRooms;

    /** The map of connected players and their associated client handlers. */
    private final Map<ClientHandler, Player> connectedPlayers;

    /** Singleton instance of the GameManager. */
    private static GameManager instance;

    /** Counter for generating unique game room names. */
    private static int roomNumber = 0;

    /**
     * Private constructor for the GameManager class. Initializes the game rooms and connected players maps.
     */
    private GameManager() {
        this.gameRooms = new HashMap<>();
        this.connectedPlayers = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the GameManager class.
     * If an instance does not exist, it creates one.
     *
     * @return the singleton instance of the GameManager
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Creates a new game room with a unique name.
     * The name of the room is generated based on the current room number.
     *
     * @return the name of the newly created game room
     */
    public String createGameRoom() {
        String roomName = "GameRoom " + roomNumber;
        GameRoom gameRoom = new GameRoom(roomName);
        gameRooms.put(roomName, gameRoom);
        return "GameRoom " + roomNumber++;
    }

    /**
     * Removes a game room from the GameManager.
     *
     * @param roomName the name of the game room to remove
     */
    public void removeGameRoom(String roomName) {
        gameRooms.remove(roomName);
    }

    /**
     * Returns the game room identified by the given room name.
     *
     * @param roomName the name of the game room
     * @return the game room with the given name, or null if it doesn't exist
     */
    public GameRoom getGameRoom(String roomName) {
        return gameRooms.get(roomName);
    }

    /**
     * Returns the player associated with the given socket.
     *
     * @param socket the socket of the player
     * @return the player associated with the socket, or null if not found
     */
    public Player getPlayer(Socket socket) {
        return connectedPlayers.get(socket);
    }

    /**
     * Checks if a player is connected by their socket.
     *
     * @param socket the socket of the player
     * @return true if the player is connected, false otherwise
     */
    public boolean isPlayerConnected(Socket socket) {
        return connectedPlayers.containsKey(socket);
    }

    /**
     * Adds a player to a specific game room.
     * If the game room is empty, the player becomes the host.
     *
     * @param player the player to add to the game room
     * @param roomName the name of the game room
     */
    public void addPlayerToGameRoom(Player player, String roomName) {
        GameRoom gameRoom = gameRooms.get(roomName);
        if (gameRoom.getPlayers().isEmpty()) {
            player.setHost(true);
        }
        gameRoom.addPlayer(player);
    }

    /**
     * Removes a player from a specific game room.
     *
     * @param player the player to remove from the game room
     * @param roomName the name of the game room
     */
    public void removePlayerFromGameRoom(Player player, String roomName) {
        GameRoom gameRoom = gameRooms.get(roomName);
        gameRoom.removePlayer(player);
    }

    /**
     * Sends a command to all players in a game room except the issuer.
     *
     * @param gameRoom the game room where the command will be sent
     * @param issuer the player who issued the command
     * @param command the command to send
     */
    public void sendCommand(GameRoom gameRoom, Player issuer, Command command) {
        gameRoom.getPlayers().stream()
                .filter(player -> !player.equals(issuer))
                .forEach(player -> player.getClientHandler().sendCommand(command));
    }

    /**
     * Returns the game room that contains the given player.
     *
     * @param player the player whose game room is to be found
     * @return the game room that contains the player, or null if not found
     */
    public GameRoom getGameRoomForPlayer(Player player) {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().contains(player)) {
                return gameRoom;
            }
        }
        return null;
    }

    /**
     * Checks if there is a joinable game room (i.e., a game room with exactly one player).
     *
     * @return {@link RoomStates#JOINABLE} if a joinable game room is found, {@link RoomStates#FULL} otherwise
     */
    public RoomStates joinableGameRoom() {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().size() == 1) {
                return JOINABLE;
            }
        }
        return RoomStates.FULL;
    }

    /**
     * Finds a game room for the given player that has exactly one player and adds the player to that room.
     *
     * @param player the player to find a game room for
     */
    public void findGameRoomForPlayer(Player player) {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().size() == 1) {
                addPlayerToGameRoom(player, gameRoom.getRoomName());
            }
        }
    }
}
