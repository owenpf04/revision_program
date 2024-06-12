package program;

import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.comparators.QuestionAttributeComparator;
import program.comparators.QuestionNumericalAttributeComparator;
import program.comparators.StringComparator;
import program.helpers.ReformatString;
import program.helpers.SortingKey;

import java.util.*;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class QuestionList {
    private ArrayList<Question> questions;

    public QuestionList(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getNumQuestions() {
        return questions.size();
    }

    public Question getFirstQuestion() {
        return questions.get(0);
    }

    public QuestionList selectByAttributeArray(Collection<String> values, QuestionAttribute attribute) {
        ArrayList<Question> matchingQuestions = new ArrayList<>();
        StringComparator comparator;

        if (attribute.equals(QuestionAttribute.TITLE) ||
                attribute.equals(QuestionAttribute.TOPIC)) {
            comparator = new StringComparator(true);
        } else {
            comparator = new StringComparator(false);
        }

        for (Question elem : questions) {
            for (String value : values) {
                if (comparator.compare(elem.getQuestionAttribute(attribute), value) == 0) {
                    matchingQuestions.add(elem);
                }
            }
        }

        return new QuestionList(matchingQuestions);
    }

    public QuestionList sortByAttribute(QuestionAttribute attribute, boolean reverse) {
        QuestionList sortedList = this;
        Collections.sort(sortedList.questions, new QuestionAttributeComparator(attribute, reverse));
        return sortedList;
    }

    public QuestionList sortByNumericalAttribute(QuestionNumericalAttribute attribute,
            boolean reverse) {
        QuestionList sortedList = this;
        Collections.sort(sortedList.questions,
                new QuestionNumericalAttributeComparator(attribute, reverse));
        return sortedList;
    }

    public QuestionList sort(SortingKey key) {
        QuestionList sortedList = this;
        if (key.hasQuestionAttributeKey()) {
            sortedList = sortedList.sortByAttribute(key.getQuestionAttributeKey(), key.isReverse());
        } else if (key.hasQuestionNumericalAttributeKey()) {
            sortedList = sortedList.sortByNumericalAttribute(key.getQuestionNumericalAttributeKey(),
                    key.isReverse());
        } else {
            throw new RuntimeException("Provided SortingKey \"" + key + "\" has neither a " +
                    "QuestionAttributeKey nor a " +
                    "QuestionNumericalAttributeKey!");
        }

        return sortedList;
    }

    // TODO not needed?
//    public QuestionList replaceChangedValues(QuestionList alteredQList) {
//        ArrayList<Question> originalList = this.questions;
//        ArrayList<Question> alteredList = alteredQList.questions;
//
//        for (Question elem : alteredList) {
//            int origListIndex = Collections.binarySearch(originalList, elem, new
//            QuestionComparator());
//
//            if (origListIndex >= 0) {
//                originalList.set(origListIndex, elem);
//            } else {
//                //TODO 'elem' from alteredQList is not present in 'this'
//                throw new IllegalArgumentException("some message");
//            }
//        }
//        return new QuestionList(originalList, this.fileLocation);
//    }

    public void answeredFirstQuestion(Boolean isCorrect, Settings settings) {
        answeredQuestion(0, isCorrect, settings);
    }

    public void answeredQuestion(int index, boolean isCorrect, Settings settings) {
        ArrayList<Question> questionsList = this.questions;
        int questionsInPool = questionsList.size();

        if (isCorrect) {
            questionsList.get(index).answeredCorrect(questionsInPool, settings);
        } else {
            questionsList.get(index).answeredIncorrect(questionsInPool, settings);
        }

        for (int i = 0; i < questionsInPool; i++) {
            if (i != index) {
                questionsList.get(i).notAsked(questionsInPool, settings);
            }
        }
    }

    public String listQuestions(SortingKey sortingKey) {
        ArrayList<Question> questionsToDisplay = questions;

        if (sortingKey != null) {
            questionsToDisplay = this.sort(sortingKey).questions;
        }

        String returnString = "";

        int number = 0;
        for (Question elem : questionsToDisplay) {
            number++;
            returnString += ("#" + number);

            if (sortingKey != null && sortingKey.hasQuestionNumericalAttributeKey()) {
                QuestionNumericalAttribute att = sortingKey.getQuestionNumericalAttributeKey();
                String value = String.format("%.2f", (double) elem.getQuestionNumericalAttribute(att));

                returnString += " (" + ReformatString.toPlainText(att.toString(), false) +
                        " = " + value + ")";
            }

            returnString += ("   -   " + elem + "\n");
        }

        return returnString;
    }

    public Set<String> getValues(QuestionAttribute attribute) {
        Set<String> returnSet = new TreeSet<>();

        for (Question question : questions) {
            returnSet.add(question.getQuestionAttribute(attribute));
        }

        return returnSet;
    }
}
