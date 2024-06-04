package program.GUI;

import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private class TopPanel extends JPanel {
        public TopPanel(Color bgColour) {
            setBackground(bgColour);
        }
    }
    public MainFrame() {
        FlatSolarizedLightIJTheme.setup();
        UIManager.put("TabbedPane.tabHeight", 150);
        UIManager.put("TabbedPane.font", new Font("Segoe UI", 1, 16));
        UIManager.put("TabbedPane.minimumTabWidth", 150);
        UIManager.put("ScrollBar.width", 20);
        UIManager.put("ScrollBar.thumbInsets", new Insets(3, 3, 3, 3));
        UIManager.put("ScrollBar.thumbArc", 15);

        setTitle("This is a frame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();

        setSize(screenWidth * 2 / 3, screenHeight * 2 / 3);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(37,39,40));
        mainPanel.add(new TopPanel(new Color(101,155,94)), BorderLayout.NORTH);
        mainPanel.add(new MainTabbedPane(new Color(37,39,40)), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
    }
}
