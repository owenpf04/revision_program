package program.GUI;

import com.formdev.flatlaf.ui.FlatEmptyBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setBackground(new Color(238,232,213));
        setLayout(new BorderLayout());

        JPanel innerPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        innerPanel.setLayout(cardLayout);
        innerPanel.add(new HomeSelectFilePanel(), "homeSelectFilePanel");
        innerPanel.add(new HomeFilterQuestionsPanel(), "homeFilterQuestionsPanel");

        JScrollPane scrollPane = new JScrollPane(innerPanel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        add(scrollPane, BorderLayout.CENTER);

        cardLayout.show(innerPanel, "homeSelectFilePanel");
//        cardLayout.show(this, "homeFilterQuestionsPanel");
    }
}
