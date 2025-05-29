import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to the chat server");

            // Reader to get messages from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Writer to send messages to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Reader to read your input from console
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));


            // Thread to listen for messages from server
            Thread readThread = new Thread(() -> {
                String msgFromServer;
                try {
                    while ((msgFromServer = in.readLine()) != null) {
                        System.out.println("Server: " + msgFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readThread.start();


            // Main thread: read user input and send to server
            String userMsg;
            while ((userMsg = userInput.readLine()) != null) {
                out.println(userMsg);
            }

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}