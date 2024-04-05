package program.helpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class containing a number of useful methods for reformatting strings (and arrays of
 * strings) into a variety of formats.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class ReformatString {
    /**
     * Reformats the provided string into a plain text format. Removes excess whitespace, replaces
     * underscores with spaces, and makes all characters lowercase except for the first character of
     * the string, which is made uppercase.
     *
     * <p>If {@code capitaliseEachWord} is {@code true}, then the
     * first character in each word is capitalised (see {@link #capitaliseEachWord(String)}).
     *
     * @param s
     *         the string to reformat.
     * @param capitaliseEachWord
     *         whether the first character of each word should be capitalised, or only the first
     *         character of the entire string.
     *
     * @return the provided string in a plain text format
     */
    public static String toPlainText(String s, Boolean capitaliseEachWord) {
        String returnString = s.strip().toLowerCase().replaceAll("_", " ");
        returnString = returnString.substring(0, 1).toUpperCase() + returnString.substring(1);

        if (capitaliseEachWord) {
            returnString = capitaliseEachWord(returnString);
        }

        return returnString;
    }

    /**
     * Reformats the provided string into SCREAMING_SNAKE_CASE. Removes excess whitespace, makes all
     * characters uppercase, and replaces all spaces with underscores.
     *
     * @param s
     *         the string to reformat.
     *
     * @return the provided string in SCREAMING_SNAKE_CASE.
     */
    public static String toScreamingSnakeCase(String s) {
        return s.strip().toUpperCase().replaceAll(" ", "_");
    }

    /**
     * Reformats the provided string into CamelCase. Equivalent to calling
     * {@link #toPlainText(String, Boolean)} with {@code capitaliseEachWord} as {@code true},
     * followed by removing all spaces.
     *
     * @param s
     *         the string to reformat.
     *
     * @return the provided string in CamelCase.
     */
    public static String toCamelCase(String s) {
        return ReformatString.toPlainText(s, true).replaceAll(" ", "");
    }

    /**
     * {@return the provided string, with the first character in each word capitalised} That is to
     * say, for every space in the provided string, the character immediately proceeding the space
     * is made uppercase, and the result is returned.
     *
     * @param s
     *         the string to reformat.
     */
    public static String capitaliseEachWord(String s) {
        int spaceIndex = 0;

        while (s.indexOf(" ", spaceIndex + 1) != -1) {
            spaceIndex = s.indexOf(" ", spaceIndex + 1);

            String upToFirstLetter = s.substring(0, spaceIndex + 1);
            String firstLetter = s.substring(spaceIndex + 1, spaceIndex + 2).toUpperCase();
            String afterFirstLetter = s.substring(spaceIndex + 2);

            s = upToFirstLetter + firstLetter + afterFirstLetter;
        }

        return s;
    }

    /**
     * Reformats the provided string, removing any excess whitespace or surrounding quotes.
     *
     * <p>'Surrounding quotes' refers to double quotes (") at the first and last position in the
     * string (once stripped of excess whitespace), and they are removed only if the number of
     * double quotes at the start of the string is odd, indicating the outermost pair of quotes are
     * 'surrounding quotes', and not regular double quote characters (indicated by a pair of double
     * quotes "").
     *
     * @param s
     *         the string to reformat.
     *
     * @return the provided string with excess whitespace, or surrounding quotes, removed.
     */
    public static String removeWhitespaceAndQuotes(String s) {
        s = s.strip();

        // TODO should check if num of quotes is odd, not if it isn't two

        if (s.startsWith("\"") && !s.startsWith("\"\"")) {
            s = s.substring(1, s.length() - 1);
        }

        return s;
    }

    /**
     * Returns the provided array of strings, with {@link #removeWhitespaceAndQuotes(String)}
     * applied to each of the strings.
     *
     * @param s
     *         the array of string to reformat.
     *
     * @return the provided array of strings, each with excess whitespace, or surrounding quotes,
     *         removed.
     */
    public static String[] removeWhitespaceAndQuotes(String[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = removeWhitespaceAndQuotes(s[i]);
        }

        return s;
    }

    /**
     * {@return the provided array of strings, with any duplicate strings removed} That is to say,
     * removes any strings which are equal (using {@link String#equals(Object)}) to any previous
     * string in the array.
     *
     * @param s
     *         the array of strings to remove duplicates from.
     */
    public static String[] removeDuplicates(String[] s) {
        Set<String> filteredSet = new HashSet<>(Arrays.asList(s));

        String[] arrayToReturn = new String[filteredSet.size()];
        int count = 0;
        for (String elem : filteredSet) {
            arrayToReturn[count] = elem;
            count++;
        }

        return arrayToReturn;
    }
}
