package program;

import java.io.*;
import java.util.Properties;

/**
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public class Settings {
    private String userName;

    private String recentFilePath1;
    private String recentFilePath2;
    private String recentFilePath3;

    private String defaultFileOpeningDirectory;

    // cannot be equal to minus one as would be dividing by 0
    private double percentageWeighting;

    // percentage and absolute offsets cannot both be 0 (divisor is (percentage * expected asks)
    // + absolute
    private double timesAskedPercentageOffset;
    private double timesAskedAbsoluteOffset;

    public Settings(boolean useDefaults) throws IOException, IllegalArgumentException,
            NullPointerException {
        if (!useDefaults) {
            loadCustomProperties();
        } else {
            loadDefaultProperties();
        }
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

    private void loadDefaultProperties() throws IOException, IllegalArgumentException,
            NullPointerException{
        InputStream stream = Settings.class.getClassLoader().
                getResourceAsStream("app_default.properties");

        loadPropertiesFromStream(stream);
    }

    private void loadCustomProperties() throws IOException, IllegalArgumentException,
            NullPointerException{
        String settingsDirectory = Settings.class.getProtectionDomain().getCodeSource()
                .getLocation().getPath();
        settingsDirectory = settingsDirectory.substring(0, settingsDirectory.lastIndexOf("/"));

        String settingsLocation = settingsDirectory + "/app_default.properties";
        InputStream stream = null;

        try {
            stream = new FileInputStream(settingsLocation);
        } catch (FileNotFoundException | SecurityException e) {
            // These exceptions are ignored as loadPropertiesFromStream is intended to deal with such
            // issues, and this will happen as stream is set to null by default
        }

        loadPropertiesFromStream(stream);
    }

    private void loadPropertiesFromStream(InputStream stream) throws IOException,
            IllegalArgumentException, NullPointerException {
        class ValidatedProperties extends Properties {
            public String getProperty(String key) {
                String value = super.getProperty(key);
                if (value == null) {
                    throw new IllegalArgumentException();
                } else if (value.equals("\"\"")) {
                    return null;
                } else {
                    return value;
                }
            }
        }
        Properties properties = new Properties();

        properties.load(stream);


    }

    private void updateKeyFromProperties(Object key, )
}