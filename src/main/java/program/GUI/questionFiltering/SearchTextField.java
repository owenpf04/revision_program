package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchTextField extends JTextField {
    private QuestionsTableScrollPane tablePane;
    private ArrayList<JCheckBoxMenuItem> checkBoxMenuItems;

    public SearchTextField(QuestionsTableScrollPane tablePane) {
        this.tablePane = tablePane;
        checkBoxMenuItems = new ArrayList<>();

        putClientProperty("FlatLaf.styleClass", "medium");
        putClientProperty("JTextField.showClearButton", true);
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
        menuBar.setBorder(new EmptyBorder(0, 0, 0, 0));

        JMenu menu = new JMenu("Columns");

        menu.add(createSelectAllItem());
        menu.addSeparator();

        QuestionAttribute[] attributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] numericalAttributes = QuestionNumericalAttribute.values();

        JCheckBoxMenuItem item = new JCheckBoxMenuItem(ReformatString.toPlainText(
                numericalAttributes[0].toString(), false));
        checkBoxMenuItems.add(item);

        for (int i = 0; i < attributes.length; i++) {
            JCheckBoxMenuItem tempItem = new JCheckBoxMenuItem(ReformatString.toPlainText(
                    attributes[i].toString(), false));
            checkBoxMenuItems.add(tempItem);
        }

        for (int i = 1; i < numericalAttributes.length; i++) {
            JCheckBoxMenuItem tempItem = new JCheckBoxMenuItem(ReformatString.toPlainText(
                    numericalAttributes[i].toString(), false));
            checkBoxMenuItems.add(tempItem);
        }

        for (JCheckBoxMenuItem checkBoxItem : checkBoxMenuItems) {
            menu.add(checkBoxItem);
            checkBoxItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateSearchFilter();
                }
            });
        }

        checkBoxMenuItems.get(1).setSelected(true);

        menu.addSeparator();
        menu.add(createClearSelectionItem());

        menuBar.add(menu);

        return menuBar;
    }

    private JMenuItem createSelectAllItem() {
        JMenuItem selectAll = new JMenuItem("Select all");
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBoxMenuItem item : checkBoxMenuItems) {
                    item.setSelected(true);
                }
                updateSearchFilter();
            }
        });

        return selectAll;
    }

    private JMenuItem createClearSelectionItem() {
        JMenuItem clearSelection = new JMenuItem("Clear selection");
        clearSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBoxMenuItem item : checkBoxMenuItems) {
                    item.setSelected(false);
                }
                checkBoxMenuItems.get(1).setSelected(true);
                updateSearchFilter();
            }
        });

        return clearSelection;
    }

    private void updateSearchFilter() {
        RowFilter<TableModel, Integer> searchFilter = new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(
                    Entry<? extends TableModel, ? extends Integer> entry) {
                for (int i = 0; i < checkBoxMenuItems.size(); i++) {
                    if (!checkBoxMenuItems.get(i).isSelected()) {
                        continue;
                    }

                    String value = entry.getStringValue(i + 1).toUpperCase();
                    if (value.contains(getText().toUpperCase())) {
                        return true;
                    }
                }

                return false;

            }
        };
        tablePane.setSearchFilter(searchFilter);
    }
}
