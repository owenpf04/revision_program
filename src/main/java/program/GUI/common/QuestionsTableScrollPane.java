package program.GUI.common;

import program.Question;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.helpers.Misc;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsTableScrollPane extends JScrollPane {
    private List<QuestionAttribute> attributesToDisplay;
    private List<QuestionNumericalAttribute> numericalAttributesToDisplay;
    private boolean includeSelectedBox;
    private QuestionList questions;
    private JTable table;
    private DefaultTableModel tableModel;
    protected TableRowSorter<TableModel> sorter;
    protected RowFilter<TableModel, Integer> searchFilter;

    public QuestionsTableScrollPane(QuestionList questions,
            List<QuestionAttribute> attributesToDisplay,
            List<QuestionNumericalAttribute> numericalAttributesToDisplay,
            boolean includeSelectedBox) {
        this.questions = questions;
        this.attributesToDisplay = attributesToDisplay;
        this.numericalAttributesToDisplay = numericalAttributesToDisplay;
        this.includeSelectedBox = includeSelectedBox;

        // see HomeScrollPane for an explanation
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);

        searchFilter = new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                return true;
            }
        };

        String[] headers = getTableHeaders();
        Object[][] data = questionListTo2DArray(questions);

        tableModel = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 0 && includeSelectedBox);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                int offset = 1;
                if (includeSelectedBox) {
                    offset--;
                }
                switch (columnIndex + offset) {
                    case 0 -> {
                        return Boolean.class;
                    }
                    case 1, 8, 9 -> {
                        return Integer.class;
                    }
                    case 2, 3, 4, 5, 6, 7 -> {
                        return String.class;
                    }
                    case 10, 11, 12 -> {
                        return Double.class;
                    }
                    default -> {
                        return Object.class;
                    }
                }
            }
        };

        table = new JTable(tableModel);

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.putClientProperty("FlatLaf.styleClass", "large");

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getTableHeader().putClientProperty("FlatLaf.styleClass", "h1.regular");
        table.getTableHeader().setFont(new Font("Bahnschrift", Font.PLAIN, 20));

        table.setShowHorizontalLines(true);
        table.setRowHeight((int) (1.5 * table.getRowHeight()));

        setColumnWidths();
        setVisibleColumns();

//        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        setViewportView(table);
    }

    private String[] getTableHeaders() {
        QuestionAttribute[] qAttributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] qNAttributes = QuestionNumericalAttribute.values();

        int start = 0;
        if (includeSelectedBox) {
            start++;
        }
        String[] headers = new String[start + qAttributes.length + qNAttributes.length];
        if (includeSelectedBox) {
            headers[0] = "Selected";
        }
        headers[start] = ReformatString.toPlainText(qNAttributes[0].toString(), true);

        int i = 0;
        for (i = 0; i < qAttributes.length; i++) {
            headers[start + i + 1] = ReformatString.toPlainText(qAttributes[i].toString(),
                    true);
        }

        for (int j = 1; j < qNAttributes.length; j++) {
            headers[start + i + j] = ReformatString.toPlainText(qNAttributes[j].toString(),
                    true);
        }

        return headers;
    }

    private Object[][] questionListTo2DArray(QuestionList questionList) {
        List<Question> questions = questionList.getQuestions();
        final int numFields = QuestionAttribute.values().length +
                QuestionNumericalAttribute.values().length;
        Object[][] returnArray = new Object[questions.size()][numFields];

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            returnArray[i] = questionToArray(question);
        }

        return returnArray;
    }

    //TODO similar loop to getTableHeaders, can they be combined?
    private Object[] questionToArray(Question question) {
        QuestionAttribute[] qAttributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] qNAttributes = QuestionNumericalAttribute.values();

        int start = 0;
        if (includeSelectedBox) {
            start++;
        }
        Object[] returnArray = new Object[start + qAttributes.length + qNAttributes.length];
        if (includeSelectedBox) {
            returnArray[0] = true;
        }
        returnArray[start] = question.getQuestionNumericalAttribute(qNAttributes[0]);

        int i;
        for (i = 0; i < qAttributes.length; i++) {
            returnArray[start + i + 1] = question.getQuestionAttribute(qAttributes[i]);
        }

        for (int j = 1; j < qNAttributes.length; j++) {
            returnArray[start + i + j] = question.getQuestionNumericalAttribute(qNAttributes[j]);
        }

        return returnArray;
    }

    private void setColumnWidths() {
        int qAttributesLength = QuestionAttribute.values().length;
        int qNAttributesLength = QuestionNumericalAttribute.values().length;

        setColumnWidthToHeaderWidth(0);

        int offset = 0;
        if (includeSelectedBox) {
            setColumnWidthToHeaderWidth(1);
            offset++;
        }
        int i;
        for (i = 0; i < qAttributesLength; i++) {
            TableColumn column = table.getColumnModel().getColumn(i + 1 + offset);
            switch (i) {
                case 0 -> {
                    column.setPreferredWidth(400);
                }
                case 1, 3 -> {
                    column.setPreferredWidth(150);
                }
                default -> {
                    setColumnWidthToHeaderWidth(i + 1 + offset);
                }
            }
        }

        for (int j = 1; j < qNAttributesLength; j++) {
            setColumnWidthToHeaderWidth(i + j + offset);
        }
    }

    private void setColumnWidthToHeaderWidth(int index) {
        TableColumn column = table.getColumnModel().getColumn(index);
        column.setPreferredWidth(getHeaderWidth(index, column) + 25);
    }

    private int getHeaderWidth(int index, TableColumn column) {
        TableCellRenderer renderer = column.getHeaderRenderer();

        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component header = renderer.getTableCellRendererComponent(table, column.getHeaderValue(),
                false, false, -1, index);
        return header.getPreferredSize().width;
    }

    private void setVisibleColumns() {
        TableColumnModel columnModel = table.getColumnModel();

        int offset = 0;
        if (includeSelectedBox) {
            offset++;
        }

        QuestionAttribute[] qAttributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] qNAttributes = QuestionNumericalAttribute.values();

        int i = qAttributes.length;

        for (int j = qNAttributes.length - 1; j > 0; j--) {
            if (!numericalAttributesToDisplay.contains(qNAttributes[j])) {
                columnModel.removeColumn(columnModel.getColumn(i + j + offset));
            }
        }

        for (i = qAttributes.length - 1; i >= 0; i--) {
            if (!attributesToDisplay.contains(qAttributes[i])) {
                columnModel.removeColumn(columnModel.getColumn(1 + i + offset));
            }
        }

        if (!numericalAttributesToDisplay.contains(qNAttributes[0])) {
            columnModel.removeColumn(columnModel.getColumn(0 + offset));
        }
    }

    private void updateFirstColumnCheckboxes() {
        TableCellRenderer renderer = table.getDefaultRenderer(Boolean.class);
        JCheckBox checkBoxRenderer = (JCheckBox) renderer;
        Misc.updateCheckboxIcons(checkBoxRenderer, 21);

        DefaultCellEditor editor = (DefaultCellEditor) table.getDefaultEditor(Boolean.class);
        JCheckBox checkBoxEditor = (JCheckBox) editor.getComponent();
        Misc.updateCheckboxIcons(checkBoxEditor, 21);
    }

    public void setSearchFilter(RowFilter<TableModel, Integer> searchFilter) {
        this.searchFilter = searchFilter;
        updateFilters();
    }

    protected void updateFilters() {;
        sorter.setRowFilter(searchFilter);
    }

    public QuestionList getSelectedQuestions() {
        if (!includeSelectedBox) {
            throw new UnsupportedOperationException("The method getSelectedQuestions() cannot be " +
                    "applied to an AbstractQuestionTableScrollPane which doesn't have 'Selected' " +
                    "checkboxes (ie one which was constructed with param includeSelectedBox = false.");
        }
        List<Question> allQuestions = questions.getQuestions();
        List<Question> selectedQuestions = new ArrayList<>();

        for (int i = 0; i < allQuestions.size(); i++) {
            if (sorter.convertRowIndexToView(i) != -1 && (boolean) tableModel.getValueAt(i,
                    0)) {
                selectedQuestions.add(allQuestions.get(i));
            }
        }

        return new QuestionList(selectedQuestions);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //TODO can cause slight jump just as table becomes smaller than viewport size, as 'getWidth'
        // does not take into account width of scrollbar so it doesn't adjust at quite the right moment
        if (getWidth() > table.getPreferredSize().width) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }
}
