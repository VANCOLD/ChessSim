package managament;

import communication.ClientHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Player} class represents a player in the game.
 * It holds the player's name, whether the player is the host, and the associated {@code ClientHandler} for communication.
 * This class is used to track each player's details and their role in the game.
 */
@Setter
@Getter
public class Player {

    /** The name of the player. */
    private String name;

    /** A flag indicating whether the player is the host of the game. */
    private boolean isHost;

    /** The {@code ClientHandler} associated with this player for handling communication. */
    private ClientHandler clientHandler;

    /**
     * Constructs a new {@code Player} with default values:
     * - name is an empty string
     * - isHost is set to false
     * - clientHandler is set to null
     */
    public Player() {
        this.name = "";
        this.clientHandler = null;
        this.isHost = false;
    }
}
