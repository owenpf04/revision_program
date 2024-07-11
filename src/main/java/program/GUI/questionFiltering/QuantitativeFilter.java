package program.GUI.questionFiltering;

import com.formdev.flatlaf.icons.FlatOptionPaneErrorIcon;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionList;
import program.attributes.fields.QuestionNumericalAttribute;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuantitativeFilter extends Filter {
    private int originalMin;
    private int originalMax;
    private int selectedMin;
    private int selectedMax;

    public QuantitativeFilter(QuestionNumericalAttribute attribute, int columnNum,
            QuestionList questionsFromFile, QuestionsTableScrollPane tablePane,
            int min, int max) {
        this(attribute, columnNum, questionsFromFile, tablePane);

        originalMin = min;
        originalMax = max;
    }
    public QuantitativeFilter(QuestionNumericalAttribute attribute, int columnNum,
            QuestionList questionsFromFile, QuestionsTableScrollPane tablePane) {
        super(columnNum, questionsFromFile, tablePane, attribute.toString(),
                attribute.getDescription());

        originalMin = 0;
        originalMax = (int) Math.floor(questionsFromFile.getMaxValue(attribute).doubleValue());
        selectedMin = originalMin;
        selectedMax = originalMax;

        optionsPanel = createOptionsPanel();

        headingButton.addActionListener(new HeadingButtonListener());
    }

    private JPanel createOptionsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel rangePanel = new JPanel();
        rangePanel.setLayout(new BoxLayout(rangePanel, BoxLayout.Y_AXIS));

        JPanel minSliderPanel = createNewSliderPanel(selectedMin, true);
        JPanel maxSliderPanel = createNewSliderPanel(selectedMax, false);
        rangePanel.add(minSliderPanel);
        rangePanel.add(maxSliderPanel);

        mainPanel.add(rangePanel, BorderLayout.NORTH);
        return mainPanel;
    }

    private JPanel createNewSliderPanel(int selectedValue, boolean isMin) {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        String title = isMin ? "Min" : "Max";
        JLabel label = new JLabel(title + ":");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.putClientProperty("FlatLaf.styleClass", "large");

        JLabel value = new JLabel(String.valueOf(selectedValue));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.putClientProperty("FlatLaf.styleClass", "medium");

        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0,
                originalMax, selectedValue);
        slider.setSnapToTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(originalMax);
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean wasValid = selectedMax >= selectedMin;

                if (isMin) {
                    selectedMin = slider.getValue();
                    value.setText(String.valueOf(selectedMin));
                } else {
                    selectedMax = slider.getValue();
                    value.setText(String.valueOf(selectedMax));
                }

                List<RowFilter<TableModel, Integer>> minFilters = new ArrayList<>();
                List<RowFilter<TableModel, Integer>> maxFilters = new ArrayList<>();

                minFilters.add(RowFilter.numberFilter(
                            RowFilter.ComparisonType.EQUAL, selectedMin, columnNum));
                minFilters.add(RowFilter.numberFilter(
                        RowFilter.ComparisonType.AFTER, selectedMin, columnNum));

                maxFilters.add(RowFilter.numberFilter(
                        RowFilter.ComparisonType.EQUAL, selectedMax, columnNum));
                maxFilters.add(RowFilter.numberFilter(
                        RowFilter.ComparisonType.BEFORE, selectedMax, columnNum));

                RowFilter<TableModel, Integer> minFilter = RowFilter.orFilter(minFilters);
                RowFilter<TableModel, Integer> maxFilter = RowFilter.orFilter(maxFilters);

                List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                filters.add(minFilter);
                filters.add(maxFilter);

                tablePane.setColumnFilter(RowFilter.andFilter(filters), columnNum);

                boolean isValid = selectedMax >= selectedMin;

                if (wasValid && !isValid) {
                    invalidSelection();
                } else if (!wasValid && isValid) {
                    validSelection();
                }
            }
        });

        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(value, BorderLayout.CENTER);
        mainPanel.add(slider, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void invalidSelection() {
        JLabel invalidSelection = new JLabel("Min cannot be greater than max.",
                new FlatOptionPaneErrorIcon(), SwingConstants.LEFT);
        invalidSelection.putClientProperty("FlatLaf.styleClass", "large");

        optionsPanel.add(invalidSelection, BorderLayout.CENTER);
    }

    private void validSelection() {
        optionsPanel.remove(1);
    }

    private class HeadingButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (expanded) {
                expanded = false;

                removeAll();
                headingButton = createHeadingButton(tooltip);
                headingButton.addActionListener(new HeadingButtonListener());
                add(headingButton, BorderLayout.NORTH);
            } else {
                expanded = true;

                removeAll();
                headingButton = createHeadingButton(tooltip);
                headingButton.addActionListener(new HeadingButtonListener());
                add(headingButton, BorderLayout.NORTH);
                add(optionsPanel, BorderLayout.CENTER);
            }
        }
    }
}
