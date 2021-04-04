package frontend;

import backend.Client;
import backend.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainWindow {
    public JPanel mainPanel;
    private JPanel serverPanel;
    private JPanel infoPanel;
    private JPanel chatPanel;
    private JRadioButton serverToogle;
    private JTextArea serverText;
    private JTextArea clientText;
    private JTextPane chatPane;
    private JTextField codeField;
    private JPanel codePanel;
    private JButton addCode;
    private JTextField msgField;
    private JPanel sendPanel;
    public JButton sendButton;
    private JRadioButton clientToogle;
    private JTextField idField;
    private JLabel image;
    private JButton saveButton;
    private JButton clearButton;
    private Server server;
    private Client client;
    private MainWindow window;
    private JSONObject message;
    private JSONArray currentClientMsg;
    private JSONArray otherClientMsg;

    public MainWindow() {
        window = this;
        message = new JSONObject();
        currentClientMsg = new JSONArray();
        otherClientMsg = new JSONArray();
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
                    ClientDialog clientDialog = new ClientDialog();
                    clientDialog.setSize(300,200);
                    clientDialog.setLocationRelativeTo(null);
                    clientDialog.setResizable(false);
                    clientDialog.setVisible(true);

                    if(clientDialog.getCancelStatus()==false)
                    {
                        sendButton.setEnabled(true);
                        addCode.setEnabled(true);
                        clearButton.setEnabled(true);
                        saveButton.setEnabled(true);
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
                    clearButton.setEnabled(false);
                    saveButton.setEnabled(false);
                    setCodeField("");
                    setIdField("");
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
        // clear chat panel
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearChat();
                clearMsgVars();
            }
        });
        // save json file
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try(FileWriter output = new FileWriter(getDate() + ".json");)
                {
                    message.put("APP Name", "Extreme-Chat");
                    message.put("APP Version", "V1.0");
                    message.put("Your Message", currentClientMsg);
                    message.put("Other's Message", otherClientMsg);
                    output.write(message.toJSONString());
                    clearMsgVars();
                }
                catch (IOException e)
                {
                    System.out.println(e.toString());
                }

            }
        });
    }

    private String getDate()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return dateFormat.format(date);
    }

    private void clearMsgVars()
    {
        message = new JSONObject();
        otherClientMsg = new JSONArray();
        currentClientMsg = new JSONArray();
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

    public void writeChatOther(String username, String text)
    {
        otherClientMsg.add(username+": "+text);
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setFontSize(attributeSet, 15);
        StyleConstants.setForeground(attributeSet, Color.BLUE);
        StyleConstants.setBackground(attributeSet, Color.YELLOW);
        Document doc = chatPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "->("+username+"):", attributeSet);
            StyleConstants.setBackground(attributeSet, Color.WHITE);
            doc.insertString(doc.getLength(), text+"\n", attributeSet);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }

    public void writeChatYou(String text)
    {
        currentClientMsg.add("YOU: "+text);
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setFontSize(attributeSet, 15);
        StyleConstants.setForeground(attributeSet, Color.decode("#0A6135"));
        Document doc = chatPane.getStyledDocument();
        try{
            doc.insertString(doc.getLength(),text+"\n", attributeSet);

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
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

    public void setIdField(String id)
    {
        idField.setText(id);
    }

    public void clearChat()
    {
        chatPane.setText("");
    }

}




