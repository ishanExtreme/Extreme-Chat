package backend;

import frontend.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Seperate thread for writing the message
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;
    private MainWindow window;

    public WriteThread(Socket socket, Client client, MainWindow window) {
        this.socket = socket;
        this.client = client;
        this.window = window;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

            // give id to the userThread
            writer.println(client.getUniqueId());
            // First Input is Name
            String userName = window.readUser("Enter your name: ");
            client.setUserName(userName);
            writer.println(userName);


//        while (!socket.isOutputShutdown()) {
            // ----> Encode Here <------
            window.sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String text = window.getMsg();
                    if (!text.equals("")) {
                        String encMsg = Crypto.encrypt(text, client.getCurrentSecret());
                        writer.println(encMsg);
                        window.cleanMsgField();
                        window.writeChatYou(text);
                    }
                }
            });
            // encode the text with secret key
//            if (text == null || text.isEmpty() || text.equals("null"))
//                break;


//        }

//        try {
//            socket.close();
//            writer.close();
//        } catch (IOException ex) {
//
//            System.out.println("Error writing to server: " + ex.getMessage());
//        }
    }
}

