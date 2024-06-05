package program.exceptions;

import program.attributes.fields.QuestionAttribute;
import program.helpers.ReformatString;

/**
 * Exception to be thrown when a String value is not a valid value for a particular
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class InvalidQuestionAttributeException extends Exception {

    /**
     * The {@link program.attributes.fields.QuestionAttribute QuestionAttribute} for which the
     * {@code value} was not a valid value.
     */
    private final QuestionAttribute ATTRIBUTE;

    /**
     * The invalid value for the provided
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
     */
    private final String VALUE;

    /**
     * Creates a new {@code InvalidQuestionAttributeException} with the specified
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} (for which the
     * {@code value} was not a valid value), and specified (invalid) {@code value}.
     *
     * @param attribute
     *         the {@code QuestionAttribute} for which the {@code value} was not a valid value.
     * @param value
     *         the invalid value.
     */
    public InvalidQuestionAttributeException(QuestionAttribute attribute, String value) {
        this(attribute, value, ("\"" + value + "\" is not a valid " +
                ReformatString.toCamelCase(attribute.toString()) + "."));
    }

    /**
     * Creates a new {@code InvalidQuestionAttributeException} with the specified
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} (for which the
     * {@code value} was not a valid value), specified (invalid) {@code value}, and specified
     * {@code message}.
     *
     * @param attribute
     *         the {@code QuestionAttribute} for which the {@code value} was not a valid value.
     * @param value
     *         the invalid value.
     * @param message
     *         a message to include in the exception.
     */
    public InvalidQuestionAttributeException(QuestionAttribute attribute, String value,
            String message) {
        super(message);
        this.ATTRIBUTE = attribute;
        this.VALUE = value;
    }
}
