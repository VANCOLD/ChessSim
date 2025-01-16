import lombok.Getter;

import java.net.Socket;

@Getter
public class Player {

    private String name;
    private Socket socket;
    private boolean isHost;

    public Player(String name, Socket socket, boolean isHost) {
        this.name = name;
        this.socket = socket;
        this.isHost = isHost;
    }

    public boolean isHost() {
        return isHost;
    }
}
