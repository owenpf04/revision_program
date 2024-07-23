package program.GUI.questionFiltering;

import program.GUI.common.SearchTextField;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class FilterPanel extends JPanel {
    private QuestionList questionsFromFile;
    private QuestionsSelectionTableScrollPane tablePane;

    private ArrayList<Filter> filters;

    public FilterPanel(QuestionList questionsFromFile, QuestionsSelectionTableScrollPane tablePane) {
        this.questionsFromFile = questionsFromFile;
        this.tablePane = tablePane;
        this.filters = new ArrayList<>();

        setLayout(new BorderLayout(0,30));

        SearchTextField searchTextField = new SearchTextField(tablePane);
        add(searchTextField, BorderLayout.NORTH);

        JPanel mainFilterPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.putClientProperty("FlatLaf.styleClass", "h0");
        filterLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainFilterPanel.add(filterLabel, gbc);

        gbc.ipady = 8;

        QuestionAttribute[] attributes = QuestionAttribute.valuesReversed();
        for (int i = 0; i < attributes.length - 1; i++) {
            gbc.gridy++;
            QualitativeFilter filter = new QualitativeFilter(attributes[i], 7 - i,
                    questionsFromFile, tablePane);
            filters.add(filter);
            mainFilterPanel.add(filter, gbc);
        }

        QuestionNumericalAttribute[] numericalAttributes = QuestionNumericalAttribute.values();
        for (int i = 1; i < numericalAttributes.length; i++) {
            gbc.gridy++;
            QuantitativeFilter filter;
            switch (numericalAttributes[i]) {
                case PERCENTAGE, LIKELIHOOD -> {
                    filter = new QuantitativeFilter(numericalAttributes[i], i + 7,
                            questionsFromFile, tablePane, 0, 100);
                }
                default -> {
                    filter = new QuantitativeFilter(numericalAttributes[i], i + 7,
                            questionsFromFile, tablePane);
                }
            }
            filters.add(filter);
            mainFilterPanel.add(filter, gbc);
        }

        gbc.gridy = attributes.length + numericalAttributes.length - 1;
        gbc.weighty = 1;
        // filler panel
        mainFilterPanel.add(new JPanel(), gbc);

        add(mainFilterPanel, BorderLayout.CENTER);

        setBorder(new EmptyBorder(5, 10, 0, 30));
    }
}
