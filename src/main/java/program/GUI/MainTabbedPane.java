package program.GUI;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.Settings;

import javax.swing.*;
import java.awt.*;

public class MainTabbedPane extends JTabbedPane {
    public MainTabbedPane(Color bgColour) {
        setBackground(bgColour);
        putClientProperty("JTabbedPane.tabIconPlacement", SwingConstants.TOP);
        setTabPlacement(JTabbedPane.LEFT);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        addTab("Home", FontIcon.of(CarbonIcons.HOME, 40), new HomeScrollPane());
        addTab("Settings", FontIcon.of(CarbonIcons.SETTINGS, 40), new JPanel());
        addTab("About", FontIcon.of(CarbonIcons.INFORMATION, 40), new JPanel());
    }
}
