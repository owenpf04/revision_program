package program;

import program.exceptions.InvalidPropertiesException;
import program.helpers.InvalidProperty;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
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

    /**
     * Initialises the {@link Settings} class' {@link #customProperties} and
     * {@link #defaultProperties} objects (i.e. loads the properties in for use in the rest of the
     * program). This method MUST be called before any other methods can be used, as all other
     * methods rely on the properties being loaded.
     * <p>
     * Note that this method does not validate these properties, it only loads them, meaning the
     * {@link #validateAllProperties()} method should be called afterwards to ensure properties are
     * valid.
     * @throws IOException if an error occurs while reading from either of the input streams for the
     * custom or default properties files.
     * @throws IllegalArgumentException if either of the default or custom properties files contain
     * a malformed Unicode escape sequence.
     * @throws NullPointerException if either of the default or custom properties files could not be
     * found or opened for some reason (meaning no input stream to read from), or technically if
     * either {@link #DEFAULT_SETTINGS_NAME} or {@link #CUSTOM_SETTINGS_NAME} are {@code null}.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     */
    public static void initialiseProperties() throws IOException, IllegalArgumentException,
            NullPointerException, URISyntaxException {
        loadDefaultProperties();
        loadCustomProperties();
    }

    /**
     * Calls {@link #validateProperties(Properties)} on both {@link #defaultProperties} (not that
     * this should be necessary as the defaults should be valid) and {@link #customProperties}.
     * @throws InvalidPropertiesException if either {@link Properties} object is in any way invalid
     * (if there are required key-value pairs missing, or if the values for any required keys are
     * invalid). The exception will contain the details of all of the issues found, not just the
     * first one.
     */
    public static void validateAllProperties() throws InvalidPropertiesException {
        validateProperties(defaultProperties);
        validateProperties(customProperties);
    }

    /**
     * Resets all properties to their defaults, and saves these properties. See
     * {@link #saveSettings()}.
     * @throws InvalidPropertiesException if the default properties {@link #defaultProperties} are
     * invalid (this should never occur).
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    public static void resetAllFieldsToDefaults() throws InvalidPropertiesException, URISyntaxException,
            IOException {
        customProperties = defaultProperties;
        validateProperties(customProperties);
        saveSettings();
    }

    /**
     * Updates {@link #customProperties}' values for each of the keys in {@code keys} with the
     * corresponding values from {@link #defaultProperties}, and saves the result (see
     * {@link #saveSettings()}).
     * @param keys a collection of keys, each of whose values should be reset to its default value.
     * @throws IllegalArgumentException if any of the provided keys is not present in
     * {@code customProperties}.
     * @throws InvalidPropertiesException if the resulting set of properties is invalid (this should
     * never occur).
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
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

    /**
     * Updates {@link #customProperties}'s value for {@code key} to {@code value}, and saves the
     * result (see {@link #saveSettings()}).
     * @param key the key whose value should be updated.
     * @param newValue the value that {@code key} should be updated to.
     * @throws IllegalArgumentException if the provided key does not exist in {@code customProperties}.
     * @throws InvalidPropertiesException if the resulting set of properties is invalid.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    public static void updateField(String key, String newValue) throws IllegalArgumentException,
            InvalidPropertiesException, URISyntaxException, IOException{
        updateSingleField(key, newValue);
        validateProperties(customProperties);
        saveSettings();
    }

    /**
     * Updates the values of {@code recentFilePath1}, {@code 2} & {@code 3}, and {@code recentFileDate1},
     * {@code 2} & {@code 3} to include a new entry with {@code fileLocation} as the file path, and
     * the current LocalDateTime ({@link LocalDateTime#now()}) as the file date, and then saves the
     * result (see {@link #saveSettings()}). If all 3 recent file slots are already taken, the one
     * with the oldest date (i.e. the one opened least recently) will be replaced with the new entry.
     * <p>
     * Although the recent files need not be sorted before running this method, they will be sorted
     * afterwards - so recent file 1 will be the most recent (i.e. the one added by this method)
     * etc. See {@link #setRecentFiles(Collection)} for more details on updating recent files.
     * @param fileLocation the file path to associate with the new entry.
     * @throws InvalidPropertiesException if the resulting set of properties is invalid.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    public static void addRecentFile(String fileLocation) throws InvalidPropertiesException,
            URISyntaxException, IOException {
        List<QuestionFile> recentFiles = getRecentFiles();

        boolean alreadyRecent = false;
        String currentDateTime = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss"));

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

        setRecentFiles(recentFiles);
    }

    /**
     * Removes the recent file entry with {@code fileLocation} as its file path, updates the
     * relevant properties in {@link #customProperties} and saves the result.
     * <p>
     * Although the recent files need not be sorted before running this method, they will be sorted
     * afterwards - so recent file 1 will be the most recent etc. See
     * {@link #setRecentFiles(Collection)} for more details on updating recent files.
     * @param fileLocation the file path of the recent file to remove.
     * @throws InvalidPropertiesException if the resulting set of properties is invalid.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    public static void removeRecentFile(String fileLocation) throws InvalidPropertiesException,
            URISyntaxException, IOException {
        List<QuestionFile> recentFiles = getRecentFiles();
        List<QuestionFile> toRemove = new ArrayList<>();

        for (int i = 0; i < recentFiles.size(); i++) {
            QuestionFile file = recentFiles.get(i);

            if (file.getFilePath().equals(fileLocation)) {
                toRemove.add(file);
                //TODO what happens if same filepath appears twice
            }
        }

        recentFiles.removeAll(toRemove);

        setRecentFiles(recentFiles);
    }

    /**
     * Writes the current set of key-value pairs stored in {@link #customProperties} to a new custom
     * properties file with a name provided by {@link #CUSTOM_SETTINGS_NAME}, and in the same
     * directory as this class (in the directory supplied by {@code
     * Settings.class.getProtectionDomain().getCodeSource().getLocation()}). This is done via the
     * {@link Properties#store(OutputStream, String)} method. If such a file already exists, its
     * contents will be overwritten.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    public static void saveSettings() throws URISyntaxException, IOException {
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI().getPath();

        settingsDirectory = settingsDirectory.substring(0,
                settingsDirectory.lastIndexOf("/") + 1);

        String settingsLocation = settingsDirectory + CUSTOM_SETTINGS_NAME;
        customProperties.store(new FileOutputStream(settingsLocation),
                "Last updated by program:");
    }

    /**
     * Loads the default properties from the default properties file in resources (whose name should
     * be {@link #DEFAULT_SETTINGS_NAME}). Note this does not validate these properties (not that
     * they should need validation since they are the defaults), it simply assigns
     * {@link #defaultProperties} the {@link Properties} object created.
     * @throws IOException if an error occurs while reading from the input stream.
     * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape
     * sequence.
     * @throws NullPointerException if the input stream is null (i.e. if the default properties file
     * could not be found), or technically if {@code DEFAULT_SETTINGS_NAME == null} .
     */
    private static void loadDefaultProperties() throws IOException, IllegalArgumentException,
            NullPointerException {
        InputStream stream = Settings.class.getClassLoader().
                getResourceAsStream(DEFAULT_SETTINGS_NAME);

        defaultProperties = new Properties();
        defaultProperties.load(stream);
        stream.close();
    }

    /**
     * Loads the custom properties from the custom properties file, whose name should be
     * {@link #CUSTOM_SETTINGS_NAME}, and which should be located in the same directory as this
     * class (in the directory supplied by {@code
     * Settings.class.getProtectionDomain().getCodeSource().getLocation()}). Note this does not
     * validate these properties, it simply assigns {@link #customProperties} the {@link Properties}
     * object created, and therefore they should be later validated using
     * {@link #validateAllProperties()}.
     * @throws IOException if an error occurs while reading from the input stream.
     * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape
     * sequence.
     * @throws NullPointerException if the input stream is null (meaning the custom properties file
     * could not be found or opened for some reason), or technically if {@code CUSTOM_SETTINGS_name
     * == null}.
     * @throws URISyntaxException if the URL returned by {@code Settings.class.} ...
     * {@code .getLocation()} is not formatted strictly according to RFC2396 and could not be
     * converted to a URI (this should never occur).
     */
    private static void loadCustomProperties() throws IOException, IllegalArgumentException,
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

        customProperties = new Properties();
        customProperties.load(stream);
        stream.close();
    }

    /**
     * Checks that the provided {@link Properties} object has values for all of the required keys,
     * and that these values are all valid. Any key-value pairs which aren't required will be ignored.
     * @param properties the {@code Properties} object to check for validity.
     * @throws InvalidPropertiesException if {@code properties} is in any way invalid (if there are
     * required key-value pairs missing, or if the values for any required keys are invalid). The
     * exception will contain the details of all of the issues found, not just the first one.
     */
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

    /**
     * Returns {@code value} interpreted as a username, which simply means that if {@code value ==
     * "user.name"}, {@link System#getProperty(String) System.getProperty(value)} will be returned
     * instead.
     * @param value the {@link String} to parse as a username.
     * @return {@code System.getProperty(value)} if {@code value == "user.name"}, {@code value}
     * otherwise.
     * @throws IllegalArgumentException if {@code value == null}.
     */
    private static String parseUsername(String value) throws IllegalArgumentException {
        verifyNonNull("userName", value);

        if (value.equals("user.name")) {
            return System.getProperty("user.name");
        } else {
            return value;
        }
    }

    /**
     * Verifies that {@code value} is a valid <i>theoretical</i> file/directory location (theoretical
     * meaning this method does not check that a file/directory <i>actually exists</i> at the location
     * specified by {@code value}, it just checks if {@code value} is a valid location at which a
     * file/directory could exist).
     * @param key the name of the key to which the provided value relates (used for
     * {@link #verifyNonNull(String, String)}).
     * @param value the value, relating to the provided key, which is to be verified.
     * @throws IllegalArgumentException if {@code value == null}, or if {@code value} is not a valid
     * file/directory location (checked using {@link Paths#get(String, String...)}).
     */
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

    /**
     * Verifies that {@code value} is a valid {@link LocalDateTime} (i.e. that it can be interpreted
     * as per the ISO-8601 standard). The method does not check if {@code value} is a
     * <i>sensible</i> {@code LocalDateTime} (i.e. whether it is in the past or the future, how
     * distant it is from the current time etc).
     * @param key the name of the key to which the provided value relates (used for
     * {@link #verifyNonNull(String, String)}).
     * @param value the value, relating to the provided key, which is to be verified.
     * @throws IllegalArgumentException if {@code value == null}, or if {@code value} is not a valid
     * {@code LocalDateTime} (checked using {@link LocalDateTime#parse(CharSequence)}).
     */
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

    /**
     * Verifies that {@code value} is a valid {@link Double}.
     * @param key the name of the key to which the provided value relates (used for
     * {@link #verifyNonNull(String, String)}).
     * @param value the value, relating to the provided key, which is to be verified.
     * @throws IllegalArgumentException if {@code value == null}, or if {@code value} cannot be
     * parsed as a double (see {@link Double#valueOf(String)}).
     */
    private static void verifyDoubleProperty(String key, String value) throws IllegalArgumentException {
        verifyNonNull(key, value);

        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + value + "\" could not be parsed as a number.");
        }
    }

    /**
     * Verifies that {@code value != null}. This method is to be used in the context of interpreting
     * key-value pairs from a {@link Properties} object, and so the parameter {@code key} is
     * required for the purpose of personalising the potentially thrown
     * {@code IllegalArgumentException} with the message
     * {@code "Value for key \"" + key + "\" not found."}.
     * @param key the name of the key to which the provided value relates.
     * @param value the value, relating to the provided key, which is to be verified.
     * @throws IllegalArgumentException if {@code value == null};
     */
    private static void verifyNonNull(String key, String value) throws IllegalArgumentException {
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

    /**
     * Sets {@code newValue} as the value for the key named {@code key} in {@link #customProperties}.
     * If no key named {@code key} exists, an {@code IllegalArgumentException} is thrown. This method
     * does not check if {@code newValue} is a valid value, so this should be done separately using
     * {@link #validateProperties(Properties)}.
     * @param key the name of the key whose value should be replaced.
     * @param newValue the new value to set for the specified key.
     * @throws IllegalArgumentException if no key named {@code key} currently exists in {@code
     * customProperties}.
     */
    private static void updateSingleField(String key, String newValue)
            throws IllegalArgumentException {
        if (customProperties.replace(key, newValue) == null) {
            throw new IllegalArgumentException("\"" + key + "\" is not a valid field key");
        }
    }

    /**
     * Sets the mappings for {@code recentFilePath1}, {@code 2} & {@code 3} and {@code recentFileDate1},
     * {@code 2} & {@code 3} to reflect the contents of {@code files}, and saves the result (see
     * {@link #saveSettings()}). {@code recentFilePath1} and {@code -date1} will be updated to match
     * the details of the {@link QuestionFile} in {@code files} with the most recent
     * {@code lastOpened} date, and so on.
     * <p>
     * If {@code files} contains less than
     * 3 {@code QuestionFile}s, the excess keys will be updated to {@code "null"} (e.g. if {@code files}
     * only contained one {@code QuestionFile}, {@code recentFilePath2} & {@code -date2}, and {@code
     * recentFilePath3} & {@code -date3} would all be set to {@code "null"}).
     * @param files the collection of {@code QuestionFiles} to update the properties mappings to
     * reflect.
     * @throws InvalidPropertiesException if the resulting set of properties is invalid.
     * @throws URISyntaxException if the URL returned by Settings.class. ... .getLocation() is not
     * formatted strictly according to RFC2396 and could not be converted to a URI (this should never
     * occur).
     * @throws IOException if the custom properties file cannot be created (or opened if it already
     * exists), or if an error occurs while writing the properties to the file to the output stream.
     */
    private static void setRecentFiles(Collection<QuestionFile> files) throws InvalidPropertiesException,
            URISyntaxException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        List<QuestionFile> recentFiles = new ArrayList<>(files);

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
}
