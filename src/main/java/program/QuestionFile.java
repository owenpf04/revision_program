package program;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class QuestionFile implements Comparable<QuestionFile>{
    private String filePath;
    private String fileName;
    private LocalDateTime lastOpened;

    public QuestionFile(String filePath, String dateTime) throws IllegalArgumentException {
        try {
            Paths.get(filePath);
            this.filePath = filePath;
            this.fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("\"" + filePath + "\" cannot be interpreted as a " +
                    "file/directory path.");
        }

        try {
            lastOpened = LocalDateTime.parse(dateTime);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("\"" + dateTime + "\" is not in the correct format " +
                    "to be interpretable as a datetime - see the ISO-8601 standard for the correct " +
                    "format.");
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getLastOpened() {
        return lastOpened;
    }

    /**
     * Compares two QuestionFiles based on their last opened datetimes
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(QuestionFile o) {
        // want most recent file first
        return (-1 * lastOpened.compareTo(o.lastOpened));
    }
}