import communication.ClientHandler;
import lombok.extern.slf4j.Slf4j;
import managament.GameRoom;
import managament.Player;

import java.io.IOException;
import java.net.*;
import java.util.*;

@Slf4j
public class Main {

    private static final int PORT = 27615;


    public static void main(String[] args) {
        log.info("Server is listening on port {}", PORT);
        ServerSocket server = null;

        try {

            server = new ServerSocket(PORT);
            server.setReuseAddress(true);


            while (true) {

                Socket client = server.accept();
                log.info("New client connected {}", client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client, new Player());
                new Thread(clientSock).start();
            }

        } catch (IOException e) {
            log.error("Server exception: {}", e.getMessage());
        } finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    log.error("Server exception: {}", e.getMessage());
                }
            }
        }
    }

}
