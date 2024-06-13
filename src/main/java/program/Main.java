package program;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import program.GUI.MainFrame;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;
import program.exceptions.InvalidQuestionFileException;
import program.exceptions.ReturnHomeException;
import program.helpers.ReformatString;
import program.helpers.RunStats;
import program.helpers.SortingKey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;


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
        FlatJetBrainsMonoFont.install();
        FlatLaf.setPreferredMonospacedFontFamily(FlatJetBrainsMonoFont.FAMILY);
        FlatLaf.registerCustomDefaultsSource( "FlatLafThemes");
        FlatSolarizedLightIJTheme.setup();

        // Exception catching here is a fallback, it should be done in each of the individual methods
        // first
        try {
            Settings settings = loadSettings();

            MainFrame mainFrame = new MainFrame(settings);
        } catch (Exception e) {
            displayUncaughtExceptionDialog(e, "Not specified");
            System.exit(2);
        }
    }

    private static Settings loadSettings() {
        boolean resetSettingsToDefault = false;
        try {
            return new Settings(false);
        } catch (IllegalArgumentException e) {
            //TODO change this to allow only resetting particular fields, rather than entire file
            String description = "The contents of app.properties are invalid, and therefore " +
                    "settings could not be properly loaded.";
            resetSettingsToDefault = requestUseDefaults(description, e.getMessage());
        } catch (NullPointerException e) {
            String description = "The app.properties file, which should be located in the main " +
                    "program folder, does not exist or could not be opened.";
            resetSettingsToDefault = requestUseDefaults(description);
        } catch (IOException e) {
            String description = "Trying to load app.properties - an error occurred while reading " +
                    "from the provided InputStream (see Settings.loadCustomProperties()).";
            displayUncaughtExceptionDialog(e, description);
            System.exit(1);
        } catch (URISyntaxException e) {
            String description = "Trying to load app.properties - the URL returned by" +
                    "Settings.class.getProtectionDomain().getCodeSource().getLocation() is not " +
                    "formatted strictly according to RFC2396 and cannot be converted to a URI.";
            displayUncaughtExceptionDialog(e, description);
            System.exit(1);
        }

        if (resetSettingsToDefault) {
            Settings settings = null;

            try {
                Settings.copyDefaultSettings();
            } catch (Exception e) {
                String description = "Trying to copy default settings to app.properties";
                displayUncaughtExceptionDialog(e, description);
                System.exit(1);
            }

            return loadSettings();
        } else {
            System.exit(1);
            return null;
        }
    }

    private static boolean requestUseDefaults(String message, String details) {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel mainLabel = new JLabel("An error occurred while trying to load app.properties " +
                "(the settings file).");
        mainLabel.setBorder(new EmptyBorder(0,0,20,0));
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JLabel descriptionLabel = new JLabel("Description: " + message);
        descriptionLabel.setBorder(new EmptyBorder(0,0,20,0));

        if (details != null && !details.isBlank()) {
            JLabel detailsLabel = new JLabel("Details: " + details);
            detailsLabel.setBorder(new EmptyBorder(0,0,20,0));
            descriptionPanel.add(detailsLabel, BorderLayout.CENTER);
        }

        descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);

        JLabel buttonDescriptionsLabel = new JLabel("<html>Click \"Yes\" to create a new " +
                "app.properties file in the correct location (or overwrite the existing invalid " +
                "one - this will delete any saved<br>settings, but not any of your question files), " +
                "or click \"No\" to exit the program.</html>");

        mainPanel.add(mainLabel, BorderLayout.NORTH);
        mainPanel.add(descriptionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonDescriptionsLabel, BorderLayout.SOUTH);

        int response = JOptionPane.showConfirmDialog(null, mainPanel,
                "Error - reset to default settings?", JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean requestUseDefaults(String message) {
        return requestUseDefaults(message, null);
    }

    public static void displayUncaughtExceptionDialog(Exception e, String description) {
        //TODO update contact details
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel mainLabel = new JLabel(("An unexpected error has occurred. Details:"));
        mainLabel.setBorder(new EmptyBorder(0,0,20,0));

        String detailsMessage = "Exception description: " + ReformatString.wrapString(description, 60, 23) +
                "\nCause:                 " + ReformatString.wrapString(String.valueOf(e), 60, 23) +
                "\nStacktrace:            " + ReformatString.wrapString(Arrays.toString(e.getStackTrace()), 60, 23);
        JTextArea details = new JTextArea(detailsMessage);
        details.setEditable(false);
        details.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12));
        details.setBorder(new EmptyBorder(0,0,20,0));

        String finalMessage = "Please send me an email at owenpf@outlook.com with these details. " +
                "The program will now exit.";
        JLabel finalLabel = new JLabel(finalMessage);

        mainPanel.add(mainLabel, BorderLayout.NORTH);
        mainPanel.add(details, BorderLayout.CENTER);
        mainPanel.add(finalLabel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null, mainPanel, "Error - uncaught exception",
                JOptionPane.ERROR_MESSAGE);

    }
//    public static void main(String[] args) {
//        MainFrame.main(args);
//        boolean isCommandLine = true;
//
//        QuestionList questionsFromFile = null;
//
//        // Loading file initially
//        try {
//            questionsFromFile = loadFile(args, isCommandLine);
//        } catch (ReturnHomeException e) {
//            exitProgram(questionsFromFile, isCommandLine);
//        }
//
//        boolean exit = false;
//        do {
//            String homePromptResponse = null;
//
//            try {
//                homePromptResponse = homePrompt(isCommandLine);
//            } catch (ReturnHomeException e) {
//                continue;
//            }
//
//            if (homePromptResponse.equals("select")) {
//                try {
//                    QuestionList questionSelection = selectQuestions(questionsFromFile,
//                            isCommandLine);
//                    RunType runType = determineRunType(isCommandLine);
//                    run(questionSelection, runType, isCommandLine);
//                } catch (ReturnHomeException ignored) {
//                }
//            } else if (homePromptResponse.equals("list")) {
//                try {
//                    QuestionList questionSelection = selectQuestions(questionsFromFile,
//                            isCommandLine);
//                    SortingKey sortingKey = getSortingKey(RunType.CUSTOM, isCommandLine);
//                    displaySelection(questionSelection, sortingKey, "Your sorted selection:",
//                            isCommandLine);
//                } catch (ReturnHomeException ignored) {
//                }
//            } else {
//                exitProgram(questionsFromFile, isCommandLine);
//            }
//        } while (!exit);
//    }
//
//    /**
//     * Returns a new {@link QuestionList} created from the contents of a .csv file. The location of
//     * this file is either obtained from the command-line arguments, if there are any, or from the
//     * user directly if not.
//     *
//     * @param args
//     *         command-line arguments.
//     * @param isCommandLine
//     *         whether interactions with the user are to be via the command line
//     *         {@code isCommandLine = true}, or via the GUI {@code isCommandLine = false}.
//     *
//     * @return a new {@code QuestionList} created from the contents of a .csv file.
//     *
//     * @throws IllegalArgumentException
//     *         if a file location is provided by the CL args and the file at this location is
//     *         non-existent or in any way invalid.
//     */
//    public static QuestionList loadFile(String[] args, boolean isCommandLine) throws
//            IllegalArgumentException {
//        QuestionList questions = null;
//
//        if (args.length > 1) {
//            throw new IllegalArgumentException("Expected maximum 1 command line argument, got " +
//                    args.length + "!");
//        } else if (args.length == 1) {
//            String fileLocation = ReformatString.removeWhitespaceAndQuotes(args[0]);
//
//            try {
//                questions = FileQuestionsInterface.createQuestionList(fileLocation);
//                } catch (FileNotFoundException | InvalidQuestionFileException e) {
//                throw new IllegalArgumentException("File at location \"" + fileLocation +
//                        "\" (provided by command-line " +
//                        "arguments) invalid! Details:\n" +
//                        e.getMessage());
//            }
//        } else {
//            if (isCommandLine) {
//                questions = CLI.getQuestionList();
//            }
//        }
//        return questions;
//    }
//
//    /**
//     * Displays the home page message (see {@link Settings#getUponHomePage()})
//     *
//     * @param isCommandLine
//     *
//     * @return
//     */
//    public static String homePrompt(boolean isCommandLine) {
//        String response = null;
//
//        if (isCommandLine) {
//            String[] validResponses = {"select", "list", Settings.getExitCommand()};
//            response = CLI.getResponse(Settings.getUponHomePage(), validResponses, false,
//                    false, false);
//        }
//
//        return response;
//    }
//
//    //TODO javaDoc only talks about questionAttributes, not numericalQuestionAttributes
//
//    /**
//     * Obtains and returns a filtered subset of the provided QuestionList parameter. Asks the user
//     * to input desired values for each of the QuestionAttributes of the Question, and returns a
//     * QuestionList containing only Questions which satisfied every set of conditions supplied by
//     * the user
//     *
//     * @param questionsFromFile
//     *         the QuestionList to filter
//     *
//     * @return a subset of the QuestionList provided, filtered as per the conditions supplied by the
//     *         user
//     */
//    public static QuestionList selectQuestions(QuestionList questionsFromFile,
//            boolean isCommandLine) {
//        QuestionList filteredQuestions = questionsFromFile;
//
//        displayMessage(Settings.getUponFilter(), isCommandLine);
//
//        for (QuestionAttribute att : QuestionAttribute.valuesReversed()) {
//            if (isCommandLine) {
//                filteredQuestions = CLI.selectQuestions(filteredQuestions, att, false);
//            }
//        }
//
//        displaySelection(filteredQuestions, null, "Your current selection:",
//                isCommandLine);
//
//        boolean doRemove = doRemoveQuestions(isCommandLine);
//        if (doRemove) {
//            displayMessage(Settings.getUponFilterRemove(), isCommandLine);
//
//            for (QuestionAttribute att : QuestionAttribute.valuesReversed()) {
//                if (isCommandLine) {
//                    filteredQuestions = CLI.selectQuestions(filteredQuestions, att, true);
//                }
//            }
//
//            displaySelection(filteredQuestions, null, "Your final selection:",
//                    isCommandLine);
//        }
//
//        return filteredQuestions;
//    }
//
//    public static QuestionList updateQuestionList(QuestionList originalList,
//            QuestionList selectedList,
//            boolean remove) {
//        if (remove) {
//            ArrayList<Question> updatedArray = originalList.getQuestions();
//
//            updatedArray.removeAll(selectedList.getQuestions());
//            return new QuestionList(updatedArray, originalList.getFileLocation());
//        } else {
//            return selectedList;
//        }
//    }
//
//    public static void displayMessage(String message, boolean isCommandLine) {
//        if (isCommandLine) {
//            System.out.println(message);
//        }
//    }
//
//    public static boolean doRemoveQuestions(boolean isCommandLine) {
//        boolean removeQuestions = true;
//
//        if (isCommandLine) {
//            removeQuestions = CLI.doRemoveQuestions();
//        }
//
//        return removeQuestions;
//    }
//
//    public static void displaySelection(QuestionList selection, SortingKey sortingKey,
//            String precedingMessage, boolean isCommandLine) {
//        if (isCommandLine) {
//            CLI.displaySelection(selection, sortingKey, precedingMessage);
//        }
//    }
//
//    /**
//     * Returns a RunType as specified by user input
//     *
//     * @return RunType specified by user input
//     */
//    public static RunType determineRunType(boolean isCommandLine) {
//        RunType runType = null;
//
//        if (isCommandLine) {
//            runType = CLI.determineRunType();
//        }
//
//        return runType;
//    }
//
//    public static void run(QuestionList questions, RunType runType, boolean isCommandLine) {
//        SortingKey sortingKey = getSortingKey(runType, isCommandLine);
//
//        displayMessage(Settings.getUponRun(), isCommandLine);
//
//        if (runType.equals(RunType.REVISE)) {
//            runRevise(questions, sortingKey, isCommandLine);
//        } else {
//            runOrdered(questions, sortingKey, isCommandLine);
//        }
//    }
//
//    public static SortingKey getSortingKey(RunType runType, boolean isCommandLine) {
//        SortingKey sortingKey;
//
//        if (runType.equals(RunType.REVISE)) {
//            sortingKey = new SortingKey(QuestionNumericalAttribute.LIKELIHOOD, true);
//        } else if (runType.equals(RunType.TEST)) {
//            sortingKey = new SortingKey(QuestionNumericalAttribute.INDEX, false);
//        } else {
//            sortingKey = getCustomSortingAttribute(isCommandLine);
//
//            boolean reverse = getCustomReverse(isCommandLine);
//            sortingKey.setReverse(reverse);
//        }
//        return sortingKey;
//    }
//
//    public static SortingKey getCustomSortingAttribute(boolean isCommandLine) {
//        SortingKey keyToReturn = null;
//
//        if (isCommandLine) {
//            keyToReturn = CLI.getCustomSortingAttribute();
//        }
//
//        return keyToReturn;
//    }
//
//    public static boolean getCustomReverse(boolean isCommandLine) {
//        boolean reverse = false;
//
//        if (isCommandLine) {
//            reverse = CLI.getCustomReverse();
//        }
//
//        return reverse;
//    }
//
//    public static void runRevise(QuestionList questions, SortingKey sortingKey,
//            boolean isCommandLine) {
//        boolean exit = false;
//        RunStats stats = new RunStats();
//
//        do {
//            //TODO implement avoidance of same question repeatedly
//            questions = questions.sort(sortingKey);
//
//            Boolean isCorrect = askQuestion(questions.getFirstQuestion(), isCommandLine);
//            questions.answeredFirstQuestion(isCorrect);
//
//            if (isCorrect) {
//                stats.correct();
//            } else {
//                stats.incorrect();
//            }
//
//            if (Settings.isDisplayMotivationMessages()) {
//                displayMotivationMessage(stats, isCommandLine);
//            }
//
//            displayQuestionStats(stats, isCommandLine);
//        } while (!exit);
//    }
//
//    public static void runOrdered(QuestionList questions, SortingKey sortingKey,
//            boolean isCommandLine) {
//        boolean exit = false;
//        RunStats stats = new RunStats();
//
//        do {
//            questions = questions.sort(sortingKey);
//            ArrayList<Question> questionList = questions.getQuestions();
//
//            for (int i = 0; i < questionList.size(); i++) {
//                Boolean isCorrect = askQuestion(questionList.get(i), isCommandLine);
//                questions.answeredQuestion(i, isCorrect);
//
//                if (isCorrect) {
//                    stats.correct();
//                } else {
//                    stats.incorrect();
//                }
//
//                if (Settings.isDisplayMotivationMessages()) {
//                    displayMotivationMessage(stats, isCommandLine);
//                }
//
//                displayQuestionStats(stats, isCommandLine);
//            }
//
//            stopAskingQuestionsCheck(isCommandLine);
//        } while (!exit);
//    }
//
//    public static Boolean askQuestion(Question question, boolean isCommandLine) {
//        Boolean correct = false;
//
//        if (isCommandLine) {
//            return CLI.askQuestion(question);
//        }
//
//        return correct;
//    }
//
//    public static void displayQuestionStats(RunStats stats, boolean isCommandLine) {
//        if (isCommandLine) {
//            CLI.displayQuestionStats(stats);
//        }
//    }
//
//    public static void displayMotivationMessage(RunStats stats, boolean isCommandLine) {
//        if (stats.getAttempted() % Settings.getMotivationalMessagesFrequency() == 0) {
//            if (isCommandLine) {
//                CLI.displayMotivationalMessage();
//            }
//        }
//    }
//
//    public static void stopAskingQuestionsCheck(boolean isCommandLine) {
//        if (isCommandLine) {
//            CLI.stopAskingQuestionsCheck();
//        }
//    }
//
//    public static void exitProgram(QuestionList fileQList, boolean isCommandLine) {
//        if (fileQList != null) {
//            boolean saveChanges = true;
//
//            if (isCommandLine) {
//                saveChanges = CLI.askSaveChanges();
//            }
//
//            if (saveChanges) {
//                try {
//                    FileQuestionsInterface.saveToFile(fileQList);
//                } catch (IOException e) {
//                    throw new RuntimeException("File location \"" + fileQList.getFileLocation() +
//                            " cannot be opened, or cannot be created!");
//                }
//            }
//        }
//
//        displayMessage(Settings.getUponProgramExit(), isCommandLine);
//        System.exit(0);
//    }
}
