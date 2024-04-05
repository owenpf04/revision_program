package program.comparators;

import program.Question;
import program.attributes.fields.QuestionAttribute;

import java.util.Comparator;

/**
 * Comparator for comparing two {@link program.Question Question}s by a provided
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class QuestionAttributeComparator implements Comparator<Question> {

    /**
     * The {@link program.attributes.fields.QuestionAttribute QuestionAttribute} that this
     * comparator should compare by.
     */
    private final QuestionAttribute ATT_TO_COMPARE;

    /**
     * Whether the {@link #compare(Question, Question)} function should compare in reverse (ie
     * compare {@code qTwo} to {@code qOne}) or not.
     */
    private final boolean IS_REVERSE;

    /**
     * Creates a new {@code QuestionAttributeComparator} with the provided parameters.
     *
     * @param attribute
     *         the {@link program.attributes.fields.QuestionAttribute QuestionAttribute} to compare
     *         by.
     * @param reverse
     *         whether the {@link #compare(Question, Question)} function should compare in reverse
     *         (ie compare {@code qTwo} to {@code qOne}) or not.
     */
    public QuestionAttributeComparator(QuestionAttribute attribute, boolean reverse) {
        ATT_TO_COMPARE = attribute;
        this.IS_REVERSE = reverse;
    }

    /**
     * Compares two {@link program.Question Question}s by this particular comparator's
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}, in reverse order if
     * this particular comparator's {@code reverse} value is {@code true}.
     *
     * @param qOne
     *         the first Question to be compared.
     * @param qTwo
     *         the second Question to be compared.
     *
     * @return {@code qOne}'s value for the comparator's attribute compared to {@code qTwo}'s value
     *         for the same attribute, using {@link String#compareTo(String)}.
     */
    public int compare(Question qOne, Question qTwo) {
        String qOneValue = qOne.getQuestionAttribute(ATT_TO_COMPARE);
        String qTwoValue = qTwo.getQuestionAttribute(ATT_TO_COMPARE);

        if (IS_REVERSE) {
            return qTwoValue.compareTo(qOneValue);
        } else {
            return qOneValue.compareTo(qTwoValue);
        }
    }
}
