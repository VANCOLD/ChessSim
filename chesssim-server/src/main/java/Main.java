import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final int PORT = 27615;
    private static final List<GameRoom> gameRooms = new ArrayList<>();
    private static final Map<Socket, Player> connectedPlayers = new HashMap<>();

    public static void main(String[] args) {
        printLogoAndVersion();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle the client in a separate thread
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printLogoAndVersion() {
        String logo = """
                
                
                  /$$$$$$  /$$                                           /$$$$$$  /$$                                    /$$$$$$
                 /$$__  $$| $$                                          /$$__  $$|__/                                   /$$__  $$
                | $$  \\__/| $$$$$$$   /$$$$$$   /$$$$$$$ /$$$$$$$      | $$  \\__/ /$$ /$$$$$$/$$$$                     | $$  \\__/  /$$$$$$   /$$$$$$  /$$    /$$ /$$$$$$   /$$$$$$
                | $$      | $$__  $$ /$$__  $$ /$$_____//$$_____/      |  $$$$$$ | $$| $$_  $$_  $$       /$$$$$$      |  $$$$$$  /$$__  $$ /$$__  $$|  $$  /$$//$$__  $$ /$$__  $$
                | $$      | $$  \\ $$| $$$$$$$$|  $$$$$$|  $$$$$$        \\____  $$| $$| $$ \\ $$ \\ $$      |______/       \\____  $$| $$$$$$$$| $$  \\__/ \\  $$/$$/| $$$$$$$$| $$  \\__/
                | $$    $$| $$  | $$| $$_____/ \\____  $$\\____  $$       /$$  \\ $$| $$| $$ | $$ | $$                     /$$  \\ $$| $$_____/| $$        \\  $$$/ | $$_____/| $$
                |  $$$$$$/| $$  | $$|  $$$$$$$ /$$$$$$$//$$$$$$$/      |  $$$$$$/| $$| $$ | $$ | $$                    |  $$$$$$/|  $$$$$$$| $$         \\  $/  |  $$$$$$$| $$
                 \\______/ |__/  |__/ \\_______/|_______/|_______/        \\______/ |__/|__/ |__/ |__/                     \\______/  \\_______/|__/          \\_/    \\_______/|__/
                (Version 1.0.0)
                """;
        logger.info(logo);
    }

    // Client handler for each connected client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private Player player;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Setup the input/output streams
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                // Register the player (no prompt needed, just handle the connection)
                registerPlayer();

                // Now that the player is registered, enter the listening mode for commands
                listenForCommands();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Register the player when they connect (this is where you store their info)
        private void registerPlayer() throws IOException {
            // Register the player (this could be just the socket or any other necessary details)
            player = new Player("Player_" + socket.getInetAddress().getHostAddress(), socket, false); // For example, a generic name based on IP address
            connectedPlayers.put(socket, player);
            System.out.println("Player registered: " + player.getName());
        }

        // Listen for commands after registration
        private void listenForCommands() {
            try {
                while (true) {
                    writer.println("What would you like to do? (H)ost a game, (J)oin a game, or (Q)uit");

                    String command = reader.readLine(); // Wait for client input

                    if (command == null) {
                        break; // Exit if the client disconnects
                    }

                    if (command.equalsIgnoreCase("HOST_GAME")) {
                        hostGame(); // Host a game
                    } else if (command.equalsIgnoreCase("JOIN_GAME")) {
                        joinGame(); // Join a game
                    } else if(command.equalsIgnoreCase("LIST_ROOMS")) {
                        writer.println("GAME_ROOMS: " + gameRooms);
                    }else if (command.equalsIgnoreCase("Q")) {
                        writer.println("Goodbye!"); // Quit the session
                        break; // Exit the loop and close the connection
                    } else {
                        writer.println("Invalid command. Please try again.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void hostGame() {
            GameRoom gameRoom = new GameRoom("Room " + (gameRooms.size() + 1));
            gameRoom.addPlayer(player);
            gameRooms.add(gameRoom);
            player = new Player(player.getName(), socket, true); // Set the host
            writer.println("GAME_HOSTED: " + gameRoom.getRoomName());
            // Wait for another player to join
            waitForSecondPlayer(gameRoom);
        }

        private void waitForSecondPlayer(GameRoom gameRoom) {
            try {
                while (gameRoom.getPlayers().size() < 2) {
                    Thread.sleep(1000); // Wait for the second player to join
                }
                // Notify both players that the game can start
                for (Player p : gameRoom.getPlayers()) {
                    PrintWriter pWriter = new PrintWriter(p.getSocket().getOutputStream(), true);
                    pWriter.println("Game starting in room: " + gameRoom.getRoomName());
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        private void joinGame() {
            writer.println("Available game rooms: ");
            for (int i = 0; i < gameRooms.size(); i++) {
                writer.println((i + 1) + ". " + gameRooms.get(i).getRoomName());
            }

            writer.println("Select a game room to join (1, 2, etc.): ");
            try {
                int roomNumber = Integer.parseInt(reader.readLine()) - 1;
                if (roomNumber >= 0 && roomNumber < gameRooms.size()) {
                    GameRoom selectedRoom = gameRooms.get(roomNumber);
                    if (selectedRoom.isFull()) {
                        writer.println("This room is full. Try another one.");
                    } else {
                        selectedRoom.addPlayer(player);
                        writer.println("You have joined the game room: " + selectedRoom.getRoomName());
                        // Notify the host that a player has joined
                        PrintWriter hostWriter = new PrintWriter(selectedRoom.getPlayers().get(0).getSocket().getOutputStream(), true);
                        hostWriter.println("A new player has joined your game.");
                    }
                } else {
                    writer.println("Invalid room selection.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
