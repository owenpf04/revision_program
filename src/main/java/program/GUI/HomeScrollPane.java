package program.GUI;

import program.GUI.fileSelection.HomeSelectFilePanel;
import program.GUI.questionFiltering.HomeFilterQuestionsPanel;
import program.QuestionList;
import program.Settings;
import program.helpers.Misc;

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
        setBackground(Misc.getUIManagerColor("colorMenuBackground"));

        cardPanel = new JPanel();
        setViewportView(cardPanel);
        // fix for artifacting when scrolling backwards - BACKINGSTORE_SCROLL_MODE is stated to
        // provide better performance (which makes sense), but requires significantly more RAM
        // (around double the amount from my primitive testing)
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);

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
