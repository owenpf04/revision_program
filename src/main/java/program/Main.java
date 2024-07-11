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
import java.awt.*;
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
        // instead - this is here only in case an exception is missed
        try {
            loadSettings();
            MainFrame mainFrame = new MainFrame();
        } catch (Exception e) {
            MessageDialog.displayUnexpectedErrorMessage(null,e);
            System.exit(2);
        }
    }

    private static void loadSettings() {
        String context = "trying to load the custom properties file";

        try {
            Settings.initialiseProperties();
        } catch (NullPointerException e) {
            requestUseDefaults();
            loadSettings();
        } catch (IllegalArgumentException e) {
            requestUseDefaults("We can't load your settings.", "The properties " +
                    "file contains one or more malformed Unicode escape sequences, meaning it can't " +
                    "be interpreted. This likely means there is a single backslash ('\\') " +
                    "somewhere there shouldn't be - to use a single backslash, it must be escaped " +
                    "by another backslash, so instead of '\\', '\\\\' should be used.");
            loadSettings();
        } catch (IOException | URISyntaxException e) {
            MessageDialog.displayExpectedErrorMessage(null, context, e);
            System.exit(1);
        }

        try {
            Settings.validateAllProperties();
        } catch (InvalidPropertiesException e) {
            requestReplaceInvalidFields(e);
            loadSettings();
        }
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

    private static void requestUseDefaults(String contentTitle, String description) {
        DialogOption option1 = new DialogOption("Reset", "create a new file with " +
                "the default properties in the correct location.");
        DialogOption option2 = new DialogOption("Exit", "exit the program and " +
                "try to solve the problem manually.");

        ArrayList<DialogOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        int response = OptionDialog.displayOptionDialog("Error", contentTitle,
                description, JOptionPane.ERROR_MESSAGE,
                JOptionPane.YES_NO_OPTION, options);

        if (response == JOptionPane.YES_OPTION) {
            tryUseDefaults();
        } else {
            System.exit(1);
        }
    }

    private static void requestUseDefaults() {
        requestUseDefaults("We can't find your settings.", "The app.properties " +
                "file, which should be located in the main program folder, does not exist or could " +
                "not be opened.");
    }

    private static void tryReplaceInvalidFields(InvalidPropertiesException e) {
        try {
            Settings.resetFieldsToDefaults(e.getInvalidKeys());
        } catch (IllegalArgumentException | InvalidPropertiesException | URISyntaxException |
                IOException exc) {
            MessageDialog.displayExpectedErrorMessage(null, "trying to replace " +
                            "invalid properties", exc);
            System.exit(1);
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
                MessageDialog.displayInfoMessage(null, "Defaults restored",
                        "Default settings restored.", "We successfully reset all " +
                                "settings to their default values. Click <b>" +
                                "OK</b> to load the new settings.");
            } catch (IllegalArgumentException | InvalidPropertiesException | URISyntaxException |
                    IOException e) {
                MessageDialog.displayExpectedErrorMessage(null, "trying to copy the " +
                                "default settings", e);
                System.exit(1);
            }
        }
    }
}
