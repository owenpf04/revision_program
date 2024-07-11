//package program;
//
//import program.attributes.fields.QuestionAttribute;
//import program.attributes.fields.QuestionNumericalAttribute;
//import program.comparators.StringComparator;
//import program.exceptions.InvalidQuestionAttributeException;
//import program.exceptions.InvalidQuestionFileException;
//import program.exceptions.ReturnHomeException;
//import program.helpers.ReformatString;
//import program.helpers.RunStats;
//import program.helpers.SortingKey;
//
//import java.io.FileNotFoundException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
///**
// * Class containing all of the command-line interface implementations of the methods defined in
// * {@link Main}.
// * <p>
// * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
// */
//public class CLI {
//
//    /**
//     * Returns a {@link QuestionList} consisting of {@link Question}s created from the data in a
//     * file location provided through the CLI.
//     *
//     * <p>If the file at the provided location is in any way invalid, the user will be repeatedly
//     * asked to provide a valid file location until they do so, or exit the program.
//     *
//     * @return a {@code QuestionList} of {@code Questions} read in from a file location provided by
//     *         the user through the CLI.
//     */
//    public static QuestionList getQuestionList() {
//        QuestionList questions = null;
//
//        do {
//            System.out.println("Please specify the exact location of the CSV file you'd like to " +
//                    "load from, or type \"" + Settings.getExitCommand() +
//                    "\" to exit the program:");
//            Scanner keyboard = new Scanner(System.in);
//            String fileLocation = keyboard.nextLine();
//
//            if (exitProgramCheck(fileLocation, null)) {
//                continue;
//            }
//
//            fileLocation = ReformatString.removeWhitespaceAndQuotes(fileLocation);
//
//            try {
//                questions = FileQuestionsInterface.createQuestionList(fileLocation);
//            } catch (FileNotFoundException | InvalidQuestionFileException e) {
//                System.err.println(e);
//            }
//        } while (questions == null);
//
//        return questions;
//    }
//
//    /**
//     * Returns a subset of the {@link Question}s from the provided {@link QuestionList}, filtered by
//     * the provided {@link QuestionAttribute}.
//     *
//     * <p> The user is asked, through the CLI, to specify one or more valid values for the provided
//     * {@code QuestionAttribute} by which they would like to filter.
//     *
//     * If {@code remove = false}, all {@code Question}s in the list whose corresponding value for
//     * the provided {@code QuestionAttribute} is not any of the user-specified filter values are
//     * removed, and matching {@code Question}s are kept. Otherwise, if {@code remove = true}, all
//     * matching {@code Question}s and removed, and only non-matching {@code Question}s are kept.
//     *
//     * <p> If any of the user-specified filter values are not valid values for the provided
//     * {@code QuestionAttribute} (ignoring case and removing excess whitespace and quotes - see
//     * {@link ReformatString#removeWhitespaceAndQuotes(String[])}), the user will be repeatedly
//     * asked to provide a valid set of values until they do so, or exit the program.
//     *
//     * @param questions
//     *         the {@code QuestionList} to filter.
//     * @param att
//     *         the {@code QuestionAttribute} for which the user is to provide values by which to
//     *         filter.
//     * @param remove
//     *         whether {@code Question}s in the {@code questions} list which match the
//     *         user-specified filter values should be kept, and non-matching {@code Question}s
//     *         removed ({@code remove = false}), or vice versa ({@code remove = true}).
//     *
//     * @return a subset of the provided {@code QuestionList}, filtered by the user's inputted
//     *         value(s).
//     */
//    public static QuestionList selectQuestions(QuestionList questions, QuestionAttribute att,
//            boolean remove) {
//        QuestionList filteredQuestions = questions;
//        Scanner keyboard = new Scanner(System.in);
//        boolean validResponse;
//
//        do {
//            validResponse = true;
//            System.out.println(ReformatString.toPlainText(att.toString(), false) +
//                    ":");
//            String[] response = keyboard.nextLine().split(",");
//
//            if (returnHomeCheck(response[0])) {
//                validResponse = false;
//                continue;
//            }
//
//            // If response is empty, no filter is applied
//            if (Arrays.equals(response, new String[]{""})) {
//                System.out.println(filteredQuestions.getNumQuestions() + " questions remaining");
//                break;
//            }
//
//            response = ReformatString.removeWhitespaceAndQuotes(response);
//
//            try {
//                response = QuestionAttribute.formatAttributeNameArray(response, att);
//                response = ReformatString.removeDuplicates(response);
//                List<String> responseList = Arrays.asList(response);
//                QuestionList questionSelection = filteredQuestions.selectByAttributeArray(responseList,
//                        att);
//
//                filteredQuestions = Main.updateQuestionList(filteredQuestions, questionSelection,
//                        remove);
//
//                System.out.println(filteredQuestions.getNumQuestions() + " questions remaining");
//            } catch (InvalidQuestionAttributeException e) {
//                validResponse = false;
//                System.err.println(e);
//            }
//        } while (!validResponse);
//
//        return filteredQuestions;
//    }
//
//    /**
//     * Displays the {@link Question}s in the provided {@link QuestionList} to the CL.
//     *
//     * @param selection
//     *         the {@code QuestionList} whose {@code Question}s are to be displayed.
//     * @param sortingKey
//     *         an optional {@link SortingKey} by which to sort the list of {@code Question}s before
//     *         displaying. If this is {@code null}, the list will not be sorted.
//     * @param precedingMessage
//     *         an optional message to display to the command line before displaying the
//     *         {@code Question}s in the list. Note than a newline character ("\n") will
//     *         automatically be inserted after this message if it is not blank.
//     *
//     * @see QuestionList#listQuestions(SortingKey)
//     */
//    public static void displaySelection(QuestionList selection, SortingKey sortingKey,
//            String precedingMessage) {
//        if (!precedingMessage.isBlank()) {
//            precedingMessage += "\n";
//        }
//        System.out.println(precedingMessage + selection.listQuestions(sortingKey));
//    }
//
//    /**
//     * Asks the user via the CLI if they would like to remove {@link Question}s from their list, and
//     * returns {@code true} if their answer is "Y", or {@code false} if their answer is "N"
//     * (non-case-sensitive, responses have excess whitespace removed - see
//     * {@link #getResponse(String, String[], boolean, boolean, boolean)}).
//     *
//     * <p>If the user does not respond with either "Y" or "N", they will be repeatedly asked to
//     * enter a valid response until they do so, or exit the program.
//     *
//     * @return {@code true} if the user has specified they would like to remove questions from their
//     *         {@link QuestionList}, {@code false} if they have specified they would not.
//     */
//    public static boolean doRemoveQuestions() {
//        String[] validResponses = {"Y", "N"};
//        String question =
//                "Would you like to remove any questions from your list? Type \"Y\" to begin " +
//                        "removing questions by filtering again, or type \"N\" to confirm your " +
//                        "current selection: ";
//
//        String response = getResponse(question, validResponses, true,
//                false, true);
//
//        return response.equals("Y");
//    }
//
//    /**
//     * Asks the user via the CLI which {@link RunType} they would like to perform. Returns
//     * {@link RunType#REVISE} if their response is "R", {@link RunType#TEST} if their response is
//     * "T", or {@link RunType#CUSTOM} if their response is "C" (non-case-sensitive, responses have
//     * excess whitespace removed - see
//     * {@link #getResponse(String, String[], boolean, boolean, boolean)}).
//     *
//     * <p>If the user does not respond with either "R", "T" or "C", they will be repeatedly asked
//     * to enter a valid response until they do so, or exit the program.
//     *
//     * @return the desired {@code RunType} specified by the user
//     */
//    public static RunType determineRunType() {
//        String[] validResponses = {"R", "T", "C"};
//
//        String response = getResponse(Settings.getUponSelectRunType(), validResponses, true,
//                false, true);
//
//        if (response.equals("R")) {
//            return RunType.REVISE;
//        } else if (response.equals("T")) {
//            return RunType.TEST;
//        } else {
//            return RunType.CUSTOM;
//        }
//    }
//
//    /**
//     * Asks the user via the CLI to enter a valid {@link QuestionAttribute} or
//     * {@link QuestionNumericalAttribute} by which they would like to sort a list of
//     * {@link Question}s. Returns a new {@link SortingKey} containing the specified attribute.
//     *
//     * <p>If the user does not respond with a valid {@code QuestionAttribute} or
//     * {@code QuestionNumericalAttribute} (using {@link QuestionAttribute#isAttribute(String)} or
//     * {@link QuestionNumericalAttribute#isNumericalAttribute(String)}), they will be repeatedly
//     * asked to enter a valid response until they do so, or exit the program.
//     *
//     * @return a new {@code SortingKey} containing the specified attribute.
//     */
//    public static SortingKey getCustomSortingAttribute() {
//        SortingKey keyToReturn = null;
//        Scanner keyboard = new Scanner(System.in);
//
//        do {
//            System.out.println(
//                    "Please enter a valid QuestionAttribute or QuestionNumericalAttribute " +
//                            "by which you would like to sort the questions:");
//            String response = keyboard.nextLine();
//
//            if (returnHomeCheck(response)) {
//                continue;
//            }
//
//            response = ReformatString.toScreamingSnakeCase(response.strip());
//
//            if (QuestionAttribute.isAttribute(response)) {
//                QuestionAttribute key = QuestionAttribute.valueOf(response);
//                keyToReturn = new SortingKey(key);
//            } else if (QuestionNumericalAttribute.isNumericalAttribute(response)) {
//                QuestionNumericalAttribute key = QuestionNumericalAttribute.valueOf(response);
//                keyToReturn = new SortingKey(key);
//            } else {
//                System.out.println(
//                        "\"" + response + "\" is not a valid QuestionAttribute or a valid " +
//                                "QuestionNumericalAttribute.\n");
//            }
//        } while (keyToReturn == null);
//
//        return keyToReturn;
//    }
//
//    /**
//     * Asks the user via the CLI if they would like to sort a selection of {@link Question}s in
//     * ascending or descending order, and returns {@code true} if their answer is "D" (i.e.
//     * {@code reverse = true}), or {@code false} if their answer is "A" ({@code reverse = false}).
//     * Responses are non-case-sensitive, and have excess whitespace removed - see
//     * {@link #getResponse(String, String[], boolean, boolean, boolean)}).
//     *
//     * <p>If the user does not respond with either "A" or "D", they will be repeatedly asked to
//     * enter a valid response until they do so, or exit the program.
//     *
//     * @return {@code true} if the user has specified they would like the {@link QuestionList} to be
//     *         sorted in descending order, {@code false} if they have specified they would like it
//     *         to be sorted in ascending order.
//     */
//    public static boolean getCustomReverse() {
//        String question = "Would you like the questions to be sorted in ascending (default; e.g. " +
//                "A-Z, 0-9) - \"A\" - or descending (e.g. Z-A, 9-0) - \"D\" order?";
//        String[] validResponses = {"A", "D"};
//
//        String response = getResponse(question, validResponses, true, false, true);
//
//        if (response.equals("A")) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    /**
//     * Displays to the user, via the CLI, the {@link QuestionAttribute#TITLE} of the provided
//     * {@link Question}, and expects a response either of "Y" (indicating they were able to answer
//     * the question correctly) or "N" (indicating they were not).
//     *
//     * <p>Returns {@code true} if the response received is "Y", and {@code
//     * false} if the response received is "N" (non-case-sensitive, responses have excess whitespace
//     * removed - see {@link #getResponse(String, String[], boolean, boolean, boolean)}).
//     *
//     * @param question
//     *         the {@code Question} to display to the user.
//     *
//     * @return {@code true} if the user specified they were able to answer the question correctly,
//     *         {@code false} if they specified they were not.
//     */
//    public static boolean askQuestion(Question question) {
//        String[] validResponses = {"Y", "N"};
//
//        String response = CLI.getResponse(question.getTitle(), validResponses, true,
//                false, true);
//
//        return response.equals("Y");
//    }
//
//    /**
//     * Displays a {@link RunStats} object via the CLI, using {@link RunStats#toString()}.
//     *
//     * @param stats
//     *         the {@link RunStats} object to display.
//     */
//    public static void displayQuestionStats(RunStats stats) {
//        System.out.println(stats);
//    }
//
//    /**
//     * Displays a random motivational message (see {@link Settings#getMotivationalMessage()}).
//     */
//    public static void displayMotivationalMessage() {
//        System.out.println("\n   ---   " + Settings.getMotivationalMessage() + "   ---\n");
//    }
//
//    /**
//     * Asks the user via the CLI if they would like to return home, or continue answering
//     * {@link Question}s (to be called when the user has answered all the {@code Question}s in their
//     * selection). Throws a {@link ReturnHomeException} if the user requests to return home (using
//     * the {@code homeCommand} - see {@link Settings#getHomeCommand()}), otherwise does nothing.
//     */
//    public static void stopAskingQuestionsCheck() {
//        boolean validResponse;
//        Scanner keyboard = new Scanner(System.in);
//
//        do {
//            validResponse = true;
//
//            System.out.println(Settings.getUponAllQuestionsAsked());
//            String response = keyboard.nextLine();
//
//            if (response.equals(Settings.getHomeCommand())) {
//                throw new ReturnHomeException();
//            } else if (!response.equals("continue")) {
//                validResponse = false;
//                System.out.println("Invalid response.\n");
//            }
//        } while (!validResponse);
//    }
//
//    /**
//     * Asks the user via the CLI if they would like to save their changes to the file from which the
//     * {@link Question}s were loaded, and returns {@code true} if their answer is "Y", or
//     * {@code false} if their answer is "N".
//     *
//     * <p>If the user does not respond with either "Y" or "N", they will be repeatedly asked to
//     * enter a valid response until they do so, or exit the program.
//     *
//     * @return {@code true} if the user has specified they would like to save their changes to the
//     *         file from which the {@code Question}s were loaded, {@code false} if they specified
//     *         they would not like to save their changes.
//     */
//    public static boolean askSaveChanges() {
//        String[] validResponses = {"Y", "N"};
//        String question = "Would you like to save any changes you might have made? " +
//                "Type \"Y\" to save any changes, or \"N\" not to " +
//                "(case-sensitive):";
//
//        String response = getResponse(question, validResponses, false,
//                false, false);
//
//        return response.equals("Y");
//    }
//
//    /**
//     * General method for asking the user the provided {@code question} via the CLI, and formatting
//     * their response as specified by the parameters {@code ignoreCase},
//     * {@code stripAndRemoveQuotes} and {@code strip}. This formatted response is then checked
//     * against the provided array of valid responses, and if it matches any of the provided valid
//     * responses, it is returned.
//     *
//     * <p>If the user's response does not match any of the provided responses, they will be told
//     * their response was invalid, and will be repeatedly asked to provide a valid response to the
//     * question until they do so, or exit the program.
//     *
//     * <p>Note that the {@code question} parameter refers to a general (String) question, such as
//     * "Would you like to filter your selection? Y/N", and <b>not</b> a {@link Question} object.
//     *
//     * @param question
//     *         the question to ask the user.
//     * @param validResponses
//     *         an exhaustive array of the possible valid responses from the user. The user's
//     *         response will not be returned until it matches one of the values in this array.
//     * @param ignoreCase
//     *         whether or not the method should ignore case when checking if the user's response
//     *         matches any of the values in the {@code validResponses} array.
//     * @param stripAndRemoveQuotes
//     *         whether or not the user's response should be reformatted using
//     *         {@link ReformatString#removeWhitespaceAndQuotes(String)} before being checked against
//     *         the {@code validResponses} array for matches. Note that if this is {@code true}, the
//     *         value of the {@code strip} parameter is ignored, as the user's response will be
//     *         stripped of excess whitespace regardless of the value of {@code strip}.
//     * @param strip
//     *         whether or not the user's response should be stripped of excess whitespace before
//     *         being checked against the {@code validResponses} array for matches.
//     *
//     * @return a value in the {@code validResponses} array specified by the user.
//     */
//    public static String getResponse(String question, String[] validResponses,
//            boolean ignoreCase, boolean stripAndRemoveQuotes, boolean strip) {
//        boolean validResponse;
//        String response;
//        StringComparator comparator = new StringComparator(ignoreCase);
//        Scanner keyboard = new Scanner(System.in);
//
//        do {
//            validResponse = false;
//
//            System.out.println(question);
//            response = keyboard.nextLine();
//
//            if (returnHomeCheck(response)) {
//                continue;
//            }
//
//            if (stripAndRemoveQuotes) {
//                response = ReformatString.removeWhitespaceAndQuotes(response);
//            } else if (strip) {
//                response = response.strip();
//            }
//
//            for (String elem : validResponses) {
//                if (comparator.compare(response, elem) == 0) {
//                    response = elem;
//                    validResponse = true;
//                    break;
//                }
//            }
//
//            if (!validResponse) {
//                System.out.println("Invalid response.\n");
//            }
//        } while (!validResponse);
//
//        return response;
//    }
//
//    /**
//     * Checks if the provided string exactly matches the {@code homeCommand} specified in
//     * {@link Settings} - see {@link Settings#getHomeCommand()}.
//     *
//     * <p>If it does, the user will be asked to confirm returning home by entering the
//     * {@code homeCommand} again. If it exactly matches again, a {@code ReturnHomeException} will be
//     * thrown; otherwise, the method will return {@code true}, indicating that the provided string
//     * is indeed the {@code homeCommand}, but that the user did not confirm the exit. This is
//     * intended to lead to whatever instruction was most recently presented to the user in the
//     * calling method being repeated.
//     *
//     * <p>If the provided string does not match the {@code homeCommand}, the method will return
//     * {@code false}, indicating that the user didn't request to return home originally. This is
//     * intended to have no effect in the calling method.
//     *
//     * @param stringToCheck
//     *         the string to check against the {@code homeCommand}.
//     *
//     * @return {@code true} if the provided string matches the {@code homeCommand}, but the user did
//     *         not confirm the exit, {@code false} if the provided string does not match the
//     *         {@code homeCommand}.
//     *
//     * @throws ReturnHomeException
//     *         if the provided string matches the {@code homeCommand}, and the user confirmed the
//     *         exit by repeating this {@code homeCommand}.
//     */
//    public static boolean returnHomeCheck(String stringToCheck) throws ReturnHomeException {
//        if (stringToCheck.equals(Settings.getHomeCommand())) {
//            System.out.println("\n" + Settings.getUponHomeRequest());
//            Scanner keyboard = new Scanner(System.in);
//            if (keyboard.nextLine().equals(Settings.getHomeCommand())) {
//                throw new ReturnHomeException();
//            } else {
//                System.out.println("Cancelling return home and continuing where you were...");
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Checks if the provided string exactly matches the {@code exitCommand} specified in
//     * {@link Settings} - see {@link Settings#getExitCommand()}.
//     *
//     * <p>If it does, the user will be asked to confirm exiting the program by entering the
//     * {@code exitCommand} again. If it exactly matches again,
//     * {@link Main#exitProgram(QuestionList, boolean)} will be called to exit the program, with the
//     * provided {@link QuestionList} passed as the {@code fileQList} parameter; otherwise, the
//     * method will return {@code true}, indicating that the provided string is indeed the
//     * {@code exitCommand}, but that the user did not confirm the exit. This is intended to lead to
//     * whatever instruction was most recently presented to the user in the calling method being
//     * repeated.
//     *
//     * <p>If the provided string does not match the {@code exitCommand}, the method will return
//     * {@code false}, indicating that the user didn't request to return home originally. This is
//     * intended to have no effect in the calling method.
//     *
//     * <p>This method is very similar to {@link #returnHomeCheck(String)}, but is intended to be
//     * called only from the home page, while {@code returnHomeCheck} is to be called from all points
//     * in the program except the home page.
//     *
//     * @param stringToCheck
//     *         the string to check against the {@code exitCommand}.
//     * @param fileQuestions
//     *         the {@code QuestionList} that has been altered in this run of the program, to be
//     *         passed to the {@code Main.exitProgram(QuestionList, boolean)} method if the user
//     *         confirms the program exit.
//     *
//     * @return {@code true} if the provided string matches the {@code exitCommand}, but the user did
//     *         not confirm the exit, {@code false} if the provided string does not match the
//     *         {@code exitCommand}.
//     */
//    public static boolean exitProgramCheck(String stringToCheck, QuestionList fileQuestions) {
//        boolean returnValue = false;
//
//        if (stringToCheck.equals(Settings.getExitCommand())) {
//            System.out.println("\n" + Settings.getUponExitRequest());
//            Scanner keyboard = new Scanner(System.in);
//
//            if (keyboard.nextLine().equals(Settings.getExitCommand())) {
//                Main.exitProgram(fileQuestions, true);
//            } else {
//                System.out.println("Cancelling exit and continuing where you were...");
//                returnValue = true;
//            }
//        }
//
//        return returnValue;
//    }
//}
