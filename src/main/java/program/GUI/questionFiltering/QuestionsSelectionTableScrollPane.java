package program.GUI.questionFiltering;

import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionsSelectionTableScrollPane extends program.GUI.common.QuestionsTableScrollPane {
    private List<RowFilter<TableModel, Integer>> columnFilters;

    public QuestionsSelectionTableScrollPane(QuestionList questions) {
        super(questions, Arrays.asList(QuestionAttribute.values()),
                Arrays.asList(QuestionNumericalAttribute.values()),
                true);

        columnFilters = new ArrayList<>();

        for (int i = 0; i < (QuestionAttribute.values().length +
                QuestionNumericalAttribute.values().length + 1); i++) {
            columnFilters.add(new RowFilter<TableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    return true;
                }
            });
        }
    }

    public void setColumnFilter(RowFilter<TableModel, Integer> columnFilter, int column) {
        columnFilters.set(column, columnFilter);
        updateFilters();
    }

    @Override
    protected void updateFilters() {
        List<RowFilter<TableModel, Integer>> allFilters = new ArrayList<>(columnFilters);
        allFilters.add(searchFilter);
        sorter.setRowFilter(RowFilter.andFilter(allFilters));
    }
}
