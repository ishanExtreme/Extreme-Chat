package backend;

import frontend.MainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Seperate thread for reading and displaying the message
 */
public class ReadThread extends Thread {
    private Socket socket;
    private Client client;
    private BufferedReader reader;
    private MainWindow window;

    public ReadThread(Socket socket, Client client, MainWindow window) {
        this.socket = socket;
        this.client = client;
        this.window = window;

        try {
            InputStream input = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!socket.isInputShutdown()) {
            try {
                String response = reader.readLine();
                if (response.contains("$")) {
                    String encMsg = response.substring(response.indexOf(':') + 1);
                    String id = response.substring(1, response.indexOf('('));
                    String userName = response.substring(response.indexOf('(') + 1, response.indexOf(')'));
                    if (encMsg != null && !encMsg.isEmpty() && !encMsg.equals("null")) {
                        // if (response.equals(null) || response.equals("null"))
                        // break;
                        // ----> DECODE HERE <-----
                        // decode message with secretKey dict of the client class
                        String secret = client.getSecretOfId(id);
                        String decMsg = null;
                        if (secret != null)
                            decMsg = Crypto.decrypt(encMsg, secret);
                            // if secretKey for that message not availaible send the
                        else
                            decMsg = encMsg;
                        // decoded message
//                        String message = "->(" + userName + "):" + decMsg;
                        window.writeChatOther(userName, decMsg);
                    }
                } else {
                    // server messages
                    window.writeClient("-------------------------------");
                    window.writeClient("->" + response);
                    window.writeClient("-------------------------------");
                }

            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        try {
            socket.close();
            reader.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
