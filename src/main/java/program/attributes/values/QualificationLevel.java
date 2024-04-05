package program.attributes.values;

/**
 * An enum containing a list of valid values for the {@code QUALIFICATION_LEVEL}
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum QualificationLevel {
    A_LEVEL("A Level"),
    BTEC_LEVEL_2("BTEC Level 2"),
    BTEC_LEVEL_3("BTEC Level 3"),
    GCSE("GCSE"),
    EPQ("EPQ");

    /**
     * The name of a given {@code QualificationLevel} constant, formatted in the way in which it
     * should be displayed to the user.
     */
    private final String PROPERTY_NAME;

    /**
     * Creates a new {@code QualificationLevel} constant with the specified display name.
     *
     * @param propertyName
     *         the display name for the constant to create.
     */
    QualificationLevel(String propertyName) {
        this.PROPERTY_NAME = propertyName;
    }

    /**
     * Returns an array of each {@code QualificationLevel}'s display name.
     *
     * @return an array of each {@code QualificationLevel}'s display name.
     */
    public static String[] getStringValues() {
        QualificationLevel[] enumValues = QualificationLevel.values();
        String[] returnArray = new String[enumValues.length];

        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = enumValues[i].PROPERTY_NAME;
        }

        return returnArray;
    }
}
