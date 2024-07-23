package program.GUI.questionFiltering;

import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.helpers.Misc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QualitativeFilter extends Filter {
    Set<String> options;
    Set<String> selectedOptions;
    public QualitativeFilter(QuestionAttribute attribute, int columnNum, QuestionList questionsFromFile,
            QuestionsSelectionTableScrollPane tablePane) {
        super(columnNum, questionsFromFile, tablePane, attribute.toString(),
                attribute.getDescription());

        this.options = questionsFromFile.getValues(attribute);
        this.selectedOptions = new HashSet<>();
        optionsPanel = createOptionsPanel();

        headingButton.addActionListener(new HeadingButtonListener());
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 30, 5, 5));

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkBox.putClientProperty("FlatLaf.styleClass", "large");

            if (selectedOptions.contains(option)) {
                checkBox.setSelected(true);
            }

            Misc.updateCheckboxIcons(checkBox, 21);
            checkBox.addActionListener(new CheckboxListener());

            panel.add(checkBox);
        }

        return panel;
    }

    private class HeadingButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (expanded) {
                expanded = false;

                removeAll();
                headingButton = createHeadingButton();
                headingButton.addActionListener(new HeadingButtonListener());
                add(headingButton, BorderLayout.NORTH);
            } else {
                expanded = true;

                removeAll();
                headingButton = createHeadingButton();
                headingButton.addActionListener(new HeadingButtonListener());
                add(headingButton, BorderLayout.NORTH);
                add(optionsPanel, BorderLayout.CENTER);
            }
        }
    }
    private class CheckboxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (((JCheckBox) e.getSource()).isSelected()) {
                selectedOptions.add(e.getActionCommand());
            } else {
                selectedOptions.remove(e.getActionCommand());
            }

            if (!selectedOptions.isEmpty()) {
                List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                for (String option : selectedOptions) {
                    filters.add(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(
                                Entry<? extends TableModel, ? extends Integer> entry) {
                            if (entry.getStringValue(columnNum).equals(option)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }

                tablePane.setColumnFilter(RowFilter.orFilter(filters), columnNum);
            } else {
                tablePane.setColumnFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(
                            Entry<? extends TableModel, ? extends Integer> entry) {
                        return true;
                    }
                }, columnNum);
            }
        }
    }
}
