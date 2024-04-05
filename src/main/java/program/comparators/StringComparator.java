package program.comparators;

import java.util.Comparator;

/**
 * Comparator for comparing two strings, with whether or not to ignore the case of the strings.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class StringComparator implements Comparator<String> {

    /**
     * Whether or not the comparator should ignore case in the {@link #compare(String, String)}
     * method.
     */
    private final boolean IS_IGNORE_CASE;

    /**
     * Creates a new {@code StringComparator} with the provided parameter.
     *
     * @param ignoreCase
     *         whether or not the comparator should ignore case in the
     *         {@link #compare(String, String)} method.
     */
    public StringComparator(boolean ignoreCase) {
        this.IS_IGNORE_CASE = ignoreCase;
    }

    /**
     * Compares two Strings. If this comparator's value of {@code ignoreCase} is true, this method
     * will return {@link String#compareToIgnoreCase(String) sOne.compareToIgnoreCase(sTwo)};
     * otherwise, it will return {@link String#compareTo(String) sOne.compareTo(sTwo)}.
     *
     * @param sOne
     *         the first String to be compared.
     * @param sTwo
     *         the second String to be compared.
     *
     * @return the result of either {@code sOne.compareToIgnoreCase(sTwo)} or
     *         {@code sOne.compareTo(sTwo)}, depending on this comparator's value of
     *         {@code ignoreCase}.
     */
    public int compare(String sOne, String sTwo) {
        if (IS_IGNORE_CASE) {
            return sOne.compareToIgnoreCase(sTwo);
        } else {
            return sOne.compareTo(sTwo);
        }
    }
}
