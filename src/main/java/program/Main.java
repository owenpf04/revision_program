package program;

import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.exceptions.ReturnHomeException;
import program.helpers.ReformatString;
import program.helpers.RunStats;
import program.helpers.SortingKey;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Main class which carries out the running of the program in a high-level, abstracted manner,
 * calling a number of other methods in various classes which actually implement each of the
 * high-level methods in this class.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */

//TODO add way to read settings from file, and update them
// TODO functions should handle an empty QuestionList gracefully
public class Main {

    /**
     * Main method of the program.
     *
     * @param args
     *         command-line arguments.
     */
    public static void main(String[] args) {
        boolean isCommandLine = true;

        QuestionList questionsFromFile = null;

        // Loading file initially
        try {
            questionsFromFile = loadFile(args, isCommandLine);
        } catch (ReturnHomeException e) {
            exitProgram(questionsFromFile, isCommandLine);
        }

        boolean exit = false;
        do {
            String homePromptResponse = null;

            try {
                homePromptResponse = homePrompt(isCommandLine);
            } catch (ReturnHomeException e) {
                continue;
            }

            if (homePromptResponse.equals("select")) {
                try {
                    QuestionList questionSelection = selectQuestions(questionsFromFile,
                            isCommandLine);
                    RunType runType = determineRunType(isCommandLine);
                    run(questionSelection, runType, isCommandLine);
                } catch (ReturnHomeException ignored) {
                }
            } else if (homePromptResponse.equals("list")) {
                try {
                    QuestionList questionSelection = selectQuestions(questionsFromFile,
                            isCommandLine);
                    SortingKey sortingKey = getSortingKey(RunType.CUSTOM, isCommandLine);
                    displaySelection(questionSelection, sortingKey, "Your sorted selection:",
                            isCommandLine);
                } catch (ReturnHomeException ignored) {
                }
            } else {
                exitProgram(questionsFromFile, isCommandLine);
            }
        } while (!exit);
    }

    /**
     * Returns a new {@link QuestionList} created from the contents of a .csv file. The location of
     * this file is either obtained from the command-line arguments, if there are any, or from the
     * user directly if not.
     *
     * @param args
     *         command-line arguments.
     * @param isCommandLine
     *         whether interactions with the user are to be via the command line
     *         {@code isCommandLine = true}, or via the GUI {@code isCommandLine = false}.
     *
     * @return a new {@code QuestionList} created from the contents of a .csv file.
     *
     * @throws IllegalArgumentException
     *         if a file location is provided by the CL args and the file at this location is
     *         non-existent or in any way invalid.
     */
    public static QuestionList loadFile(String[] args, boolean isCommandLine) throws
            IllegalArgumentException {
        QuestionList questions = null;

        if (args.length > 1) {
            throw new IllegalArgumentException("Expected maximum 1 command line argument, got " +
                    args.length + "!");
        } else if (args.length == 1) {
            String fileLocation = ReformatString.removeWhitespaceAndQuotes(args[0]);

            try {
                questions = FileQuestionsInterface.createQuestionList(fileLocation);
            } catch (FileNotFoundException | IllegalArgumentException e) {
                throw new IllegalArgumentException("File at location \"" + fileLocation +
                        "\" (provided by command-line " +
                        "arguments) invalid! Details:\n" +
                        e.getMessage());
            }
        } else {
            if (isCommandLine) {
                questions = CLI.getQuestionList();
            }
        }
        return questions;
    }

    /**
     * Displays the home page message (see {@link Settings#getUponHomePage()})
     *
     * @param isCommandLine
     *
     * @return
     */
    public static String homePrompt(boolean isCommandLine) {
        String response = null;

        if (isCommandLine) {
            String[] validResponses = {"select", "list", Settings.getExitCommand()};
            response = CLI.getResponse(Settings.getUponHomePage(), validResponses, false,
                    false, false);
        }

        return response;
    }

    //TODO javaDoc only talks about questionAttributes, not numericalQuestionAttributes

    /**
     * Obtains and returns a filtered subset of the provided QuestionList parameter. Asks the user
     * to input desired values for each of the QuestionAttributes of the Question, and returns a
     * QuestionList containing only Questions which satisfied every set of conditions supplied by
     * the user
     *
     * @param questionsFromFile
     *         the QuestionList to filter
     *
     * @return a subset of the QuestionList provided, filtered as per the conditions supplied by the
     *         user
     */
    public static QuestionList selectQuestions(QuestionList questionsFromFile,
            boolean isCommandLine) {
        QuestionList filteredQuestions = questionsFromFile;

        displayMessage(Settings.getUponFilter(), isCommandLine);

        for (QuestionAttribute att : QuestionAttribute.valuesReversed()) {
            if (isCommandLine) {
                filteredQuestions = CLI.selectQuestions(filteredQuestions, att, false);
            }
        }

        displaySelection(filteredQuestions, null, "Your current selection:",
                isCommandLine);

        boolean doRemove = doRemoveQuestions(isCommandLine);
        if (doRemove) {
            displayMessage(Settings.getUponFilterRemove(), isCommandLine);

            for (QuestionAttribute att : QuestionAttribute.valuesReversed()) {
                if (isCommandLine) {
                    filteredQuestions = CLI.selectQuestions(filteredQuestions, att, true);
                }
            }

            displaySelection(filteredQuestions, null, "Your final selection:",
                    isCommandLine);
        }

        return filteredQuestions;
    }

    public static QuestionList updateQuestionList(QuestionList originalList,
            QuestionList selectedList,
            boolean remove) {
        if (remove) {
            ArrayList<Question> updatedArray = originalList.getQuestions();

            updatedArray.removeAll(selectedList.getQuestions());
            return new QuestionList(updatedArray, originalList.getFileLocation());
        } else {
            return selectedList;
        }
    }

    public static void displayMessage(String message, boolean isCommandLine) {
        if (isCommandLine) {
            System.out.println(message);
        }
    }

    public static boolean doRemoveQuestions(boolean isCommandLine) {
        boolean removeQuestions = true;

        if (isCommandLine) {
            removeQuestions = CLI.doRemoveQuestions();
        }

        return removeQuestions;
    }

    public static void displaySelection(QuestionList selection, SortingKey sortingKey,
            String precedingMessage, boolean isCommandLine) {
        if (isCommandLine) {
            CLI.displaySelection(selection, sortingKey, precedingMessage);
        }
    }

    /**
     * Returns a RunType as specified by user input
     *
     * @return RunType specified by user input
     */
    public static RunType determineRunType(boolean isCommandLine) {
        RunType runType = null;

        if (isCommandLine) {
            runType = CLI.determineRunType();
        }

        return runType;
    }

    public static void run(QuestionList questions, RunType runType, boolean isCommandLine) {
        SortingKey sortingKey = getSortingKey(runType, isCommandLine);

        displayMessage(Settings.getUponRun(), isCommandLine);

        if (runType.equals(RunType.REVISE)) {
            runRevise(questions, sortingKey, isCommandLine);
        } else {
            runOrdered(questions, sortingKey, isCommandLine);
        }
    }

    public static SortingKey getSortingKey(RunType runType, boolean isCommandLine) {
        SortingKey sortingKey;

        if (runType.equals(RunType.REVISE)) {
            sortingKey = new SortingKey(QuestionNumericalAttribute.LIKELIHOOD, true);
        } else if (runType.equals(RunType.TEST)) {
            sortingKey = new SortingKey(QuestionNumericalAttribute.INDEX, false);
        } else {
            sortingKey = getCustomSortingAttribute(isCommandLine);

            boolean reverse = getCustomReverse(isCommandLine);
            sortingKey.setReverse(reverse);
        }
        return sortingKey;
    }

    public static SortingKey getCustomSortingAttribute(boolean isCommandLine) {
        SortingKey keyToReturn = null;

        if (isCommandLine) {
            keyToReturn = CLI.getCustomSortingAttribute();
        }

        return keyToReturn;
    }

    public static boolean getCustomReverse(boolean isCommandLine) {
        boolean reverse = false;

        if (isCommandLine) {
            reverse = CLI.getCustomReverse();
        }

        return reverse;
    }

    public static void runRevise(QuestionList questions, SortingKey sortingKey,
            boolean isCommandLine) {
        boolean exit = false;
        RunStats stats = new RunStats();

        do {
            //TODO implement avoidance of same question repeatedly
            questions = questions.sort(sortingKey);

            Boolean isCorrect = askQuestion(questions.getFirstQuestion(), isCommandLine);
            questions.answeredFirstQuestion(isCorrect);

            if (isCorrect) {
                stats.correct();
            } else {
                stats.incorrect();
            }

            if (Settings.isDisplayMotivationMessages()) {
                displayMotivationMessage(stats, isCommandLine);
            }

            displayQuestionStats(stats, isCommandLine);
        } while (!exit);
    }

    public static void runOrdered(QuestionList questions, SortingKey sortingKey,
            boolean isCommandLine) {
        boolean exit = false;
        RunStats stats = new RunStats();

        do {
            questions = questions.sort(sortingKey);
            ArrayList<Question> questionList = questions.getQuestions();

            for (int i = 0; i < questionList.size(); i++) {
                Boolean isCorrect = askQuestion(questionList.get(i), isCommandLine);
                questions.answeredQuestion(i, isCorrect);

                if (isCorrect) {
                    stats.correct();
                } else {
                    stats.incorrect();
                }

                if (Settings.isDisplayMotivationMessages()) {
                    displayMotivationMessage(stats, isCommandLine);
                }

                displayQuestionStats(stats, isCommandLine);
            }

            stopAskingQuestionsCheck(isCommandLine);
        } while (!exit);
    }

    public static Boolean askQuestion(Question question, boolean isCommandLine) {
        Boolean correct = false;

        if (isCommandLine) {
            return CLI.askQuestion(question);
        }

        return correct;
    }

    public static void displayQuestionStats(RunStats stats, boolean isCommandLine) {
        if (isCommandLine) {
            CLI.displayQuestionStats(stats);
        }
    }

    public static void displayMotivationMessage(RunStats stats, boolean isCommandLine) {
        if (stats.getAttempted() % Settings.getMotivationalMessagesFrequency() == 0) {
            if (isCommandLine) {
                CLI.displayMotivationalMessage();
            }
        }
    }

    public static void stopAskingQuestionsCheck(boolean isCommandLine) {
        if (isCommandLine) {
            CLI.stopAskingQuestionsCheck();
        }
    }

    public static void exitProgram(QuestionList fileQList, boolean isCommandLine) {
        if (fileQList != null) {
            boolean saveChanges = true;

            if (isCommandLine) {
                saveChanges = CLI.askSaveChanges();
            }

            if (saveChanges) {
                try {
                    FileQuestionsInterface.saveToFile(fileQList);
                } catch (IOException e) {
                    throw new RuntimeException("File location \"" + fileQList.getFileLocation() +
                            " cannot be opened, or cannot be created!");
                }
            }
        }

        displayMessage(Settings.getUponProgramExit(), isCommandLine);
        System.exit(0);
    }
}
