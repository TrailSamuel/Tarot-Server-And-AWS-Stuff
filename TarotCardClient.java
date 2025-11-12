import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TarotCardClient {

    public static void main(String[] args) {
        // Validate command line arguments,we only want hostname and port
        if (args.length != 2) {
            System.err.println("Usage: java TarotCardClient <hostname> <port>");
            return;
        }

        // Extract connection parameters from command line arguments
        String hostname = args[0];  // Server hostname or IP address
        int port = Integer.parseInt(args[1]);  // Server port number

        // Initialize connection objects to null for proper cleanup in finally block
        Socket socket = null;
        BufferedReader in = null;

        try {
            // Establish TCP connection to the tarot server
            socket = new Socket(hostname, port);

            // Create buffered reader to read text data from server
            // InputStreamReader converts raw bytes to characters using default charset
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read exactly 4 lines from server in this specific order:
            String card1 = in.readLine();      // Past
            String card2 = in.readLine();      // Present
            String card3 = in.readLine();      // Future
            String serverInfo = in.readLine(); // Server IP

            // Display the tarot reading
            System.out.println();
            System.out.println("=======================================");
            System.out.println("           TAROT CARD READING          ");
            System.out.println("=======================================");
            System.out.println();
            System.out.println("Past:    " + card1);    // First card represents the past
            System.out.println("Present: " + card2);    // Second card represents the present
            System.out.println("Future:  " + card3);    // Third card represents the future
            System.out.println();
            System.out.println(serverInfo);             // Display server IP info
            System.out.println("=======================================");

        } catch (IOException e) {
            // Handle any network or I/O errors during connection or reading
            // This includes connection refused, host unreachable, etc.
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Cleanup section,always runs regardless of success or exception
            // Must close resources in reverse order of creation to prevent resource leaks
            try {
                // Close the input stream first
                if (in != null) {
                    in.close();
                }
                // Then close the socket connection
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                // Even cleanup can fail, so catch and report those errors too
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}