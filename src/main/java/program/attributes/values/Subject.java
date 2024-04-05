package program.attributes.values;

/**
 * An enum containing a list of valid values for the {@code SUBJECT}
 * {@link program.attributes.fields.QuestionAttribute QuestionAttribute}.
 * <p>
 * Copyright (c) Owen Parfitt-Ford 2024. All rights reserved.
 */
public enum Subject {
    ART_AND_DESIGN("Art and Design"),
    BIOLOGY("Biology"),
    BUSINESS("Business"),
    CHEMISTRY("Chemistry"),
    COMBINED_SCIENCE("Combined Science"),
    COMPUTER_SCIENCE("Computer Science"),
    CRIMINOLOGY_CERTIFICATE("Criminology (Certificate)"),
    CRIMINOLOGY_DIPLOMA("Criminology (Diploma)"),
    DESIGN_AND_TECHNOLOGY("Design and Technology"),
    DRAMA("Drama"),
    ECONOMICS("Economics"),
    ENGLISH_LITERATURE("English Literature"),
    ENGLISH_LANGUAGE("English Language"),
    EPQ("EPQ"),
    FILM_STUDIES("Film Studies"),
    FOOD_TECHNOLOGY("Food Technology"),
    FRENCH("French"),
    FURTHER_MATHEMATICS("Further Mathematics"),
    GEOGRAPHY("Geography"),
    GERMAN("German"),
    HEALTH_AND_SOCIAL_CARE("Health and Social Care"),
    HISTORY("History"),
    LAW("Law"),
    MATHEMATICS("Mathematics"),
    MEDIA_STUDIES("Media Studies"),
    MUSIC("Music"),
    PHILOSOPHY("Philosophy"),
    PHYSICAL_EDUCATION("Physical Education"),
    PHYSICS("Physics"),
    POLITICS("Politics"),
    PSYCHOLOGY("Psychology"),
    RELIGIOUS_STUDIES("Religious Studies"),
    SOCIOLOGY("Sociology"),
    SPANISH("Spanish"),
    SPORT_SCIENCE("Sport Science"),
    STATISTICS("Statistics");

    /**
     * The name of a given {@code Subject} constant, formatted in the way in which it should be
     * displayed to the user.
     */
    private final String PROPERTY_NAME;

    /**
     * Creates a new {@code Subject} constant with the specified display name.
     *
     * @param propertyName
     *         the display name for the constant to create.
     */
    Subject(String propertyName) {
        this.PROPERTY_NAME = propertyName;
    }

    /**
     * Returns an array of each {@code Subject}'s display name.
     *
     * @return an array of each {@code Subject}'s display name.
     */
    public static String[] getStringValues() {
        Subject[] enumValues = Subject.values();
        String[] returnArray = new String[enumValues.length];

        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = enumValues[i].PROPERTY_NAME;
        }

        return returnArray;
    }
}
