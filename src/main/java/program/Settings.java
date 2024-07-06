package program;

import program.exceptions.InvalidPropertiesException;
import program.helpers.InvalidProperty;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public abstract class Settings {

    //TODO store lifetime statistics

    private static final String DEFAULT_SETTINGS_NAME = "app_default.properties";
    private static final String CUSTOM_SETTINGS_NAME = "app.properties";

    // percentageWeighting cannot be equal to minus one as would be dividing by 0

    // percentage and absolute offsets cannot both be 0 (divisor is (percentage * expected asks)
    // + absolute)

    private static Properties defaultProperties;

    private static Properties customProperties;


    public static String getProperty(String key) {
        return customProperties.getProperty(key);
    }

    public static String getUserName() {
        return parseUsername(customProperties.getProperty("userName"));
    }

    public static boolean hasRecentFiles() {
        return (!getRecentFiles().isEmpty());
    }

    public static List<QuestionFile> getRecentFiles() {
        List<QuestionFile> recentFiles = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            String filePath = parseNull(customProperties.getProperty("recentFilePath" + i));
            String fileDate = parseNull(customProperties.getProperty("recentFileDate" + i));

            if (filePath != null && fileDate != null) {
                recentFiles.add(new QuestionFile(filePath, fileDate));
            }
        }

        recentFiles.sort(null);
        return recentFiles;
    }

    public static String getDefaultFileOpeningDirectory() {
        return parseNull(customProperties.getProperty("defaultFileOpeningDirectory"));
    }

    public static double getPercentageWeighting() {
        return Double.parseDouble(customProperties.getProperty("percentageWeighting"));
    }

    public static double getTimesAskedPercentageOffset() {
        return Double.parseDouble(customProperties.getProperty("timesAskedPercentageOffset"));
    }

    public static double getTimesAskedAbsoluteOffset() {
        return Double.parseDouble(customProperties.getProperty("timesAskedAbsoluteOffset"));
    }


    public static void initialiseProperties() throws IOException, NullPointerException,
            URISyntaxException {
        loadDefaultProperties();
        loadCustomProperties();
    }

    public static void validateAllProperties() throws InvalidPropertiesException {
        validateProperties(defaultProperties);
        validateProperties(customProperties);
    }

    /**
     * @throws Exception
     *         lots of things could go wrong
     */
    public static void resetAllFieldsToDefaults() throws URISyntaxException, IOException {
        customProperties = defaultProperties;
        saveSettings();
    }

    public static void resetFieldsToDefaults(Collection<String> keys) throws IllegalArgumentException,
            InvalidPropertiesException, URISyntaxException, IOException {
        for (String key : keys) {
            updateSingleField(key, defaultProperties.getProperty(key));

            //TODO i don't think there's a nicer way of doing this, but may be worth checking
            if (key.equals("recentFilePath1") || key.equals("recentFilePath2") || key.equals("recentFilePath3")) {
                String otherKey = "recentFileDate" + key.charAt(14);
                updateSingleField(otherKey, defaultProperties.getProperty(otherKey));
            } else if (key.equals("recentFileDate1") || key.equals("recentFileDate2") ||
                    key.equals("recentFileDate3")) {
                String otherKey = "recentFilePath" + key.charAt(14);
                updateSingleField(otherKey, defaultProperties.getProperty(otherKey));
            }
        }

        validateProperties(customProperties);
        saveSettings();
    }

    public static void updateField(String key, String newValue) throws InvalidPropertiesException,
            URISyntaxException, IOException{
        updateSingleField(key, newValue);
        validateProperties(customProperties);
        saveSettings();
    }

    public static void addRecentFile(String fileLocation) throws InvalidPropertiesException,
            URISyntaxException, IOException {
        List<QuestionFile> recentFiles = getRecentFiles();

        boolean alreadyRecent = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(formatter);

        for (int i = 0; i < recentFiles.size(); i++) {
            QuestionFile file = recentFiles.get(i);

            if (file.getFilePath().equals(fileLocation)) {
                alreadyRecent = true;
                recentFiles.set(i, new QuestionFile(fileLocation, currentDateTime));
            }
        }

        if (!alreadyRecent) {
            if (recentFiles.size() < 3) {
                recentFiles.add(new QuestionFile(fileLocation, currentDateTime));
            } else {
                recentFiles.set(2, new QuestionFile(fileLocation, currentDateTime));
            }
        }

        recentFiles.sort(null);
        for (int i = 0; i < 3; i++) {
            try {
                QuestionFile file = recentFiles.get(i);
                updateSingleField(("recentFilePath" + (i + 1)), file.getFilePath());
                updateSingleField(("recentFileDate" + (i + 1)),
                        file.getLastOpened().format(formatter));
            } catch (IndexOutOfBoundsException e) {
                updateSingleField(("recentFilePath" + (i + 1)), "null");
                updateSingleField(("recentFileDate" + (i + 1)), "null");
            }
        }

        validateProperties(customProperties);
        saveSettings();
    }

    public static void saveSettings() throws URISyntaxException, IOException {
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI().getPath();

        settingsDirectory = settingsDirectory.substring(0,
                settingsDirectory.lastIndexOf("/") + 1);

        String settingsLocation = settingsDirectory + CUSTOM_SETTINGS_NAME;
        customProperties.store(new FileOutputStream(settingsLocation),
                "Last updated by program:");
    }


    private static void loadDefaultProperties() throws IOException, NullPointerException {
        InputStream stream = Settings.class.getClassLoader().
                getResourceAsStream(DEFAULT_SETTINGS_NAME);

        defaultProperties = new Properties();
        defaultProperties.load(stream);
        stream.close();
    }

    private static void loadCustomProperties() throws IOException, NullPointerException,
            URISyntaxException {
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

        customProperties = new Properties();
        customProperties.load(stream);
        stream.close();
    }

//    /**
//     * Creates a new Settings object from the key-value pairs specified in a .properties file. If
//     * useDefaults is false, the method will search for a file "app.properties" in the same directory
//     * as the class. If useDefaults is true, the method will search for a file "app_default.properties"
//     * in the resources folder (which it should always find).
//     * @param useDefaults
//     * @throws IOException if an error occurred while reading from the file
//     * @throws IllegalArgumentException if the file is in any way invalid
//     * @throws NullPointerException if the file does not exist or could not be opened
//     * @throws URISyntaxException this should never occur, thrown if
//     * Settings.class.getProtectionDomain().getCodeSource().getLocation() cannot be converted to a
//     * URI using .toURI()
//     */
//    public Settings(boolean useDefaults) throws IOException, InvalidPropertiesException,
//            NullPointerException, URISyntaxException {
//        if (!useDefaults) {
//            properties = loadCustomProperties();
//        } else {
//            properties = loadDefaultProperties();
//        }
//
//        validateProperties(properties);
//    }
//
//    public Settings(Properties properties) throws InvalidPropertiesException {
//        Settings.validateProperties(properties);
//        this.properties = properties;
//    }

    private static void validateProperties(Properties properties) throws InvalidPropertiesException {
        Set<InvalidProperty> invalidPropertySet = new LinkedHashSet<>();

        try {
            parseUsername(properties.getProperty("userName"));
        } catch (IllegalArgumentException e) {
            invalidPropertySet.add(new InvalidProperty("userName", e.getMessage()));
        }

        for (int i = 1; i < 4; i++) {
            String filePath = properties.getProperty("recentFilePath" + i);
            String fileDate = properties.getProperty("recentFileDate" + i);

            try {
                verifyFileLocation(("recentFilePath" + i), filePath);
            } catch (IllegalArgumentException e) {
                invalidPropertySet.add(new InvalidProperty(("recentFilePath" + i),
                        e.getMessage()));
            }

            try {
                verifyDateTime(("recentFileDate" + i), fileDate);
            } catch (IllegalArgumentException e) {
                invalidPropertySet.add(new InvalidProperty(("recentFileDate" + i),
                        e.getMessage()));
            }

            if (filePath.equals("null") && !fileDate.equals("null")) {
                String description = "Recent file #" + i + " has an associated '" +
                        "last opened' date \"" + fileDate +
                        "\", but no corresponding file location.";
                invalidPropertySet.add(new InvalidProperty(("recentFilePath" + i),
                        description));
            } else if (!filePath.equals("null") && fileDate.equals("null")) {
                String description = "Recent file #" + i + " has an associated " +
                        "file location \"" + filePath +
                        "\", but no corresponding 'last opened' date.";
                invalidPropertySet.add(new InvalidProperty(("recentFileDate" + i),
                        description));
            }
        }

        try {
            verifyFileLocation("defaultFileOpeningDirectory",
                    properties.getProperty("defaultFileOpeningDirectory"));
        } catch (IllegalArgumentException e) {
            invalidPropertySet.add(new InvalidProperty("defaultFileOpeningDirectory",
                    e.getMessage()));
        }

        try {
            verifyDoubleProperty("percentageWeighting",
                    properties.getProperty("percentageWeighting"));
        } catch (IllegalArgumentException e) {
            invalidPropertySet.add(new InvalidProperty("percentageWeighting",
                    e.getMessage()));
        }
        try {
            verifyDoubleProperty("timesAskedPercentageOffset",
                    properties.getProperty("timesAskedPercentageOffset"));
        } catch (IllegalArgumentException e) {
            invalidPropertySet.add(new InvalidProperty("timesAskedPercentageOffset",
                    e.getMessage()));
        }
        try {
            verifyDoubleProperty("timesAskedAbsoluteOffset",
                    properties.getProperty("timesAskedAbsoluteOffset"));
        } catch (IllegalArgumentException e) {
            invalidPropertySet.add(new InvalidProperty("timesAskedAbsoluteOffset",
                    e.getMessage()));
        }

        if (!invalidPropertySet.isEmpty()) {
            throw new InvalidPropertiesException(invalidPropertySet);
        }
    }

    private static String parseUsername(String value) throws IllegalArgumentException {
        verifyNonNull("userName", value);

        if (value.equals("user.name")) {
            return System.getProperty("user.name");
        } else {
            return value;
        }
    }

    private static void verifyFileLocation(String key, String value) throws IllegalArgumentException {
        verifyNonNull(key, value);

        if (!value.equals("null")) {
            try {
                Paths.get(value);
            } catch (InvalidPathException e) {
                throw new IllegalArgumentException("\"" + value + "\" cannot be " +
                        "interpreted as a valid file/directory path.");
            }
        }
    }

    private static void verifyDateTime(String key, String value) throws IllegalArgumentException {
        verifyNonNull(key, value);

        if (!value.equals("null")) {
            try {
                LocalDateTime.parse(value);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException(
                        "\"" + value + "\" is not in the correct format to be interpretable as a " +
                                "datetime - see the ISO-8601 standard for the correct format.");
            }
        }
    }

    private static void verifyDoubleProperty(String key, String value) throws IllegalArgumentException {
        verifyNonNull(key, value);

        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + value + "\" could not be parsed as a number.");
        }
    }

    private static void verifyNonNull(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value for key \"" + key + "\" not found.");
        }
    }

    private static String parseNull(String value) {
        if (value.equals("null")) {
            return null;
        }

        return value;
    }

    private static Properties updateSingleField(String key, String newValue)
            throws IllegalArgumentException {
        if (customProperties.replace(key, newValue) == null) {
            throw new IllegalArgumentException("\"" + key + "\" is not a valid field key");
        }

        return customProperties;
    }
}
