package program.helpers;

import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
//TODO clunky because of questionAttribute and questionNumericalAttribute being separate

/**
 * Helper class representing a key by which to sort a {@link program.QuestionList QuestionList},
 * storing a {@code key} (either a
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} or a
 * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}) and a
 * value {@code isReverse}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class SortingKey {

    /**
     * The {@link program.attributes.fields.QuestionAttribute QuestionAttribute} that the sorting
     * key should be used to sort {@link program.Question Question}s by their values for.
     */
    private QuestionAttribute key;

    /**
     * The {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * that the sorting key should be used to sort {@link program.Question Question}s by their
     * values for.
     */
    private QuestionNumericalAttribute numericalKey;

    /**
     * Whether the key should be used to sort values in ascending (0-9, A-Z; indicated by
     * {@code isReverse = false}) or descending (9-0, Z-A; indicated by {@code isReverse = true})
     * order.
     */
    private boolean isReverse;

    /**
     * Creates a new {@code SortingKey} with the specified
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} as the key (and
     * therefore the
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey} set to null), and {@code isReverse} set to false - indicating the key
     * should be used to sort values in ascending (0-9, A-Z) order.
     *
     * @param key
     *         the {@code QuestionAttribute} that the sorting key should be used to sort
     *         {@link program.Question Question}s by their values for.
     */
    public SortingKey(QuestionAttribute key) {
        this(key, false);
    }

    /**
     * Creates a new {@code SortingKey} with the specified
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute} as
     * the numericalKey (and therefore the
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} set to
     * null), and {@code isReverse} set to false - indicating the key should be used to sort values
     * in ascending (0-9, A-Z) order.
     *
     * @param numericalKey
     *         the {@code QuestionNumericalAttribute} that the sorting key should be used to sort
     *         {@link program.Question Question}s by their values for.
     */
    public SortingKey(QuestionNumericalAttribute numericalKey) {
        this(numericalKey, false);
    }

    /**
     * Creates a new {@code SortingKey} with the specified
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} as the key (and
     * therefore the
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey} set to null), and the specified value of {@code reverse}.
     *
     * @param key
     *         the {@code QuestionAttribute} that the sorting key should be used to sort
     *         {@link program.Question Question}s by their values for.
     * @param reverse
     *         whether the key should be used to sort values in ascending (0-9, A-Z; default,
     *         indicated by {@code reverse = false}) or descending (9-0, Z-A; indicated by
     *         {@code reverse = true}) order.
     */
    public SortingKey(QuestionAttribute key, boolean reverse) {
        this.key = key;
        this.numericalKey = null;
        this.isReverse = reverse;
    }

    /**
     * Creates a new {@code SortingKey} with the specified
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute} as
     * the numericalKey (and therefore the
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} set to
     * null), and the specified value of {@code reverse}.
     *
     * @param numericalKey
     *         the {@code QuestionNumericalAttribute} that the sorting key should be used to sort
     *         {@link program.Question Question}s by their values for.
     * @param reverse
     *         whether the key should be used to sort values in ascending (0-9, A-Z; default,
     *         indicated by {@code reverse = false}) or descending (9-0, Z-A; indicated by
     *         {@code reverse = true}) order.
     */
    public SortingKey(QuestionNumericalAttribute numericalKey, boolean reverse) {
        this.key = null;
        this.numericalKey = numericalKey;
        this.isReverse = reverse;
    }

    /**
     * Returns true if this {@code SortingKey} has a
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} (if
     * {@code key} is not null - only one of {@code key} and {@code numericalKey} can be non-null,
     * as a {@code SortingKey} cannot have both a {@code QuestionAttribute} {@code key} and a
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey}).
     *
     * @return true if this {@code SortingKey}'s {@code QuestionAttribute} {@code key} is not null,
     *         false otherwise.
     */
    public boolean hasQuestionAttributeKey() {
        return key != null;
    }

    /**
     * Returns true if this {@code SortingKey} has a
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey} (if {@code key} is not null - only one of {@code key} and
     * {@code numericalKey} can be non-null, as a {@code SortingKey} cannot have both a
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} and a
     * {@code QuestionNumericalAttribute} {@code numericalKey}).
     *
     * @return true if this {@code SortingKey}'s {@code QuestionNumericalAttribute}
     *         {@code numericalKey} is not null, false otherwise.
     */
    public boolean hasQuestionNumericalAttributeKey() {
        return numericalKey != null;
    }

    /**
     * Returns this {@code SortingKey}'s
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} (you may
     * want to first check if this value has been initialised using
     * {@link #hasQuestionAttributeKey()}, as a {@code SortingKey} cannot have both a
     * {@code QuestionAttribute} {@code key} and a
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey}).
     *
     * @return this {@code SortingKey}'s {@code QuestionAttribute} {@code key}.
     */
    public QuestionAttribute getQuestionAttributeKey() {
        return key;
    }

    /**
     * Returns this {@code SortingKey}'s
     * {@link program.attributes.fields.QuestionNumericalAttribute QuestionNumericalAttribute}
     * {@code numericalKey} (you may want to first check if this value has been initialised using
     * {@link #hasQuestionNumericalAttributeKey()}, as a {@code SortingKey} cannot have both a
     * {@link program.attributes.fields.QuestionAttribute QuestionAttribute} {@code key} and a
     * {@code QuestionNumericalAttribute} {@code numericalKey}).
     *
     * @return this {@code SortingKey}'s {@code QuestionNumericalAttribute} {@code numericalKey}.
     */
    public QuestionNumericalAttribute getQuestionNumericalAttributeKey() {
        return numericalKey;
    }

    /**
     * Returns this {@code SortingKey}'s value of {@code isReverse}.
     *
     * @return this {@code SortingKey}'s value of {@code isReverse}.
     */
    public boolean isReverse() {
        return isReverse;
    }

    /**
     * Sets this {@code SortingKey}'s {@code isReverse} value to the value provided
     *
     * @param reverse
     *         whether the key should be used to sort values in ascending (0-9, A-Z; default,
     *         indicated by {@code reverse = false}) or descending (9-0, Z-A; indicated by
     *         {@code reverse = true}) order.
     */
    public void setReverse(boolean reverse) {
        this.isReverse = reverse;
    }

    /**
     * Returns a string representation of this {@code SortingKey}, in the format:
     * <pre>
     *     "SortingKey: Key = {@code key}; NumericalKey = {@code numericalKey}; Reverse = {@code isReverse}"
     * </pre>
     *
     * @return a string representation of this {@code SortingKey} object.
     */
    public String toString() {
        String returnString =
                "SortingKey: Key = " + key + "; NumericalKey = " + numericalKey + "; " +
                        "Reverse = " + isReverse;
        return returnString;
    }
}
