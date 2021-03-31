package backend;

import frontend.MainWindow;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;


/**
 * Main class for running the server
 */
public class Server extends Thread{
    private int port;
    // stop the server
    public boolean stop;
    // stores username
    private Set<String> userNames = new HashSet<>();
    // stores user objects
    private Set<UserThread> userThreads = new HashSet<>();
    private MainWindow window;

    /**
     * Default constructor TODO: take the output stream as parametere too
     */
    public Server() {
        this.port = 3000;
        this.stop = false;
    }

    /**
     * Parametrized Constructor
     *
     * @param port: port number to run the server on TODO: take the output stream as
     *              parameteres too
     */
    public Server(int port, MainWindow window) {
        this.port = port;
        this.stop = false;
        this.window = window;
    }

    /**
     * get the port number
     *
     * @return:port number
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Initializing the server
     */
    public void init() {
        try (ServerSocket server = new ServerSocket(port)) {

            while (!stop) {
                Socket clientSocket = server.accept();
                // TODO: Add this message to info panel of GUI--->
                window.writeServer("Client Connected");
                window.writeServer("Client Info=>");
                window.writeServer(clientSocket.getInetAddress().toString());
                window.writeServer("-----------------------");
                // <-------

                // sperate thread for each client
                UserThread newUser = new UserThread(clientSocket, this);
                // add user the list
                userThreads.add(newUser);
                // start the thread
                newUser.start();
            }

        } catch (IOException e) {
            // TODO: Add this message to info panel of GUI---->
            window.writeServer("Port already in use");
            // <----
            window.setServerToogle(false);

            // TODO: Log this error---->
            System.out.println(e.toString());
            // <----
        }
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            // TODO: display in GUI--->
            window.writeServer("The user " + userName + " quitted");
            // <---
        }
    }

    /**
     *
     * @return: userName
     */
    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently
     * connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    @Override
    public void run()
    {
        init();
    }

}
