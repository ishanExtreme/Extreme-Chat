package frontend;

import javax.swing.*;
import java.awt.event.*;

public class CodeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idField;
    private JTextField codeField;
    private JLabel errorLabel;
    private boolean cancelStatus;

    public CodeDialog() {
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
        if(idField.getText().equals(""))
        {
            errorLabel.setText("Unique Id cannot be empty");
            return;
        }
        if(codeField.getText().equals(""))
        {
            errorLabel.setText("Secret Code cannot be empty");
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

    public String getUniqueId()
    {
        return idField.getText();
    }

    public String getSecretCode()
    {
        return codeField.getText();
    }

//    public static void main(String[] args) {
//        CodeDialog dialog = new CodeDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
