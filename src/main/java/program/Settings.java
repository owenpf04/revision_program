package program;

import program.exceptions.InvalidQuestionFileException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class Settings {
    private enum OS {
        WINDOWS, MAC, LINUX, UNKNOWN;
    }

    private static final OS os = determineOS();

    private static final String DEFAULT_SETTINGS_NAME = "app_default.properties";
    private static final String CUSTOM_SETTINGS_NAME = "app.properties";

    private String userName;

    private Set<QuestionFile> recentFiles;

    private String defaultFileOpeningDirectory;

    // cannot be equal to minus one as would be dividing by 0
    private double percentageWeighting;

    // percentage and absolute offsets cannot both be 0 (divisor is (percentage * expected asks)
    // + absolute
    private double timesAskedPercentageOffset;
    private double timesAskedAbsoluteOffset;

    /**
     * Creates a new Settings object from the key-value pairs specified in a .properties file. If
     * useDefaults is false, the method will search for a file "app.properties" in the same directory
     * as the class. If useDefaults is true, the method will search for a file "app_default.properties"
     * in the resources folder (which it should always find).
     * @param useDefaults
     * @throws IOException if an error occurred while reading from the file
     * @throws IllegalArgumentException if the file is in any way invalid
     * @throws NullPointerException if the file does not exist or could not be opened
     * @throws URISyntaxException this should never occur, thrown if
     * Settings.class.getProtectionDomain().getCodeSource().getLocation() cannot be converted to a
     * URI using .toURI()
     */
    public Settings(boolean useDefaults) throws IOException, IllegalArgumentException,
            NullPointerException, URISyntaxException {
        if (!useDefaults) {
            loadCustomProperties();
        } else {
            loadDefaultProperties();
        }
    }

    public String getUserName() {
        return userName;
    }

    public boolean hasRecentFiles() {
        return (!recentFiles.isEmpty());
    }

    public Set<QuestionFile> getRecentFiles() {
        return recentFiles;
    }

    public String getDefaultFileOpeningDirectory() {
        return defaultFileOpeningDirectory;
    }

    public double getPercentageWeighting() {
        return percentageWeighting;
    }

    public double getTimesAskedPercentageOffset() {
        return timesAskedPercentageOffset;
    }

    public double getTimesAskedAbsoluteOffset() {
        return timesAskedAbsoluteOffset;
    }

    private static OS determineOS() {
        String fullOS = System.getProperty("os.name").toLowerCase();

        if (fullOS.contains("win")) {
            return OS.WINDOWS;
        } else if (fullOS.contains("mac")) {
            return OS.MAC;
        } else if (fullOS.contains("nix") || fullOS.contains("nux") || fullOS.contains("aix")) {
            return OS.LINUX;
        } else {
            return OS.UNKNOWN;
        }
    }

    private void loadDefaultProperties() throws IOException, IllegalArgumentException,
            NullPointerException{
        InputStream stream = Settings.class.getClassLoader().
                getResourceAsStream(DEFAULT_SETTINGS_NAME);

        loadPropertiesFromStream(stream);
    }

    private void loadCustomProperties() throws IOException, IllegalArgumentException,
            NullPointerException, URISyntaxException {
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath();

        settingsDirectory = settingsDirectory.substring(0, settingsDirectory.lastIndexOf("/") + 1);

        String settingsLocation = settingsDirectory + CUSTOM_SETTINGS_NAME;
        InputStream stream = null;

        try {
            stream = new FileInputStream(settingsLocation);
        } catch (FileNotFoundException | SecurityException e) {
            // These exceptions are ignored as loadPropertiesFromStream is intended to deal with such
            // issues, and this will happen as stream is set to null by default
        }

        loadPropertiesFromStream(stream);
    }

    /**
     * Note this closes the stream
     * @param stream
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    private void loadPropertiesFromStream(InputStream stream) throws IOException,
            IllegalArgumentException, NullPointerException {
        class ValidatedProperties extends Properties {
            public String getStringProperty(String key) throws IOException, IllegalArgumentException {
                String value = super.getProperty(key);

                if (value == null) {
                    stream.close();
                    throw new IllegalArgumentException("Value for key \"" + key + "\" not found.");
                }

                switch (key) {
                    case "userName" -> {
                        return value;
                    }
                    case "defaultFileOpeningDirectory" -> {
                        if (value.equals("null")) {
                            return null;
                        }

                        try {
                            Paths.get(value);
                            return value;
                        } catch (InvalidPathException e) {
                            stream.close();
                            throw new IllegalArgumentException("\"" + value + "\" cannot be " +
                                    "interpreted as a valid file/directory path.");
                        }
                    }
                    default -> {
                        if (value.equals("null")) {
                            return null;
                        } else {
                            return value;
                        }
                    }
                }
            }

            public Double getDoubleProperty(String key) throws IOException, IllegalArgumentException {
                String value = super.getProperty(key);

                if (value == null) {
                    stream.close();
                    throw new IllegalArgumentException("Value for key \"" + key + "\" not found.");
                }

                try {
                    return Double.valueOf(value);
                } catch (NumberFormatException e) {
                    stream.close();
                    throw new IllegalArgumentException("Value \"" + value + "\" for key \"" + key +
                            "\" could not be parsed as a double.");
                }
            }
        }
        ValidatedProperties properties = new ValidatedProperties();

        properties.load(stream);

        userName = properties.getStringProperty("userName");

        recentFiles = new TreeSet<>();
        tryAddQuestionFile(properties.getStringProperty("recentFilePath1"),
                properties.getStringProperty("recentFileDate1"), 1);
        tryAddQuestionFile(properties.getStringProperty("recentFilePath2"),
                properties.getStringProperty("recentFileDate2"), 2);
        tryAddQuestionFile(properties.getStringProperty("recentFilePath3"),
                properties.getStringProperty("recentFileDate3"), 3);

        this.defaultFileOpeningDirectory = properties.getStringProperty("defaultFileOpeningDirectory");

        this.percentageWeighting = properties.getDoubleProperty("percentageWeighting");
        this.timesAskedPercentageOffset = properties.getDoubleProperty("timesAskedPercentageOffset");
        this.timesAskedAbsoluteOffset = properties.getDoubleProperty("timesAskedAbsoluteOffset");

        stream.close();
    }

    private void tryAddQuestionFile(String filePath, String fileDate, int fileNum) {
        if (filePath != null && fileDate != null) {
            recentFiles.add(new QuestionFile(filePath, fileDate));
        } else if (filePath != null) {
            throw new IllegalArgumentException("Recent file #" + fileNum + " has an associated " +
                    "file location \"" + filePath + "\", but no corresponding \"last opened\" date.");
        } else if (fileDate != null) {
            throw new IllegalArgumentException("Recent file #" + fileNum + " has an associated \"" +
                    "last opened\" date \"" + fileDate + "\", but no corresponding file location.");
        }
    }

    /**
     *
     * @throws Exception lots of things could go wrong
     */
    public static void copyDefaultSettings() throws Exception {
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath();

        settingsDirectory = settingsDirectory.substring(0, settingsDirectory.lastIndexOf("/") + 1);

        String settingsLocation = settingsDirectory + CUSTOM_SETTINGS_NAME;

        // workaround
        if (os.equals(OS.WINDOWS) && settingsLocation.indexOf("/") == 0) {
            settingsLocation = settingsLocation.substring(1);
        }

        InputStream defaultSettingsStream = Settings.class.getClassLoader().
                getResourceAsStream(DEFAULT_SETTINGS_NAME);


        Files.copy(defaultSettingsStream, Paths.get(settingsLocation), StandardCopyOption.REPLACE_EXISTING);

        defaultSettingsStream.close();
    }

    public static void updateCustomSettings(Settings settings) {

    }
}