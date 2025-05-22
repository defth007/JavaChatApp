import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args){
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server started on port " + port);

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler handler = new ClientHandler(clientSocket);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler handler : clientHandlers) {
            if (handler != sender) {
                handler.sendMessage(message);
            }
        }
    }

    // Remove a client when they disconnect
    public static void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
    }
}

class ClientHandler implements Runnable{
    private final Socket socket;
    private PrintWriter out;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null){
                System.out.println("Received: " + message);
                Server.broadcast(message, this);
            }
        } catch (IOException e){
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            Server.removeClient(this);
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }
}
