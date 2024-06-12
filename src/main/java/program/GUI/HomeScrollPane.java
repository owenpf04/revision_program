package program.GUI;

import program.QuestionList;
import program.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeScrollPane extends JScrollPane {
    private Settings settings;
    private QuestionList questionsFromFile;

    private JPanel cardPanel;
    private CardLayout cardPanelLayout;

    public HomeScrollPane(Settings settings) {
        this.settings = settings;
        setBackground(new Color(238,232,213));

        cardPanel = new JPanel();
        setViewportView(cardPanel);
        getVerticalScrollBar().setUnitIncrement(16);

        cardPanelLayout = new CardLayout();
        cardPanel.setLayout(cardPanelLayout);
        cardPanel.add(new HomeSelectFilePanel(this, settings), "homeSelectFilePanel");

        setBorder(new EmptyBorder(0,0,0,0));

        cardPanelLayout.show(cardPanel, "homeSelectFilePanel");
    }

    public Settings getSettings() {
        return settings;
    }

    public JPanel getCardPanel() {
        return cardPanel;
    }

    public CardLayout getCardPanelLayout() {
        return cardPanelLayout;
    }

    public void addFilterQuestionsPanel(QuestionList questionsFromFile) {
        cardPanel.add(new HomeFilterQuestionsPanel(questionsFromFile), "homeFilterQuestionsPanel");
    }
}
