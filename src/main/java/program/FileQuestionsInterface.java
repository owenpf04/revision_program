package program;

import program.attributes.fields.QuestionAttribute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class containing all of the methods used to interface between the program and the file containing
 * {@link Question} data.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class FileQuestionsInterface {

    /**
     * The expected first line of the file containing {@link Question} data.
     */
    final static String COLUMN_HEADERS =
            "Index,Title,Topic,Paper or unit,Subject,Qualification level," +
                    "Exam board,Attempted,Correct,Percentage,Expected times asked," +
                    "Likelihood";

    /**
     * Returns a new {@link QuestionList} containing all of the {@link Question}s specified in the
     * .csv file at the provided {@code fileLocation}.
     *
     * @param fileLocation
     *         the location of the .csv file containing the {@code Question} data.
     *
     * @return a new {@code QuestionList} containing all of the {@code Question}s specified in the
     *         .csv file at the provided {@code fileLocation}.
     *
     * @throws FileNotFoundException
     *         if no file could be found at the provided {@code fileLocation}.
     * @throws IllegalArgumentException
     *         if the file at the provided {@code fileLocation} could not be entirely parsed for any
     *         reason.
     */
    public static QuestionList createQuestionList(String fileLocation) throws FileNotFoundException,
            IllegalArgumentException {
        QuestionList questions;

        try {
            ArrayList<Question> questionsArrayList = getQuestionsFromFile(fileLocation);
            questions = new QuestionList(questionsArrayList, fileLocation);

            //TODO console output should be moved to CLI.java?
            System.out.println(questions.getNumQuestions() + " questions successfully loaded.");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File at location \"" + fileLocation +
                    "\" not found! Details:\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("File at location \"" + fileLocation +
                    "\" could not be parsed! Details:\n" +
                    e.getMessage());
        }

        return questions;
    }

    /**
     * Returns a new ArrayList of {@link Question}s from the .csv file at the provided
     * {@code fileLocation} (one {@code Question} per line).
     *
     * @param fileLocation
     *         the location of the .csv file containing the {@code Question} data.
     *
     * @return a new ArrayList of {@code Question}s from the .csv file at the provided
     *         {@code fileLocation}.
     *
     * @throws FileNotFoundException
     *         if no file could be found at the provided {@code fileLocation}.
     * @throws IllegalArgumentException
     *         if any of the lines in the file at the provided {@code fileLocation} are invalid.
     */
    public static ArrayList<Question> getQuestionsFromFile(String fileLocation) throws
            FileNotFoundException,
            IllegalArgumentException {
        File file = new File(fileLocation);
        Scanner fileScanner = new Scanner(file);
        ArrayList<Question> questions = new ArrayList<>();

        String firstLine = fileScanner.nextLine();
        if (!firstLine.equals(COLUMN_HEADERS)) {
            throw new IllegalArgumentException("First line (headers) \"" + firstLine +
                    "\" does not match expected headers \"" +
                    COLUMN_HEADERS + "\" !");
        }

        int lineCount = 1;

        while (fileScanner.hasNext()) {
            lineCount++;

            String line = fileScanner.nextLine();
            Question question = null;

            try {
                question = parseFileLine(line);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Line #" + lineCount + " - \"" + line +
                        "\" - is invalid! Details:\n" +
                        e.getMessage());
            }

            questions.add(question);
        }

        return questions;
    }

    /**
     * Returns a new {@link Question} object, created using the parameters specified in the provided
     * {@code line}. If the {@code line} cannot be parsed for any reason (more or less fields than
     * expected, fields in wrong order, invalid field etc), an {@code IllegalArgumentException} will
     * be thrown.
     *
     * @param line
     *         a string from which a new {@code Question} should be created, with the values for
     *         each field separated by commas.
     *
     * @return a new {@code Question} object, created using the parameters specified in the provided
     *         {@code line}.
     *
     * @throws IllegalArgumentException
     *         if the {@code line} cannot be parsed for any reason.
     */
    private static Question parseFileLine(String line) throws IllegalArgumentException {
        Object[] questionAttributes = new Object[12];

        if (line.isBlank()) {
            throw new IllegalArgumentException("\"" + line + "\" has 0 fields, expected " +
                    questionAttributes.length + "!");
        }

        String lineSegment = line;
        for (int i = 0; i < questionAttributes.length - 1; i++) {
            String section;
            int firstSeparatingCommaIndex;

            try {
                firstSeparatingCommaIndex = getFirstSeparatingCommaIndex(lineSegment);
                section = lineSegment.substring(0, firstSeparatingCommaIndex);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("\"" + line + "\" has " + (i + 1) +
                        " fields, expected " +
                        questionAttributes.length + "!");
            }

            switch (i) {
                // TODO this whole section is very hard-wired, maybe make it more generalised?
                // These are the cases where the field is to be interpreted as a String; title,
                // topic
                // paperUnit, subject, qualificationLevel and examBoard
                case 1, 2, 3, 4, 5, 6 -> questionAttributes[i] = section;

                // These are the cases where the field is to be interpreted as an integer; index,
                // attempted and correct
                case 0, 7, 8 -> {
                    try {
                        questionAttributes[i] = Integer.parseInt(section);
                    } catch (NumberFormatException e) {
                        if (i == 0) {
                            throw new IllegalArgumentException("Value of " + (i + 1) +
                                    "st field \"index\" in \"" +
                                    line +
                                    "\" (\"" + section +
                                    "\") is not an integer!");
                        } else if (i == 7) {
                            throw new IllegalArgumentException("Value of " + (i + 1) +
                                    "th field \"attempted\" in" +
                                    " \"" +
                                    line +
                                    "\" (\"" + section +
                                    "\") is not an integer!");
                        } else {
                            throw new IllegalArgumentException("Value of " + (i + 1) +
                                    "th field \"correct\" in " +
                                    "\"" +
                                    line +
                                    "\" (\"" + section +
                                    "\") is not an integer!");
                        }
                    }
                }

                // These are the cases where the field is to be interpreted as a double; percentage,
                // expectedTimesAsked and likelihood (below)
                case 9, 10 -> {
                    try {
                        questionAttributes[i] = Double.parseDouble(section);
                    } catch (NumberFormatException e) {
                        if (i == 9) {
                            throw new IllegalArgumentException("Value of " + (i + 1) +
                                    "th field \"percentage\" " +
                                    "in \"" +
                                    line +
                                    "\" (\"" + section +
                                    "\") is not a number!");
                        } else {
                            throw new IllegalArgumentException("Value of " + (i + 1) +
                                    "th field " +
                                    "\"expectedTimesAsked\" in" +
                                    " \"" +
                                    line +
                                    "\" (\"" + section +
                                    "\") is not a number!");
                        }
                    }
                }
            }

            lineSegment = lineSegment.substring(firstSeparatingCommaIndex + 1);
        }

        try {
            questionAttributes[questionAttributes.length - 1] = Double.parseDouble(lineSegment);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value of " + (questionAttributes.length) +
                    "th field \"likelihood\" in \"" + line +
                    "\" (\"" + lineSegment +
                    "\") is not a number!");
        }

        return new Question((int) questionAttributes[0], (String) questionAttributes[1],
                (String) questionAttributes[2], (String) questionAttributes[3], (String)
                questionAttributes[4],
                (String) questionAttributes[5], (String) questionAttributes[6],
                (int) questionAttributes[7], (int) questionAttributes[8],
                (double) questionAttributes[9],
                (double) questionAttributes[10], (double) questionAttributes[11]);
    }

    /**
     * Returns the index of the first comma in the provided {@code line} which is not enclosed by
     * surrounding quotes (ie a comma which is being used to separate fields, not one which is a
     * character in a field).
     *
     * <p>This is determined by the number of double quote characters (") between
     * any comma and the previous comma in the {@code line} (or the start of the line if there is no
     * previous comma). If the number of double quote characters between these two commas is even,
     * this means the latter comma is a field-separating comma, and not a comma character in a
     * field.
     *
     * @param line
     *         the string on which the search is to be performed
     *
     * @return the index of the first comma in the provided {@code line} which is not enclosed by
     *         surrounding quotes
     */
    private static int getFirstSeparatingCommaIndex(String line) {
        int quoteCount;
        int commaIndex = 0;

        do {
            quoteCount = 0;
            commaIndex = line.indexOf(',', commaIndex + 1);

            for (int i = 0; i < commaIndex; i++) {
                if (line.charAt(i) == '"') {
                    quoteCount++;
                }
            }
        } while (quoteCount % 2 != 0);

        return commaIndex;
    }

    /**
     * Overwrites the contents of the file at the file location specified by the provided
     * {@link QuestionList} (see {@link QuestionList#getFileLocation()}) with all of the questions
     * in the provided {@code QuestionList}.
     *
     * <p>Writes the {@link #COLUMN_HEADERS} to the first line of the file, and then writes the
     * attributes of each {@link Question} in the provided list on a new line, and in the order
     * specified by the {@code COLUMN_HEADERS}.
     *
     * @param questions
     *         the {@code QuestionList} whose {@code Question}s are to be written to the file at its
     *         specified file location.
     *
     * @throws IOException
     *         if specified location cannot be written to for any reason.
     */
    public static void saveToFile(QuestionList questions)
            throws IOException {
        String fileLocation = questions.getFileLocation();
        FileWriter fileWriter = new FileWriter(fileLocation);

        fileWriter.write(COLUMN_HEADERS + "\n");

        ArrayList<Question> questionsList = questions.getQuestions();
        for (int i = 0; i < questionsList.size() - 1; i++) {
            writeQuestionToFile(fileWriter, questionsList.get(i), false);
        }
        writeQuestionToFile(fileWriter, questionsList.get(questionsList.size() - 1), true);

        fileWriter.close();
    }

    /**
     * Writes the attributes of the provided {@link Question} to the file to which the provided
     * {@code fileWriter} is writing, in the correct order (as specified by
     * {@link #COLUMN_HEADERS}).
     *
     * @param fileWriter
     *         the {@code FileWriter} object to be used to write the contents of the
     *         {@code Question}.
     * @param question
     *         the {@code Question} whose contents are to be written.
     * @param isLastQuestion
     *         whether or not this {@code Question} is the last one to be written to the file
     *         (meaning that a newline character "\n" should not be inserted afterwards).
     *
     * @throws IOException
     *         if an I/O error occurs (see {@link FileWriter#write(String)}).
     */
    private static void writeQuestionToFile(FileWriter fileWriter, Question question,
            boolean isLastQuestion) throws IOException {
        fileWriter.write(question.getIndex() + ",");

        for (QuestionAttribute attribute : QuestionAttribute.values()) {
            fileWriter.write(question.getQuestionAttribute(attribute) + ",");
        }

        fileWriter.write(question.getAttempted() + ",");
        fileWriter.write(question.getCorrect() + ",");
        fileWriter.write(String.format("%.2f", question.getPercentage()) + ",");
        fileWriter.write(String.format("%.2f", question.getExpectedTimesAsked()) + ",");
        fileWriter.write(String.format("%.2f", question.getLikelihood()));

        if (!isLastQuestion) {
            fileWriter.write("\n");
        }
    }
}
