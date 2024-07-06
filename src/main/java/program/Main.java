package program;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import program.GUI.dialogs.DialogOption;
import program.GUI.dialogs.MessageDialog;
import program.GUI.MainFrame;
import program.GUI.dialogs.OptionDialog;
import program.exceptions.InvalidPropertiesException;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;


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
        FlatLaf.registerCustomDefaultsSource("FlatLafThemes");
        FlatSolarizedLightIJTheme.setup();

        // Exception catching here is a fallback, it should be done in each of the individual methods
        // first
        try {
            loadSettings();
            MainFrame mainFrame = new MainFrame();
        } catch (Exception e) {
            MessageDialog.displayUnexpectedErrorMessage(e);
            System.exit(2);
        }
    }

    private static void loadSettings() {
        String context = "trying to load the custom properties file";
//        String instructions = "This is a low-level error which I didn't expect to ever occur in " +
//                "practice, so I can't really offer any advice on how to fix it, other than to " +
//                "try again.";
        Properties properties = null;
        try {
            Settings.initialiseProperties();
        } catch (NullPointerException e) {
            requestUseDefaults();
            loadSettings();
        } catch (IOException e) {
//            String description = "An error occurred while reading " +
//                    "from the provided InputStream (see Settings.loadCustomProperties()).";
            MessageDialog.displayExpectedErrorMessage(context, e);
            System.exit(1);
        } catch (URISyntaxException e) {
//            String description = "The URL returned by " +
//                    "Settings.class.getProtectionDomain().getCodeSource().getLocation() is not " +
//                    "formatted strictly according to RFC2396 and cannot be converted to a URI.";
            MessageDialog.displayExpectedErrorMessage(context, e);
            System.exit(1);
        }

        try {
            Settings.validateAllProperties();
        } catch (InvalidPropertiesException e) {
            //TODO change this to allow only resetting particular fields, rather than entire file
            String description = "The contents of app.properties are invalid, and therefore " +
                    "settings could not be properly loaded (" + e.getMessage() + ").";
            requestReplaceInvalidFields(e);
            loadSettings();
        }
//
//        try {
//            return new Settings(false);
//        } catch (InvalidPropertiesException e) {
//            //TODO change this to allow only resetting particular fields, rather than entire file
//            String description = "The contents of app.properties are invalid, and therefore " +
//                    "settings could not be properly loaded (" + e.getMessage() + ").";
//            requestReplaceInvalidFields(e);
//            return loadSettings();
////            resetSettingsToDefault = requestUseDefaults(description);
//        } catch (NullPointerException e) {
//            requestUseDefaults();
//            return loadSettings();
//        } catch (IOException e) {
//            String description = "An error occurred while reading " +
//                    "from the provided InputStream (see Settings.loadCustomProperties()).";
//            MessageDialog.displaySpecificErrorMessage(context, e, description,
//                    instructions);
//            System.exit(1);
//        } catch (URISyntaxException e) {
//            String description = "The URL returned by " +
//                    "Settings.class.getProtectionDomain().getCodeSource().getLocation() is not " +
//                    "formatted strictly according to RFC2396 and cannot be converted to a URI.";
//            MessageDialog.displaySpecificErrorMessage(context, e, description,
//                    instructions);
//            System.exit(1);
//        }
    }

    private static void requestReplaceInvalidFields(InvalidPropertiesException e) {
        DialogOption option1 = new DialogOption("Replace", "replace the invalid " +
                "value(s) with their respective defaults, keeping other values the same.");
        DialogOption option2 = new DialogOption("Reset", "restore all properties " +
                "to their default values.");
        DialogOption option3 = new DialogOption("Exit", "exit the program and " +
                "try to solve the problem manually.");

        ArrayList<DialogOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        int response = OptionDialog.displayOptionDialog("Error",
                "We can't load your settings.", e,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
                options);

        if (response == JOptionPane.YES_OPTION) {
            tryReplaceInvalidFields(e);
        } else if (response == JOptionPane.NO_OPTION) {
            tryUseDefaults();
        } else {
            System.exit(1);
        }
    }

    private static void requestUseDefaults() {
        String description = "The app.properties file, which should be located in the main " +
                "program folder, does not exist or could not be opened.";

        DialogOption option1 = new DialogOption("Reset", "create a new file with " +
                "the default properties in the correct location.");
        DialogOption option2 = new DialogOption("Exit", "exit the program and " +
                "try to solve the problem manually.");

        ArrayList<DialogOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        int response = OptionDialog.displayOptionDialog("Error",
                "We can't find your settings.", description, JOptionPane.ERROR_MESSAGE,
                JOptionPane.YES_NO_OPTION, options);

        if (response == JOptionPane.YES_OPTION) {
            tryUseDefaults();
        } else {
            System.exit(1);
        }
    }

    private static void tryReplaceInvalidFields(InvalidPropertiesException e) {
        try {
            Settings.resetFieldsToDefaults(e.getInvalidKeys());
        } catch (Exception exc) {
//            MessageDialog.displayExpectedErrorMessage("trying to replace invalid properties",
//                    exc);
//            System.exit(1);
        }
    }

    private static void tryUseDefaults() {
        DialogOption option1 = new DialogOption("Reset", "create a new file with " +
                "the default properties in the correct location.");
        DialogOption option2 = new DialogOption("Cancel", "cancel.");

        ArrayList<DialogOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        int response = OptionDialog.displayOptionDialog("Confirmation", "Are you sure?",
                "Are you sure you want to reset ALL properties to their defaults? This will " +
                        "reset all settings and statistics, as well as knowledge of your recently " +
                        "opened files. This will NOT delete any question files you may have.",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, options);

        if (response == JOptionPane.YES_OPTION) {
            try {
                Settings.resetAllFieldsToDefaults();
                MessageDialog.displayInfoMessage("Defaults restored",
                        "Default settings restored.", "We successfully reset all " +
                                "settings to their default values. Click <b>" +
                                "OK</b> to load the new settings.");
            } catch (Exception e) {
                //            ,"The default app.properties file could not be copied to the " +
                //                    "correct location", "This is a low-level error which I " +
                //                    "didn't expect to ever occur in practice, so I can't really offer " +
                //                    "any advice on how to fix it, other than to try again."
                MessageDialog.displayExpectedErrorMessage("trying to copy the default settings",
                        e);
                System.exit(1);
            }
        }
    }
}
