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
 * The GameManager class is a singleton class that manages the game rooms and connected players.
 */
@Getter
@Setter
public class GameManager {

    /** The list of game rooms. */
    private final Map<String, GameRoom> gameRooms;

    /** The map of connected players. */
    private final Map<ClientHandler, Player> connectedPlayers;

    private static GameManager instance;

    private static int roomNumber = 0;

    /**
     * Private constructor for the GameManager class.
     */
    private GameManager() {
        this.gameRooms = new HashMap<>();
        this.connectedPlayers = new HashMap<>();
    }

    /**
     * Returns the instance of the GameManager class.
     * @return the instance of the GameManager class
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Creates a new game room.
     * @return the name of the new game room
     */
    public String createGameRoom() {

        String roomName = "GameRoom " + roomNumber;
        GameRoom gameRoom = new GameRoom(roomName);
        gameRooms.put(roomName,gameRoom);
        return "GameRoom " + roomNumber++;

    }

    /**
     * Removes a game room.
     * @param roomName the name of the game room to remove
     */
    public void removeGameRoom(String roomName) {
        gameRooms.remove(roomName);
    }

    /**
     * Returns a game room identified by the given name.
     * @return the game room identified by the given name
     */
    public GameRoom getGameRoom(String roomName) {
        return gameRooms.get(roomName);
    }

    /**
     * Returns a player identified by the given socket.
     * @return the player identified by the given socket
     */
    public Player getPlayer(Socket socket) {
        return connectedPlayers.get(socket);
    }

    /**
     * Checks if a player is connected.
     * @return true if the player is connected, false otherwise
     */
    public boolean isPlayerConnected(Socket socket) {
        return connectedPlayers.containsKey(socket);
    }

    /**
     * Adds a player to a game room.
     * @return the player to add
     */
    public void addPlayerToGameRoom(Player player, String roomName) {

        GameRoom gameRoom = gameRooms.get(roomName);
        if(gameRoom.getPlayers().isEmpty()) {
            player.setHost(true);
        }

        gameRoom.addPlayer(player);
    }

    /**
     * Removes a player from a game room.
     * @return the player to remove
     */
    public void removePlayerFromGameRoom(Player player, String roomName) {
        GameRoom gameRoom = gameRooms.get(roomName);
        gameRoom.removePlayer(player);
    }

    public void sendCommand(GameRoom gameRoom, Player issuer, Command command) {
        gameRoom.getPlayers().stream()
                .filter(player -> !player.equals(issuer))
                .forEach(player -> player.getClientHandler().sendComand(command));
    }

    public GameRoom getGameRoomForPlayer(Player player) {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().contains(player)) {
                return gameRoom;
            }
        }
        return null;
    }

    public RoomStates joinableGameRoom() {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().size() == 1) {
                return JOINABLE;
            }
        }
        return RoomStates.FULL;
    }

    public void findGameRoomForPlayer(Player player) {
        for (GameRoom gameRoom : gameRooms.values()) {
            if (gameRoom.getPlayers().size() == 1) {
                addPlayerToGameRoom(player, gameRoom.getRoomName());
            }
        }
    }
}
