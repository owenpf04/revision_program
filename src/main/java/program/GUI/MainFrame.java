package program.GUI;

import program.Settings;
import program.helpers.Misc;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("This is a frame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,489));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();

        setSize(screenWidth * 3 / 4, screenHeight * 3 / 4);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Misc.getUIManagerColor("colorMenuBackground"));
        mainPanel.add(new MainTabbedPane(Misc.getUIManagerColor("colorMenuBackground")), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}
