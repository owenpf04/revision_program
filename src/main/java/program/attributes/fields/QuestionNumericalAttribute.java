package program.attributes.fields;

import program.Question;
import program.Settings;
import program.exceptions.InvalidQuestionNumericalAttributeException;
import program.helpers.ReformatString;

/**
 * An enum containing a list of numeric attributes for a {@link program.Question Question} (eg
 * {@code ATTEMPTED}, {@code CORRECT}), along with a number of related helper methods.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum QuestionNumericalAttribute {
    INDEX,
    ATTEMPTED,
    CORRECT,
    PERCENTAGE,
    EXPECTED_TIMES_ASKED,
    LIKELIHOOD;

    /**
     * Returns true if the provided string matches a {@code QuestionNumericalAttribute} name, when
     * stripped and formatted in SCREAMING_SNAKE_CASE.
     *
     * @param value
     *         the string to be checked.
     *
     * @return true if the provided string matches a {@code QuestionNumericalAttribute} name, false
     *         otherwise.
     */
    public static boolean isNumericalAttribute(String value) {
        value = ReformatString.toScreamingSnakeCase(value);

        try {
            QuestionNumericalAttribute.valueOf(value);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    /**
     * Asserts that a {@code QuestionNumericalAttribute} value {@code value} passed as a parameter
     * is not less than zero.
     *
     * @param value
     *         a double (the value of the provided {@code attribute}) to assert is not less than
     *         zero.
     * @param attribute
     *         the {@code QuestionNumericalAttribute} for which {@code value} is the value.
     *
     * @throws InvalidQuestionNumericalAttributeException
     *         if {@code value} is less than zero.
     */
    private static void assertAttributeNotLessThanZero(double value,
            QuestionNumericalAttribute attribute)
            throws InvalidQuestionNumericalAttributeException {
        if (value < 0) {
            String errorMessage = "\"" + ReformatString.toPlainText(attribute.toString(), false) +
                    "\" value is less than 0!";
            throw new InvalidQuestionNumericalAttributeException(attribute, value, errorMessage);
        }
    }

    /**
     * Asserts that all the provided parameters are valid values for their corresponding
     * {@code QuestionNumericalAttribute}s.
     *
     * @param index
     *         a value for the {@code INDEX} {@code QuestionNumericalAttribute} to assert as valid.
     * @param attempted
     *         a value for the {@code ATTEMPTED} {@code QuestionNumericalAttribute} to assert as
     *         valid.
     * @param correct
     *         a value for the {@code CORRECT} {@code QuestionNumericalAttribute} to assert as
     *         valid.
     * @param percentage
     *         a value for the {@code PERCENTAGE} {@code QuestionNumericalAttribute} to assert as
     *         valid.
     * @param expectedTimesAsked
     *         a value for the {@code EXPECTED_TIMES_ASKED} {@code QuestionNumericalAttribute} to
     *         assert as valid.
     * @param likelihood
     *         a value for the {@code LIKELIHOOD} {@code QuestionNumericalAttribute} to assert as
     *         valid.
     *
     * @throws InvalidQuestionNumericalAttributeException
     *         if any of the provided parameters are invalid values for their corresponding
     *         {@code QuestionNumericalAttribute}s.
     */
    public static void assertAttributesValid(int index, int attempted, int correct,
            double percentage,
            double expectedTimesAsked, double likelihood) throws
            InvalidQuestionNumericalAttributeException {
        assertAttributeNotLessThanZero(index, INDEX);
        assertAttributeNotLessThanZero(attempted, ATTEMPTED);
        assertAttributeNotLessThanZero(correct, CORRECT);
        assertAttributeNotLessThanZero(percentage, PERCENTAGE);
        assertAttributeNotLessThanZero(expectedTimesAsked, EXPECTED_TIMES_ASKED);
        assertAttributeNotLessThanZero(likelihood, LIKELIHOOD);

        if (correct > attempted) {
            String errorMessage =
                    "The \"Correct\" value " + correct + " is greater than \"Attempted\" value " +
                            attempted + ".";
            throw new InvalidQuestionNumericalAttributeException(CORRECT, correct, errorMessage);
        }

        double roundedFilePercentage = Double.parseDouble(String.format("%.2f", percentage));

        double calcPercentage;
        if (attempted == 0) {
            calcPercentage = 0;
        } else {
            calcPercentage = 100 * ((double) correct / attempted);
            //TODO this is a janky way of doing this but it works
            calcPercentage = Double.parseDouble(String.format("%.2f", calcPercentage));
        }

        if (roundedFilePercentage != calcPercentage) {
            String errorMessage = "The \"Percentage\" value for \"Correct\" = " + correct +
                    " and \"Attempted\" = " + attempted + " should be " +
                    calcPercentage + ", but the value provided is " +
                    percentage + ".";
            throw new InvalidQuestionNumericalAttributeException(PERCENTAGE, percentage,
                    errorMessage);
        }

        double roundedFileLikelihood = Double.parseDouble(String.format("%.2f", likelihood));

        double calcLikelihood = Question.calculateLikelihood(attempted, percentage,
                expectedTimesAsked);
        calcLikelihood = Double.parseDouble(String.format("%.2f", calcLikelihood));

        if (roundedFileLikelihood != calcLikelihood) {
            String errorMessage = "The \"Likelihood\" value for \"Attempted\" = " + attempted +
                    ", \"Percentage\" = " + percentage +
                    " and \"Expected times asked\" = " +
                    expectedTimesAsked + " should be " + calcLikelihood +
                    ", but the value provided is " + likelihood + "!";
            throw new InvalidQuestionNumericalAttributeException(LIKELIHOOD, likelihood,
                    errorMessage);
        }
    }
}
