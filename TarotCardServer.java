import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class TarotCardServer {

    public static void main(String[] args) {
        // Validate command line arguments, need exactly one port number
        if (args.length != 1) {
            System.err.println("Usage: java TarotCardServer <port>");
            return;
        }

        // Parse the port number from command line argument
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = null;

        try {
            // Create server socket bound to the specified port
            // This will listen for incoming client connections
            serverSocket = new ServerSocket(port);
            System.out.println("Tarot Card Server started on port " + port);

            // Main server loop, runs infinitely to serve clients
            while (true) {
                // Block and wait for a client to connect
                // accept() returns a new Socket for this specific client
                Socket clientSocket = serverSocket.accept();

                // Create a new handler for this client connection
                // Each client gets its own ClientHandler instance
                ClientHandler handler = new ClientHandler(clientSocket);

                // Spawn a new thread to handle this client independently
                // This allows the server to handle multiple clients
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            // Handle server startup or socket creation errors
            System.err.println("Server error: " + e.getMessage());
        } finally {
            // Cleanup server socket when shutting down
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing server: " + e.getMessage());
                }
            }
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;  // The client socket this handler is responsible for

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        PrintWriter out = null;  // Output stream to send data to client

        try {
            // Create PrintWriter for sending text to client
            // Second parameter true enables auto flush after each println
            out = new PrintWriter(socket.getOutputStream(), true);

            // Generate 3 unique random tarot cards for this reading
            String[] selectedCards = drawThreeCards();

            // Get this server's IP address to send to client
            String serverIP = InetAddress.getLocalHost().getHostAddress();

            // Send exactly 4 lines to the client in this order:
            out.println(selectedCards[0]);  // Past
            out.println(selectedCards[1]);  // Present
            out.println(selectedCards[2]);  // Future
            out.println("Server IP: " + serverIP);  // Server IP

        } catch (IOException e) {
            // Handle errors in client communication
            System.err.println("Client error: " + e.getMessage());
        } finally {
            // Always clean up resources for this client
            try {
                if (out != null) {
                    out.close();  // Close output stream
                }
                socket.close();   // Close client socket connection
            } catch (IOException e) {
                System.err.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    private String[] drawThreeCards() {
        // Set of Rider–Waite tarot cards (22 cards total)
        String[] cards = {
                "The Fool", "The Magician", "The High Priestess", "The Empress",
                "The Emperor", "The Hierophant", "The Lovers", "The Chariot",
                "Strength", "The Hermit", "Wheel of Fortune", "Justice",
                "The Hanged Man", "Death", "Temperance", "The Devil",
                "The Tower", "The Star", "The Moon", "The Sun",
                "Judgement", "The World"
        };

        Random random = new Random();
        // Use HashSet to automatically handle dupes,only stores unique cards
        Set<String> selected = new HashSet<>();

        // Keep adding random cards until we have 3 unique ones
        // Set.add() automatically rejects dupes, so we just loop until size = 3
        while (selected.size() < 3) {
            selected.add(cards[random.nextInt(cards.length)]);
        }

        // Convert the set back to an array for return
        return selected.toArray(new String[3]);
    }
}