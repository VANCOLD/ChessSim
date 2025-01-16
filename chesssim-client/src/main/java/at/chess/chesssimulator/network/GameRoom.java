package at.chess.chesssimulator.network;

import lombok.Getter;

@Getter
public class GameRoom {
    private final String name;
    private final String host;

    public GameRoom(String name, String host) {
        this.name = name;
        this.host = host;
    }

    @Override
    public String toString() {
        return name + " (Host: " + host + ")";
    }
}
