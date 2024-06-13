package program.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TitlePanel extends JPanel{
    private JLabel greetingLabel;
    private JLabel openFilePromptLabel;
    private JSeparator separator;

    public TitlePanel(String titleMessage, String subtitleMessage) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(0,0,30,0));

        greetingLabel = new JLabel();
        greetingLabel.setText(titleMessage);
        greetingLabel.putClientProperty("FlatLaf.styleClass", "h00");
        greetingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        greetingLabel.setAlignmentX(LEFT_ALIGNMENT);

        openFilePromptLabel = new JLabel(subtitleMessage, SwingConstants.LEFT);
        openFilePromptLabel.putClientProperty("FlatLaf.styleClass", "h1.regular");
        openFilePromptLabel.setBorder(new EmptyBorder(0,0,15,0));
        openFilePromptLabel.setAlignmentX(LEFT_ALIGNMENT);

        separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setAlignmentX(LEFT_ALIGNMENT);

        add(greetingLabel);
        add(openFilePromptLabel);
        add(separator);
    }
}