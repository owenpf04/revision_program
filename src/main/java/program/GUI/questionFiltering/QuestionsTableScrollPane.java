package program.GUI.questionFiltering;

import program.Question;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class QuestionsTableScrollPane extends JScrollPane {
    private QuestionList questions;
    private JTable table;
    private DefaultTableModel tableModel;

    public QuestionsTableScrollPane(QuestionList questions) {
        // see HomeScrollPane for an explanation
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);

        String[] headers = getTableHeaders();
        Object[][] data = questionListTo2DArray(questions);

        tableModel = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return column == 0;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                switch (columnIndex) {
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
        table.setRowSorter(new TableRowSorter<>(tableModel));

        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Bahnschrift", Font.PLAIN, 20));

        table.setShowHorizontalLines(true);
        table.setRowHeight(30);

        setColumnWidths();

        this.setViewportView(table);
    }

    private static String[] getTableHeaders() {
        QuestionAttribute[] qAttributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] qNAttributes = QuestionNumericalAttribute.values();

        String[] headers = new String[1 + qAttributes.length + qNAttributes.length];

        headers[0] = "Selected";
        headers[1] = ReformatString.toPlainText(qNAttributes[0].toString(), true);

        int i = 0;
        for (i = 0; i < qAttributes.length; i++) {
            headers[1 + i + 1] = ReformatString.toPlainText(qAttributes[i].toString(),
                    true);
        }

        for (int j = 1; j < qNAttributes.length; j++) {
            headers[1 + i + j] = ReformatString.toPlainText(qNAttributes[j].toString(),
                    true);
        }

        return headers;
    }

    private static Object[][] questionListTo2DArray(QuestionList questionList) {
        ArrayList<Question> questions = questionList.getQuestions();
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
    private static Object[] questionToArray(Question question) {
        QuestionAttribute[] qAttributes = QuestionAttribute.values();
        QuestionNumericalAttribute[] qNAttributes = QuestionNumericalAttribute.values();

        int numFields = qAttributes.length +
                qNAttributes.length;

        Object[] returnArray = new Object[1 + numFields];

        returnArray[0] = true;
        returnArray[1] = question.getQuestionNumericalAttribute(qNAttributes[0]);

        int i;
        for (i = 0; i < qAttributes.length; i++) {
            returnArray[i + 2] = question.getQuestionAttribute(qAttributes[i]);
        }

        for (int j = 1; j < qNAttributes.length; j++) {
            returnArray[1 + i + j] = question.getQuestionNumericalAttribute(qNAttributes[j]);
        }

        return returnArray;
    }

    public void updateData(QuestionList questions) {
        this.questions = questions;
        tableModel.setDataVector(questionListTo2DArray(questions), getTableHeaders());
        setColumnWidths();
    }

    private void setColumnWidths() {
        setColumnWidthToHeaderWidth(0);
        setColumnWidthToHeaderWidth(1);

        int qAttributesLength = QuestionAttribute.values().length;
        int qNAttributesLength = QuestionNumericalAttribute.values().length;

        int i;
        for (i = 0; i < qAttributesLength; i++) {
            TableColumn column = table.getColumnModel().getColumn(i + 2);
            switch (i + 2) {
                case 2 -> {
                    column.setPreferredWidth(400);
                }
                case 3, 5 -> {
                    column.setPreferredWidth(150);
                }
                default -> {
                    setColumnWidthToHeaderWidth(i + 2);
                }
            }
        }

        for (int j = 1; j < qNAttributesLength; j++) {
            setColumnWidthToHeaderWidth(1 + i + j);
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
}
