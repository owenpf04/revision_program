package program.helpers;

/**
 * Helper class storing the number of {@link program.Question Question}s attempted, and answered
 * correctly and incorrectly, in a particular run of the program.
 *
 * <p>These details are not intended to be saved in a database anywhere, and are simply for
 * displaying to the user during the program's operation, so that they can see how many questions
 * they have answered so far, and how well they are doing.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class RunStats {

    /**
     * The number of {@link program.Question Question}s attempted in a particular run of the
     * program.
     */
    private int attempted;

    /**
     * The number of {@link program.Question Question}s answered correctly in a particular run of
     * the program.
     */
    private int correct;

    /**
     * The number of {@link program.Question Question}s answered incorrectly in a particular run of
     * the program.
     */
    private int incorrect;

    /**
     * Creates a new {@code RunStats} object with the values of {@code attempted}, {@code correct}
     * and {@code incorrect} each set to 0.
     */
    public RunStats() {
        attempted = 0;
        correct = 0;
        incorrect = 0;
    }

    /**
     * Increments the values of {@code attempted} and {@code correct}. To be used when a
     * {@link program.Question Question} was answered correctly.
     */
    public void correct() {
        attempted++;
        correct++;
    }

    /**
     * Increments the values of {@code attempted} and {@code incorrect}. To be used when a
     * {@link program.Question Question} was answered incorrectly.
     */
    public void incorrect() {
        attempted++;
        incorrect++;
    }

    /**
     * Returns the number of {@link program.Question Question}s attempted, as tracked by this
     * {@code RunStats} object.
     *
     * @return the number of {@code Question}s attempted, as tracked by this {@code RunStats}
     *         object.
     */
    public int getAttempted() {
        return attempted;
    }

    /**
     * Returns a string representation of this {@code RunStats} object, in the format:
     * <pre>
     *     "Attempted = {@code attempted}; Correct = {@code correct}; Incorrect = {@code incorrect};
     *     Percentage = {@code percentage}"
     * </pre>
     * with {@code percentage} rounded to 1dp.
     *
     * @return a string representation of this {@code RunStats} object.
     */
    public String toString() {
        String returnString =
                "Attempted = " + attempted + "; Correct = " + correct + "; Incorrect = " +
                        incorrect + "; Percentage = " +
                        String.format("%.1f", (double) 100 * correct / attempted);
        return returnString;
    }
}
