package program.GUI;

import program.GUI.fileSelection.HomeSelectFilePanel;
import program.GUI.questionFiltering.HomeFilterQuestionsPanel;
import program.QuestionList;
import program.helpers.Misc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeScrollPane extends JScrollPane {
    private final JFrame mainFrame;
    private JPanel innerPanel;

    public HomeScrollPane(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBorder(new EmptyBorder(0,0,0,0));

        innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(new EmptyBorder( 15, 15, 15, 15));
        setViewportView(innerPanel);
        // fix for artifacting when scrolling backwards - BACKINGSTORE_SCROLL_MODE is stated to
        // provide better performance (which makes sense), but requires significantly more RAM
        // (around double the amount from my primitive testing)
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);

        showSelectFilePanel();
    }

    public void showSelectFilePanel() {
        innerPanel.removeAll();
        innerPanel.add(new HomeSelectFilePanel(mainFrame, this), BorderLayout.CENTER);
    }

    public void showFilterQuestionsPanel(QuestionList questionsFromFile) {
        innerPanel.removeAll();
        innerPanel.add(new HomeFilterQuestionsPanel(mainFrame, this, questionsFromFile));
    }
}
