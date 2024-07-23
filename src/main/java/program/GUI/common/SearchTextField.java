package program.GUI.common;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.GUI.questionFiltering.QuestionsSelectionTableScrollPane;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SearchTextField extends JTextField {
    private QuestionsSelectionTableScrollPane tablePane;
    private ArrayList<JCheckBox> checkBoxes;

    public SearchTextField(QuestionsSelectionTableScrollPane tablePane) {
        this.tablePane = tablePane;
        checkBoxes = new ArrayList<>();

        putClientProperty("FlatLaf.styleClass", "large");
        putClientProperty("JTextField.placeholderText", "Search");

        int fontHeight = getFontMetrics(getFont()).getHeight();
        putClientProperty("JTextField.leadingIcon", FontIcon.of(CarbonIcons.SEARCH,
                (int) (fontHeight * 0.9)));

        JMenuBar menuBar = createMenuBar();

        putClientProperty("JTextField.trailingComponent", menuBar);

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchFilter();
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        // For some reason setting the background to a colour makes it transparent, and is the only
        // way I could find of doing so
        menuBar.setBackground(new Color(0, 0, 0));
        menuBar.setBorder(new EmptyBorder(0, 0, 0, 0));

        JMenu menu = new JMenu();
        menu.setIcon(FontIcon.of(CarbonIcons.EDIT_FILTER,
                (int) (0.9 * getFontMetrics(getFont()).getHeight())));

        menu.add(createSelectAllItem());
        menu.addSeparator();

        QuestionAttribute[] attributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] numericalAttributes = QuestionNumericalAttribute.values();

        JCheckBox item = new JCheckBox(ReformatString.toPlainText(
                numericalAttributes[0].toString(), false));
        checkBoxes.add(item);

        for (int i = 0; i < attributes.length; i++) {
            JCheckBox tempItem = new JCheckBox(ReformatString.toPlainText(
                    attributes[i].toString(), false));
            checkBoxes.add(tempItem);
        }

        for (int i = 1; i < numericalAttributes.length; i++) {
            JCheckBox tempItem = new JCheckBox(ReformatString.toPlainText(
                    numericalAttributes[i].toString(), false));
            checkBoxes.add(tempItem);
        }

        for (JCheckBox checkBox : checkBoxes) {
            checkBox.putClientProperty("FlatLaf.styleClass", "medium");
            menu.add(checkBox);
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSearchFilter();
                }
            });
        }

        checkBoxes.get(1).setSelected(true);

        menu.addSeparator();
        menu.add(createResetSelectionItem());

        menuBar.add(menu);

        return menuBar;
    }

    private JMenuItem createSelectAllItem() {
        JMenuItem selectAll = new JMenuItem("Select all");
        selectAll.putClientProperty("FlatLaf.styleClass", "medium");
        selectAll.setIcon(FontIcon.of(CarbonIcons.LIST_CHECKED,
                (int) (0.9 * getFontMetrics(selectAll.getFont()).getHeight())));
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox item : checkBoxes) {
                    item.setSelected(true);
                }
                updateSearchFilter();
            }
        });

        return selectAll;
    }

    private JMenuItem createResetSelectionItem() {
        JMenuItem resetSelection = new JMenuItem("Reset selection");
        resetSelection.putClientProperty("FlatLaf.styleClass", "medium");
        resetSelection.setIcon(FontIcon.of(CarbonIcons.LIST_BOXES,
                        (int) (0.9 * getFontMetrics(resetSelection.getFont()).getHeight())));
        resetSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox item : checkBoxes) {
                    item.setSelected(false);
                }
                checkBoxes.get(1).setSelected(true);
                updateSearchFilter();
            }
        });

        return resetSelection;
    }

    private void updateSearchFilter() {
        RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(
                    Entry<? extends TableModel, ? extends Integer> entry) {
                boolean noneSelected = true;
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (!checkBoxes.get(i).isSelected()) {
                        continue;
                    }

                    noneSelected = false;
                    String value = entry.getStringValue(i + 1).toUpperCase();
                    if (value.contains(getText().toUpperCase())) {
                        return true;
                    }
                }

                if (noneSelected) {
                    return true;
                } else {
                    return false;
                }

            }
        };
        tablePane.setSearchFilter(searchFilter);
    }
}
