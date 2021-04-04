import frontend.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        MainWindow window = new MainWindow();
        JFrame frame = new JFrame("Extreme-Chat");
        frame.setContentPane(window.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
