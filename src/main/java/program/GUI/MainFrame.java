package program.GUI;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import program.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class MainFrame extends JFrame {
    private Settings settings;

    public MainFrame(Settings settings) {
        this.settings = settings;
        FlatJetBrainsMonoFont.install();
        FlatSolarizedLightIJTheme.setup();
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

        setSize(screenWidth * 2 / 3, screenHeight * 2 / 3);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(37,39,40));
        mainPanel.add(new MainTabbedPane(new Color(37,39,40), settings), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Settings settings = new Settings(true);
        MainFrame mainFrame = new MainFrame(settings);
    }
}
