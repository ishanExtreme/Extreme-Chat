package frontend;

import javax.swing.*;
import java.awt.event.*;

public class ClientDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField hostName;
    private JTextField portName;
    private JTextField secretName;
    private JLabel errorLabel;
    private boolean cancelStatus;


    public ClientDialog() {
        cancelStatus = false;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if(hostName.getText().equals(""))
        {
            errorLabel.setText("Host Name cannot be empty");
            return;
        }
        if(portName.getText().equals(""))
        {
            errorLabel.setText("Port Number cannot be empty");
            return;
        }
        if(secretName.getText().equals(""))
        {
            errorLabel.setText("Secret Code cannot be empty");
            return;
        }
        if(secretName.getText().length()<5)
        {
            errorLabel.setText("Secret Code must be grater than 4 characters");
            return;
        }

        cancelStatus = false;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        cancelStatus = true;
        dispose();
    }

    public boolean getCancelStatus() {
        return cancelStatus;
    }

    public String getHostName() {
        return hostName.getText();
    }

    public int getPortNumber() {
        return Integer.parseInt(portName.getText());
    }

    public String getSecretCode() {
        return secretName.getText();
    }

}
