package backend;

import frontend.MainWindow;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Main Class for Client Side
 */
public class Client extends Thread {
    // ex-> localhost
    private String hostname;
    private int port;
    private String userName;
    private String id;
    private String secret;
    private HashMap<String, String> secretDict;
    private MainWindow window;
    private Socket socket;

    /**
     * Default Constructor
     */
    public Client(String secret) {
        this.hostname = "localhost";
        this.port = 3000;
        this.id = UUID.randomUUID().toString();
        this.secret = secret;
    }

    /**
     * Parametrized Constructor
     *
     * @param hostname: hostname of the server
     * @param port:     port server connected to
     */
    public Client(String hostname, int port, String secret, MainWindow window) {
        this.hostname = hostname;
        this.port = port;
        this.id = UUID.randomUUID().toString();
        this.secret = secret;
        this.window = window;
        this.secretDict = new HashMap<String, String>();
    }

    public void init() {

        window.setCodeField(this.secret);
        try {
            InetAddress address = InetAddress.getByName(hostname);
            socket = new Socket(address, port);

            window.writeClient("Connected to the chat server");
            window.writeClient("Your unique id:" + this.id);
            window.writeClient("Your secret key:" + this.secret);

            // seperate threads for reading and writing msgs
            new ReadThread(socket, this, window).start();
            new WriteThread(socket, this, window).start();

        } catch (UnknownHostException ex) {
            window.writeClient("Server not found");
            window.setClientToogle(false);
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            window.writeClient(ex.getMessage());
            window.setClientToogle(false);
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }


    @Override
    public void run()
    {
        init();
    }

    public void close()
    {
        try {
            socket.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    /**
     * Setter Method to set userName field
     *
     * @param userName
     */
    void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method to get value of field userName
     *
     * @return: userName
     */
    String getUserName() {
        return this.userName;
    }

    String getUniqueId() {
        return this.id;
    }

    String getCurrentSecret() {
        return this.secret;
    }

    String getSecretOfId(String id) {
        return this.secretDict.get(id);
    }

    public void addSecretOfId(String id, String secret) {
        this.secretDict.put(id, secret);
    }

}