package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * For making seperate thread for each user
 */
public class UserThread extends Thread {
    // User Socket
    private Socket socket;
    // Main Server
    private Server server;
    private PrintWriter writer;
    private String userName;
    private String id;

    /**
     * Parametrized Constructor
     *
     * @param socket: User Socket
     * @param server: Main Server
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.userName = "Anonymous";
    }

    /**
     * Overriding run() method of Thread Class
     */
    @Override
    public void run() {
        try (
                // autoclosable
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            this.writer = new PrintWriter(socket.getOutputStream(), true);

            printUsers();

            // First line contains the unique id
            this.id = reader.readLine();
            // Second line given by client is user name
            this.userName = reader.readLine();
            server.addUserName(userName);

            // message send by server to all user, exept current one
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            // client messages
            String clientMessage;

            // rest msgs send by current client
            while (socket.isConnected() && !socket.isClosed()) {
                clientMessage = reader.readLine();
                serverMessage = "$" + this.id + "(" + userName + "):" + clientMessage;
                server.broadcast(serverMessage, this);
            }

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();

        } finally {

            server.removeUser(userName, this);

            try {
                socket.close();

            } catch (IOException e) {
                System.out.println(e.toString());
            }

            String serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}

