package managament;

import ch.qos.logback.core.net.server.Client;
import communication.ClientHandler;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Player {

    private String name;
    private boolean isHost;
    private ClientHandler clientHandler;

    public Player() {
        this.name = "";
        this.clientHandler = null;
        this.isHost = false;
    }
}