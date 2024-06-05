package program.exceptions;

public class InvalidQuestionFileException extends Exception {
    private final String fileLocation;
    private final int lineNumber;
    private final String line;
    private final String description;

    public InvalidQuestionFileException(String line, String description) {
        this(null, -1, line, description);
    }

    public InvalidQuestionFileException(String filelocation, int lineNumber, String line, String description) {
        this.fileLocation = filelocation;
        this.lineNumber = lineNumber;
        this.line = line;
        this.description = description;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() {
        return line;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Invalid Question file:\nFile location: \"" + fileLocation + "\"\nLine number: " +
                lineNumber + "\nLine: \"" + line + "\"\nDescription: \"" + description + "\"";
    }
}
