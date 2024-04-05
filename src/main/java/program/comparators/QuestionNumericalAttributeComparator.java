package program.comparators;

import program.Question;
import program.attributes.fields.QuestionNumericalAttribute;

import java.util.Comparator;

/**
 * Comparator for comparing two {@link program.Question Question}s by a provided
 * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class QuestionNumericalAttributeComparator implements Comparator<Question> {

    /**
     * The {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * that this comparator should compare by.
     */
    private final QuestionNumericalAttribute ATT_TO_COMPARE;

    /**
     * Whether the {@link #compare(Question, Question)} function should compare in reverse (ie
     * compare {@code qTwo} to {@code qOne}) or not.
     */
    private final boolean IS_REVERSE;

    /**
     * Creates a new {@code QuestionNumericalAttributeComparator} with the provided parameters.
     *
     * @param attribute
     *         the
     *         {@link program.attributes.fields.QuestionNumericalAttribute
     *         QuestionNumericalAttribute} to compare by.
     * @param reverse
     *         whether the {@link #compare(Question, Question)} function should compare in reverse
     *         (ie compare {@code qTwo} to {@code qOne}) or not.
     */
    public QuestionNumericalAttributeComparator(QuestionNumericalAttribute attribute,
            boolean reverse) {
        ATT_TO_COMPARE = attribute;
        this.IS_REVERSE = reverse;
    }

    /**
     * Compares two {@link program.Question Question}s by this particular comparator's
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}, in
     * reverse order if this particular comparator's {@code reverse} value is {@code true}.
     *
     * @param qOne
     *         the first Question to be compared.
     * @param qTwo
     *         the second Question to be compared.
     *
     * @return {@code qOne}'s value for the comparator's attribute compared to {@code qTwo}'s value
     *         for the same attribute, using {@link Double#compare(double, double)}.
     */
    public int compare(Question qOne, Question qTwo) {
        double qOneValue = qOne.getQuestionNumericalAttribute(ATT_TO_COMPARE).doubleValue();
        double qTwoValue = qTwo.getQuestionNumericalAttribute(ATT_TO_COMPARE).doubleValue();

        if (IS_REVERSE) {
            return Double.compare(qTwoValue, qOneValue);
        } else {
            return Double.compare(qOneValue, qTwoValue);
        }
    }
}
