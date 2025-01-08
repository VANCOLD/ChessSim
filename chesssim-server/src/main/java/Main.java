import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        printLogoAndVersion();
        int port = 27615;  // Port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String clientMessage = reader.readLine();
                System.out.println("Received: " + clientMessage);

                writer.println("Hello from the server!");

                socket.close();
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
}
