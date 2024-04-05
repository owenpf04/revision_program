package program.exceptions;

import program.attributes.fields.QuestionNumericalAttribute;

/**
 * Exception to be thrown when a double value is not a valid value for a particular
 * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class InvalidQuestionNumericalAttributeException extends Exception {

    /**
     * The {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * for which the {@code value} was not a valid value.
     */
    private final QuestionNumericalAttribute ATTRIBUTE;

    /**
     * The invalid value for the provided
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}.
     */
    private final double VALUE;

    /**
     * Creates a new {@code InvalidQuestionNumericalAttributeException} with the specified
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute} (for
     * which the {@code value} was not a valid value), and specified (invalid) {@code value}.
     *
     * @param attribute
     *         the {@code QuestionNumericalAttribute} for which the {@code value} was not a valid
     *         value.
     * @param value
     *         the invalid value.
     * @param message
     *         a message to include in the exception.
     */
    public InvalidQuestionNumericalAttributeException(QuestionNumericalAttribute attribute,
            double value,
            String message) {
        super(message);
        this.ATTRIBUTE = attribute;
        this.VALUE = value;
    }

    /**
     * Returns the
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute} for
     * which the {@code value} was not a valid value.
     *
     * @return the {@code QuestionNumericalAttribute} for which the {@code value} was not a valid
     *         value.
     */
    public QuestionNumericalAttribute getAttribute() {
        return ATTRIBUTE;
    }
}
