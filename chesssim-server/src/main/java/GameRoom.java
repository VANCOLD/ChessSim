import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameRoom {

    private String roomName;
    private List<Player> players;

    public GameRoom(String roomName) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean isFull() {
        return players.size() >= 2;
    }
}
