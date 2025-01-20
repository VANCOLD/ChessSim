package managament;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GameRoom} class represents a game room in which players can join and participate in a game.
 * It holds the room name and a list of players who are part of the room.
 * Players can be added to or removed from the game room as the game progresses.
 */
@Getter
@Setter
public class GameRoom {

    /** The name of the game room. */
    private String roomName;

    /** The list of players in the game room. */
    private List<Player> players;

    /**
     * Constructs a new {@code GameRoom} with the specified room name.
     * Initializes an empty list of players.
     *
     * @param roomName the name of the game room
     */
    public GameRoom(String roomName) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
    }

    /**
     * Adds a player to the game room.
     *
     * @param player the player to add to the game room
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes a player from the game room.
     *
     * @param player the player to remove from the game room
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }
}
