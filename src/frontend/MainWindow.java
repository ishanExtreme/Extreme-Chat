package frontend;

import backend.Client;
import backend.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class MainWindow {
    public JPanel mainPanel;
    private JPanel serverPanel;
    private JPanel infoPanel;
    private JPanel chatPanel;
    private JRadioButton serverToogle;
    private JTextArea serverText;
    private JTextArea clientText;
    private JTextArea chatText;
    private JTextField codeField;
    private JPanel codePanel;
    private JButton addCode;
    private JTextField msgField;
    private JPanel sendPanel;
    public JButton sendButton;
    private JRadioButton clientToogle;
    private Server server;
    private Client client;
    private MainWindow window;

    public MainWindow() {
        window = this;
        serverToogle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // performed on turning on the server
                if(serverToogle.isSelected())
                {
                    int port=-1;
                    String text = readUser("Enter Port Number:");
                    if(text!=null)
                        port = Integer.parseInt(text);
                    else
                        setServerToogle(false);
                    if(port!=-1)
                    {
                        server = new Server(port, window);
                        writeServer("Connected to port->"+server.getPort());
                        // start server thread
                        server.start();
                    }

                }

                if(!serverToogle.isSelected() && server!=null)
                {
                    server.stop = true;
                    try(Socket temp = new Socket("localhost", server.getPort());) {

                        writeServer("Server Disconnected!");

                    }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }

            }
        });
        clientToogle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(clientToogle.isSelected())
                {
                    sendButton.setEnabled(true);
                    addCode.setEnabled(true);
                    ClientDialog clientDialog = new ClientDialog();
                    clientDialog.setSize(300,200);
                    clientDialog.setLocationRelativeTo(null);
                    clientDialog.setResizable(false);
                    clientDialog.setVisible(true);

                    if(clientDialog.getCancelStatus()==false)
                    {
                        String hostName = clientDialog.getHostName();
                        int portNumber = clientDialog.getPortNumber();
                        String secret = clientDialog.getSecretCode();
                        client = new Client(hostName, portNumber, secret, window);
                        client.start();
                    }
                    else
                    {
                        setClientToogle(false);
                    }
                }

                if(!clientToogle.isSelected() && client!=null)
                {
                    sendButton.setEnabled(false);
                    addCode.setEnabled(false);
//                    client.close();
                }
            }
        });
        addCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CodeDialog codeDialog = new CodeDialog();
                codeDialog.setSize(300,180);
                codeDialog.setLocationRelativeTo(null);
                codeDialog.setResizable(false);
                codeDialog.setVisible(true);

                if(codeDialog.getCancelStatus() == false)
                {
                    String id = codeDialog.getUniqueId();
                    String secret = codeDialog.getSecretCode();
                    client.addSecretOfId(id, secret);
                }
            }
        });
    }

    public void setServerToogle(boolean set)
    {
        serverToogle.setSelected(set);
    }

    public void setClientToogle(boolean set)
    {
        clientToogle.setSelected(set);
    }

    public void writeServer(String text)
    {

        serverText.append(text+"\n");
    }

    public void writeChat(String text)
    {
        chatText.append(text+"\n");
    }

    public void writeClient(String text)
    {
        clientText.append(text+"\n");
    }

    public String readUser(String text)
    {
        String res = JOptionPane.showInputDialog(text);
        return res;
    }

    public String getMsg()
    {
        return msgField.getText();
    }

    public void cleanMsgField()
    {
        msgField.setText("");
    }

    public void setCodeField(String code)
    {
        codeField.setText(code);
    }

}




