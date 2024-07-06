package program.GUI.dialogs;

import program.exceptions.InvalidPropertiesException;
import program.helpers.InvalidProperty;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public abstract class DialogComponents {
    static JLabel titleLabel(String title) {
        JLabel mainLabel = new JLabel("<html>" + ReformatString.wrapString(title,
                60, 0, true) + "</html>");
        mainLabel.putClientProperty("FlatLaf.styleClass", "h1");

        return mainLabel;
    }

    static JPanel invalidPropertiesPanel(InvalidPropertiesException e) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        Set<InvalidProperty> invalidPropertySet = e.getInvalidPropertySet();

        int i = 0;
        for (InvalidProperty prop : invalidPropertySet) {
            gbc.gridy = i;
            i++;

            String title = (i + ". \"" + prop.getKey() + "\":");

            JPanel innerPanel = new JPanel(new BorderLayout(0, 5));

            JLabel keyLabel = new JLabel(title);
            keyLabel.putClientProperty("FlatLaf.styleClass", "h3");

            JLabel descLabel = new JLabel("<html>" +
                    ReformatString.wrapString(prop.getDescription(), 100, 0,
                            true) + "</html>");
            descLabel.putClientProperty("FlatLaf.styleClass", "medium");

            innerPanel.add(keyLabel, BorderLayout.NORTH);
            innerPanel.add(descLabel, BorderLayout.CENTER);
            innerPanel.setBorder(new EmptyBorder(0,0,15,0));

            mainPanel.add(innerPanel, gbc);
        }

        gbc.gridy = invalidPropertySet.size() - 1;
        gbc.weighty = 1;
        // filler panel
        mainPanel.add(new JPanel(), gbc);
        mainPanel.setBorder(new EmptyBorder(10,15,0,0));

        return mainPanel;
    }
}
