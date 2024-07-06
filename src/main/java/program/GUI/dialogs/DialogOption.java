package program.GUI.dialogs;

public class DialogOption {
    private final String name;
    private final String description;

    public DialogOption(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
