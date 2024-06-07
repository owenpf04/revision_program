package program.GUI;

import program.Settings;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Settings settings;

    public MainFrame(Settings settings) {
        this.settings = settings;

        UIManager.put("TabbedPane.tabHeight", 150);
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("TabbedPane.minimumTabWidth", 150);
        UIManager.put("ScrollBar.width", 20);
        UIManager.put("ScrollBar.thumbInsets", new Insets(3, 3, 3, 3));
        UIManager.put("ScrollBar.thumbArc", 15);

        setTitle("This is a frame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,489));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();

        setSize(screenWidth * 3 / 4, screenHeight * 3 / 4);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(37,39,40));
        mainPanel.add(new MainTabbedPane(new Color(37,39,40), settings), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}
