package program.GUI;

import program.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeScrollPane extends JScrollPane {
    private Settings settings;

    public HomeScrollPane(Settings settings) {
        this.settings = settings;
        setBackground(new Color(238,232,213));

        JPanel innerPanel = new JPanel();
        setViewportView(innerPanel);
        getVerticalScrollBar().setUnitIncrement(16);

        CardLayout cardLayout = new CardLayout();
        innerPanel.setLayout(cardLayout);
        innerPanel.add(new HomeSelectFilePanel(this), "homeSelectFilePanel");
        innerPanel.add(new HomeFilterQuestionsPanel(), "homeFilterQuestionsPanel");

        setBorder(new EmptyBorder(0,0,0,0));

        cardLayout.show(innerPanel, "homeSelectFilePanel");
//        cardLayout.show(innerPanel, "homeFilterQuestionsPanel");
    }

    public Settings getSettings() {
        return settings;
    }
}
