package program;

import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.exceptions.InvalidQuestionAttributeException;
import program.exceptions.InvalidQuestionNumericalAttributeException;
import program.helpers.ReformatString;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class Question {
    private Map<QuestionAttribute, String> attributeStringMap;
    private Map<QuestionNumericalAttribute, Number> numericalAttributeNumberMap;

    /**
     * Constructor for initialising a revision_program.Question object with the provided parameters
     *
     * @param title
     *         The title of the question.
     * @param topic
     *         The topic of the question.
     * @param paperUnit
     *         The paper or unit to which the question - and topic - belong.
     * @param subject
     *         The subject to which the question belongs.
     * @param qualLevel
     *         The qualification level of the subject to which the question belongs (e.g. A Level,
     *         GCSE etc.)
     * @param examBoard
     *         The exam board of the subject to which the question belongs.
     * @param attempted
     *         The number of times the question has previously been attempted/asked.
     * @param correct
     *         The number of times the question has previously been answered correctly.
     * @param expectedTimesAsked
     *         The sum of (1 / number of questions in current question pool) for each iteration
     *         where this revision_program.Question object was in the question pool. Consider a
     *         question 'Q' on the topic of Forensic. If the set of all 12 Forensic questions were
     *         asked 6 times, and then the set of all 48 Paper 3 questions (of which 'Q' is a
     *         member) were asked 24 times, the value of expectedTimesAsked would be 6 * (1 / 12) +
     *         24 * ( 1 / 48) = (6 / 12) + (24 / 48) = 1, meaning that Q would be expected to have
     *         been asked exactly once among all 54 questions asked, assuming all questions have
     *         equal likelihood.
     *
     * @throws IllegalArgumentException
     *         if any of the provided parameters are invalid.
     */
    public Question(int index, String title, String topic, String paperUnit, String subject,
            String qualLevel, String examBoard, int attempted, int correct, double percentage,
            double expectedTimesAsked, double likelihood, Settings settings) throws IllegalArgumentException {
        String[] qAtts = {title, topic, paperUnit, subject, qualLevel, examBoard};
        qAtts = ReformatString.removeWhitespaceAndQuotes(qAtts);
        try {
            qAtts = QuestionAttribute.formatAttributeNames(qAtts[0], qAtts[1], qAtts[2], qAtts[3],
                    qAtts[4], qAtts[5]);
        } catch (InvalidQuestionAttributeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        Map<QuestionAttribute, String> attributeStringMap = new HashMap<>();

        attributeStringMap.put(QuestionAttribute.TITLE, qAtts[0]);
        attributeStringMap.put(QuestionAttribute.TOPIC, qAtts[1]);
        attributeStringMap.put(QuestionAttribute.PAPER_OR_UNIT, qAtts[3]);
        attributeStringMap.put(QuestionAttribute.SUBJECT, qAtts[2]);
        attributeStringMap.put(QuestionAttribute.QUALIFICATION_LEVEL, qAtts[4]);
        attributeStringMap.put(QuestionAttribute.EXAM_BOARD, qAtts[5]);

        try {
            QuestionNumericalAttribute.assertAttributesValid(index, attempted, correct, percentage,
                    expectedTimesAsked, likelihood, settings);
        } catch (InvalidQuestionNumericalAttributeException e) {
            //TODO this needs changing as it is CLI-specific
            if (e.getAttribute() == QuestionNumericalAttribute.PERCENTAGE || e.getAttribute() ==
                    QuestionNumericalAttribute.LIKELIHOOD) {
                System.out.print(e);
                System.out.println(" Overwriting with correct value and continuing...");
            } else {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        Map<QuestionNumericalAttribute, Number> numericalAttributeNumberMap = new HashMap<>();

        numericalAttributeNumberMap.put(QuestionNumericalAttribute.INDEX, index);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.ATTEMPTED, attempted);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.CORRECT, correct);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.EXPECTED_TIMES_ASKED,
                expectedTimesAsked);
        if (attempted == 0) {
            percentage = 0.0;
        } else {
            percentage = 100 * ((double) correct / attempted);
        }
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.PERCENTAGE, percentage);
        likelihood = calculateLikelihood(attempted, percentage, expectedTimesAsked, settings);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.LIKELIHOOD, likelihood);

        this.attributeStringMap = attributeStringMap;
        this.numericalAttributeNumberMap = numericalAttributeNumberMap;
    }

    public String getQuestionAttribute(QuestionAttribute attribute) {
        return attributeStringMap.get(attribute);
    }

    public String getTitle() {
        return getQuestionAttribute(QuestionAttribute.TITLE);
    }

    public String getTopic() {
        return getQuestionAttribute(QuestionAttribute.TOPIC);
    }

    public String getPaperUnit() {
        return getQuestionAttribute(QuestionAttribute.PAPER_OR_UNIT);
    }

    public String getSubject() {
        return getQuestionAttribute(QuestionAttribute.SUBJECT);
    }

    public String getQualLevel() {
        return getQuestionAttribute(QuestionAttribute.QUALIFICATION_LEVEL);
    }

    public String getExamBoard() {
        return getQuestionAttribute(QuestionAttribute.EXAM_BOARD);
    }

    public Number getQuestionNumericalAttribute(QuestionNumericalAttribute attribute) {
        return numericalAttributeNumberMap.get(attribute);
    }

    public int getIndex() {
        return (int) getQuestionNumericalAttribute(QuestionNumericalAttribute.INDEX);
    }

    public int getCorrect() {
        return (int) getQuestionNumericalAttribute(QuestionNumericalAttribute.CORRECT);
    }

    public int getAttempted() {
        return (int) getQuestionNumericalAttribute(QuestionNumericalAttribute.ATTEMPTED);
    }

    public double getPercentage() {
        return (double) getQuestionNumericalAttribute(QuestionNumericalAttribute.PERCENTAGE);
    }

    public double getExpectedTimesAsked() {
        return (double) getQuestionNumericalAttribute(
                QuestionNumericalAttribute.EXPECTED_TIMES_ASKED);
    }

    public double getLikelihood() {
        return (double) getQuestionNumericalAttribute(QuestionNumericalAttribute.LIKELIHOOD);
    }

    public static double calculateLikelihood(int attempted, double percentage,
            double expectedTimesAsked, Settings settings) {
        if (expectedTimesAsked == 0) {
            return 0;
        }
        double calcLikelihoodPercentComp = 100 - percentage;
        double calcLikelihoodTimesAskedComp = 50 - ((50 * (attempted - expectedTimesAsked)) /
                (((settings.getTimesAskedPercentageOffset() /
                        100) * expectedTimesAsked) +
                        settings.getTimesAskedAbsoluteOffset()));
        calcLikelihoodTimesAskedComp = Math.max(calcLikelihoodTimesAskedComp, 0);
        calcLikelihoodTimesAskedComp = Math.min(calcLikelihoodTimesAskedComp, 100);
        double calculatedLikelihood =
                ((settings.getPercentageWeighting() * calcLikelihoodPercentComp) +
                        calcLikelihoodTimesAskedComp) / (settings.getPercentageWeighting() + 1);
        return calculatedLikelihood;
    }

    public void answeredCorrect(int questionsInPool, Settings settings) {
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.CORRECT, this.getCorrect() + 1);
        this.answeredIncorrect(questionsInPool, settings);
    }

    public void answeredIncorrect(int questionsInPool, Settings settings) {
        int attempted = this.getAttempted();
        int correct = this.getCorrect();

        attempted++;
        double percentage = 100 * ((double) correct / attempted);

        numericalAttributeNumberMap.put(QuestionNumericalAttribute.ATTEMPTED, attempted);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.PERCENTAGE, percentage);

        this.notAsked(questionsInPool, settings);
    }

    public void notAsked(int questionsInPool, Settings settings) {
        int attempted = this.getAttempted();
        double expectedTimesAsked = this.getExpectedTimesAsked();
        double percentage = this.getPercentage();

        expectedTimesAsked += (double) 1 / questionsInPool;
        double likelihood = calculateLikelihood(attempted, percentage, expectedTimesAsked, settings);

        numericalAttributeNumberMap.put(QuestionNumericalAttribute.EXPECTED_TIMES_ASKED,
                expectedTimesAsked);
        numericalAttributeNumberMap.put(QuestionNumericalAttribute.LIKELIHOOD, likelihood);
    }

    //TODO should be implemented in questionComparator instead
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Question) {
            Question otherQ = (Question) obj;

            for (QuestionAttribute attribute : QuestionAttribute.values()) {
                String thisValue = this.getQuestionAttribute(attribute);
                String otherValue = otherQ.getQuestionAttribute(attribute);

                if (!thisValue.equals(otherValue)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        String stringToReturn = this.getExamBoard() + " " + this.getQualLevel() + " " +
                this.getSubject() + " (" + this.getTopic() +
                ") question: " + this.getTitle();
        return stringToReturn;
    }
}
