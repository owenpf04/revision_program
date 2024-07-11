package program.attributes.fields;

import program.attributes.values.ExamBoard;
import program.attributes.values.PaperOrUnit;
import program.attributes.values.QualificationLevel;
import program.attributes.values.Subject;
import program.exceptions.InvalidQuestionAttributeException;
import program.helpers.ReformatString;

/**
 * An enum containing a list of alphanumeric attributes for a {@link program.Question Question} (eg
 * {@code TITLE}, {@code TOPIC}) and their respective lists of valid values, along with a number of
 * related helper methods.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum QuestionAttribute {
    TITLE(null, "The title of the question."),
    TOPIC(null, "The topic/section to which the question belongs."),
    PAPER_OR_UNIT(PaperOrUnit.getStringValues(), "The paper, or unit, whose " +
            "content contains the question."),
    SUBJECT(Subject.getStringValues(), "The subject to which the question belongs."),
    QUALIFICATION_LEVEL(QualificationLevel.getStringValues(), "The qualification " +
            "level to which the question belongs."),
    EXAM_BOARD(ExamBoard.getStringValues(), "The exam board to which the question " +
            "belongs.");

    /**
     * An array of valid values for a given {@code QuestionAttribute} constant.
     */
    private final String[] VALID_VALUES;
    private final String description;

    /**
     * Creates a new {@code QuestionAttribute} with the provided array as its array of valid
     * values.
     *
     * @param validValues
     *         an array of valid values for the attribute.
     */
    QuestionAttribute(String[] validValues, String description) {
        this.VALID_VALUES = validValues;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns true if the provided string matches a {@code QuestionAttribute} name, when stripped
     * and formatted in SCREAMING_SNAKE_CASE.
     *
     * @param value
     *         the string to be checked.
     *
     * @return true if the provided string matches a {@code QuestionAttribute} name, false
     *         otherwise.
     */
    public static boolean isAttribute(String value) {
        value = ReformatString.toScreamingSnakeCase(value);

        try {
            QuestionAttribute.valueOf(value);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    /**
     * Reformats a valid value for the provided {@code QuestionAttribute} to match the exact
     * capitalisation of the value as specified in the list of valid values. If the attribute does
     * not have a list of valid values, the parameter is simply returned as is.
     *
     * @param value
     *         the valid value for the {@code QuestionAttribute} provided.
     * @param attribute
     *         the {@code QuestionAttribute} for which the value is valid.
     *
     * @return the value {@code validValue} in {@code attribute}'s valid values list for which
     *         {@link String#equalsIgnoreCase(String) value.equalsIgnoreCase(validValue)} is
     *         {@code true}.
     *
     * @throws InvalidQuestionAttributeException
     *         if the provided {@code value} is not a valid value for the {@code QuestionAttribute}
     *         provided, or if {@code value} is blank (using isBlank) if the
     *         {@code QuestionAttribute} doesn't have a list of valid values.
     */
    public static String formatAttributeName(String value, QuestionAttribute attribute)
            throws InvalidQuestionAttributeException {
        if (attribute.VALID_VALUES == null) {
            if (!value.isBlank()) {
                return value;
            } else {
                throw new InvalidQuestionAttributeException(attribute, value);
            }
        }

        String returnString = null;
        for (String elem : attribute.VALID_VALUES) {
            if (elem.equalsIgnoreCase(value)) {
                returnString = elem;
                break;
            }
        }

        if (returnString == null) {
            throw new InvalidQuestionAttributeException(attribute, value);
        }

        return returnString;
    }

    /**
     * Returns the provided array, with {@link #formatAttributeName(String, QuestionAttribute)}
     * applied to each of the values in the array.
     *
     * @param values
     *         an array of values, each of which is to be formatted.
     * @param attribute
     *         the {@code QuestionAttribute} for which the values are valid values.
     *
     * @return the provided array of Strings, with each one formatted to have the correct
     *         capitalisation.
     *
     * @throws InvalidQuestionAttributeException
     *         if any one of the values in {@code values} is not a valid value for the provided
     *         {@code QuestionAttribute}.
     */
    public static String[] formatAttributeNameArray(String[] values, QuestionAttribute attribute)
            throws InvalidQuestionAttributeException {
        for (int i = 0; i < values.length; i++) {
            values[i] = formatAttributeName(values[i], attribute);
        }

        return values;
    }

    /**
     * Returns an array of the parameters in the order provided, with
     * {@link #formatAttributeName(String, QuestionAttribute)} applied to each.
     *
     * @param title
     *         a valid value for the {@code TITLE} {@code QuestionAttribute}.
     * @param topic
     *         a valid value for the {@code TOPIC} {@code QuestionAttribute}.
     * @param paperUnit
     *         a valid value for the {@code PAPER_UNIT} {@code QuestionAttribute}.
     * @param subject
     *         a valid value for the {@code SUBJECT} {@code QuestionAttribute}.
     * @param qualLevel
     *         a valid value for the {@code QUALIFICATION_LEVEL} {@code QuestionAttribute}.
     * @param examBoard
     *         a valid value for the {@code EXAM_BOARD} {@code QuestionAttribute}.
     *
     * @return an array of the parameters provided, with each one formatted to have the correct
     *         capitalisation.
     *
     * @throws InvalidQuestionAttributeException
     *         if any one of the parameters is not a valid value for its corresponding
     *         {@code QuestionAttribute}.
     */
    public static String[] formatAttributeNames(String title, String topic, String paperUnit,
            String subject, String qualLevel, String examBoard) throws
            InvalidQuestionAttributeException {
        String[] values = {title, topic, subject, paperUnit, qualLevel, examBoard};

        values[0] = formatAttributeName(title, QuestionAttribute.TITLE);
        values[1] = formatAttributeName(topic, QuestionAttribute.TOPIC);
        values[3] = formatAttributeName(paperUnit, QuestionAttribute.PAPER_OR_UNIT);
        values[2] = formatAttributeName(subject, QuestionAttribute.SUBJECT);
        values[4] = formatAttributeName(qualLevel, QuestionAttribute.QUALIFICATION_LEVEL);
        values[5] = formatAttributeName(examBoard, QuestionAttribute.EXAM_BOARD);

        return values;
    }

    /**
     * Returns the output of {@link #values()} in a reversed order.
     *
     * @return output of {@link #values()} in a reversed order.
     */
    public static QuestionAttribute[] valuesReversed() {
        QuestionAttribute[] values = values();
        QuestionAttribute[] reversed = new QuestionAttribute[values.length];

        for (int i = 0; i < values.length; i++) {
            reversed[i] = values[values.length - 1 - i];
        }

        return reversed;
    }
}
