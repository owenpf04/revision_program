package program.attributes.values;

/**
 * An enum containing a list of valid values for the {@code EXAM_BOARD}
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum ExamBoard {
    AQA("AQA"),
    EDEXCEL("Edexcel"),
    OCR("OCR"),
    WJEC("WJEC");

    /**
     * The name of a given {@code ExamBoard} constant, formatted in the way in which it should be
     * displayed to the user.
     */
    private final String PROPERTY_NAME;

    /**
     * Creates a new {@code ExamBoard} constant with the specified display name.
     *
     * @param propertyName
     *         the display name for the constant to create.
     */
    ExamBoard(String propertyName) {
        this.PROPERTY_NAME = propertyName;
    }

    /**
     * Returns an array of each {@code ExamBoard}'s display name.
     *
     * @return an array of each {@code ExamBoard}'s display name.
     */
    public static String[] getStringValues() {
        ExamBoard[] enumValues = ExamBoard.values();
        String[] returnArray = new String[enumValues.length];

        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = enumValues[i].PROPERTY_NAME;
        }

        return returnArray;
    }
}
