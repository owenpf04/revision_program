package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class Settings {
    // FILE-READ SETTINGS START

    private static String questionsFileLocation = "";
    // cannot be equal to minus one as would be dividing by 0
    private static double percentageWeighting = 1;
    // percentage and absolute offsets cannot both be 0 (divisor is (percentage * expected asks)
    // + absolute
    private static double timesAskedPercentageOffset = 25;
    private static double timesAskedAbsoluteOffset = 2;

    //FILE-READ SETTINGS END

    //TODO these should all be screaming snake case
    private final static String exitCommand = "exit program";
    private final static String homeCommand = "return home";

    private final static String uponHomePage = "Revision program\n" +
            "Copyright (c) Owen Parfitt-Ford 2024. All" +
            " rights reserved.\n\n" +
            "Type \"select\" to select a subset of the" +
            " questions loaded from the provided file," +
            " type " +
            "\"list\" to view your loaded list of " +
            "questions, and sort by a desired " +
            "attribute, or " +
            "type \"" + exitCommand +
            "\" to exit the program (you will be able " +
            "to save any changes " +
            "you might have made to the file). At any " +
            "time, type \"" +
            homeCommand + "\" to return " +
            "to this home page:";
    private final static String uponFilter =
            "Please enter values for the following filters below " +
                    "(separate multiple values with commas; e.g. to only choose questions " +
                    "relating to " +
                    "Edexcel or AQA subjects, you would enter \"Edexcel, AQA\")\n" +
                    "If you do not wish to filter by a particular field, just press enter";
    private final static String uponFilterRemove =
            "For the next set of filters, please enter values " +
                    "for which you would like to remove the corresponding questions in your " +
                    "current selection:";
    private final static String uponSelectRunType = "Please enter one of the following values to " +
            "select your desired operation " +
            "mode:\n" +
            "1. Type \"R\" to revise questions in" +
            " order, based on which questions the" +
            " program " +
            "determines are most important (see " +
            "\"Likelihood\" in the provided .csv " +
            "file)\n" +
            "2. Type \"T\" to simply test " +
            "yourself by answering the questions " +
            "selected in the order " +
            "specified in the provided file\n" +
            "3. Type \"C\" to define a custom way" +
            " in which to sort and display the " +
            "questions, for " +
            "instance by title, or by number of " +
            "times answered correctly";
    private final static String uponRun =
            "For each question, type \"Y\" if you were able to answer " +
                    "the question correctly, or \"N\" to indicate you were not";

    private final static String uponAllQuestionsAsked =
            "You have answered every question in your " +
                    "selection. Would you like to go back to the start of your selection, and " +
                    "answer them " +
                    "again - type \"continue\", or return to the home page - type \"" +
                    homeCommand +
                    "\" (case-sensitive)?";

    private final static String uponHomeRequest = "Are you sure you would like to return to the " +
            "home page? Type \"" + homeCommand +
            "\" again to confirm returning home, or" +
            " type anything " +
            "else to continue where you were:";
    private final static String uponExitRequest =
            "Are you sure you would like to exit the program? " +
                    "Type \"" + exitCommand +
                    "\" again to confirm exit (if you have made any changes to the " +
                    "file contents, you will be able to choose if you save them or not), or type " +
                    "anything " +
                    "else to continue where you were:";
    private final static String uponProgramExit = "Exiting revision program...";

    //TODO remove motivational messages
    private static boolean displayMotivationMessages = true;
    private static String[] motivationalMessages = {"Izzy dawg, keep going you sweetie pook",
            "I know it's hard, but sometimes hard is good, so keep going",
            "Don't quiver at a fail, peak at a success",
            "Stars, hide your fires, let not light see my dark and deep desires",
            "Clap your hands 10 times",
            "You're doing so well",
            "Don't worry if it doesn't go to plan - there will be a reason for it",
            "Sometimes life gives you lemons, but in this case, it was a wrong answer",
            "Don't look back in anger, look forward in forgiveness",
            "Blink 4 times",
            "Congratulations, have a carrot",
            "Don't be a loser, keep going",
            "If you get the next question right, give yourself a belly rub",
            "Be like Shakira",
            "Take a 5 minute break (joking don't)",
            "Picture a lovely milkshake... keep going",
            "There's light at the end of the tunnel (if you succeed)",
            "Don't let them stop you from becoming your most amazing self",
            "Owieeeee",
            "If you don't keep going, you will be looked at as a failed individual"};
    private static int motivationalMessagesFrequency = 4;

    public static String getExitCommand() {
        return exitCommand;
    }

    public static String getHomeCommand() {
        return homeCommand;
    }

    public static String getUponHomePage() {
        return uponHomePage;
    }

    public static String getUponFilter() {
        return uponFilter;
    }

    public static String getUponFilterRemove() {
        return uponFilterRemove;
    }

    public static String getUponSelectRunType() {
        return uponSelectRunType;
    }

    public static String getUponRun() {
        return uponRun;
    }

    public static String getUponAllQuestionsAsked() {
        return uponAllQuestionsAsked;
    }

    public static String getUponHomeRequest() {
        return uponHomeRequest;
    }

    public static String getUponExitRequest() {
        return uponExitRequest;
    }

    public static String getUponProgramExit() {
        return uponProgramExit;
    }

    public static boolean isDisplayMotivationMessages() {
        return displayMotivationMessages;
    }

    public static String getMotivationalMessage() {
        Random random = new Random();
        return motivationalMessages[random.nextInt(motivationalMessages.length)];
    }

    public static int getMotivationalMessagesFrequency() {
        return motivationalMessagesFrequency;
    }

    public static double getPercentageWeighting() {
        return percentageWeighting;
    }

    public static double getTimesAskedPercentageOffset() {
        return timesAskedPercentageOffset;
    }

    public static double getTimesAskedAbsoluteOffset() {
        return timesAskedAbsoluteOffset;
    }

    public static void loadFromFile() {
        Scanner file = null;
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                .getLocation().getPath();
        String settingsLocation = settingsDirectory + "settings.txt";
        try {
            file = new Scanner(new File(settingsLocation));
            parseFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("No settings file found - new settings file with default values " +
                    "will be created.");
            InputStream stream = Settings.class.getClassLoader().getResourceAsStream(
                    "default_settings.txt");
            file = new Scanner(Objects.requireNonNull(stream,
                    "File \"default_settings.txt\" could not be found in resources!"));
            parseFile(file);
        } catch (IllegalArgumentException e) {
            //provided settings file is invalid - use default instead
        }
    }

    private static void parseFile(Scanner file) throws IllegalArgumentException {
        boolean isComment = false;
        int lineNumber = 0;
        while (file.hasNext()) {
            String line = file.nextLine();
            lineNumber++;

            if (line.equals("/*")) {
                isComment = true;
            } else if (line.equals("*/")) {
                isComment = false;
            }

            if (!isComment && !line.isBlank()) {
                int equalsIndex = line.indexOf("=");
                //Line is intended to be parsed
                if (equalsIndex < 0) {
                    throw new IllegalArgumentException("Line #" + lineNumber + " - \"" + line +
                            "\" - is invalid: ");
                }
                // lots of if statements for each possible assignment
            }
        }
    }
}
