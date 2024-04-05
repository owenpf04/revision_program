package program.attributes.values;

/**
 * An enum containing a list of valid values for the {@code PAPER_OR_UNIT}
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum PaperOrUnit {
    PAPER_1("Paper 1"),
    PAPER_2("Paper 2"),
    PAPER_3("Paper 3"),
    PAPER_4("Paper 4"),
    UNIT_1("Unit 1"),
    UNIT_2("Unit 2"),
    UNIT_3("Unit 3"),
    UNIT_4("Unit 4");

    /**
     * The name of a given {@code PaperOrUnit} constant, formatted in the way in which it should be
     * displayed to the user.
     */
    private final String PROPERTY_NAME;

    /**
     * Creates a new {@code PaperOrUnit} constant with the specified display name.
     *
     * @param propertyName
     *         the display name for the constant to create.
     */
    PaperOrUnit(String propertyName) {
        this.PROPERTY_NAME = propertyName;
    }

    /**
     * Returns an array of each {@code PaperOrUnit}'s display name.
     *
     * @return an array of each {@code PaperOrUnit}'s display name.
     */
    public static String[] getStringValues() {
        PaperOrUnit[] enumValues = PaperOrUnit.values();
        String[] returnArray = new String[enumValues.length];

        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = enumValues[i].PROPERTY_NAME;
        }

        return returnArray;
    }
}
