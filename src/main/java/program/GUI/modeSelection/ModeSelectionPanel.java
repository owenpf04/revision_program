package program.GUI.modeSelection;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.helpers.SortingKey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModeSelectionPanel extends JPanel {
    private ButtonGroup modeSelectGroup;

    public ModeSelectionPanel() {
        modeSelectGroup = new ButtonGroup();

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        JPanel reviseModePanel = createTogglePanel(CarbonIcons.CHART_LINE_DATA, "Revise",
                "This is a mode where you really do some revision", true);

        JPanel testModePanel = createTogglePanel(CarbonIcons.LIST, "Test",
                "This is a mode where you will test yourself", false);

        JPanel customModePanel = createTogglePanel(CarbonIcons.SETTINGS_ADJUST, "Custom",
                "Revise in your own way, because all my ideas are so bad", false);
        customModePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(reviseModePanel, gbc);
        gbc.gridy++;
        add(testModePanel, gbc);
        gbc.gridy++;
        add(customModePanel, gbc);
        gbc.gridy++;
        // Filler panel
        gbc.weighty = 1;
        add(new JPanel(), gbc);

        setBorder(new EmptyBorder(0,30,0,15));
    }

    private JPanel createTogglePanel(CarbonIcons icon, String title, String description, boolean
            selected) {
        JPanel panel = new JPanel();
        JToggleButton button = new ModeToggleButton(icon, title, description);
        if (selected) {
            button.setSelected(true);
        }
        panel.add(button);

        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        return panel;
    }

    class ModeToggleButton extends JToggleButton {
        private SortingKey sortingKey;
        public ModeToggleButton(CarbonIcons icon, String title, String description) {
            JPanel contentPanel = new JPanel(new BorderLayout(0, 40));

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.putClientProperty("FlatLaf.styleClass", "h0");
            int fontHeight = getFontMetrics(titleLabel.getFont()).getHeight();
            titleLabel.setIcon(FontIcon.of(icon, fontHeight));
            titleLabel.setIconTextGap(20);
            titleLabel.setBorder(new EmptyBorder(30, 0, 0, 0));

            JLabel descriptionLabel = new JLabel("<html>" + description + "</html>");
            descriptionLabel.putClientProperty("FlatLaf.styleClass", "h2.regular");
            descriptionLabel.setVerticalAlignment(SwingConstants.TOP);

            contentPanel.add(titleLabel, BorderLayout.NORTH);
            contentPanel.add(descriptionLabel, BorderLayout.CENTER);

            String[] tempArray = {"Option A", "Option B", "Option C"};
            JComboBox<String> tempComboBox = new JComboBox<>(tempArray);
            contentPanel.add(tempComboBox, BorderLayout.SOUTH);

            contentPanel.setPreferredSize(new Dimension((int) (8 * fontHeight),
                    (int) (5 * fontHeight)));
            contentPanel.setOpaque(false);

            add(contentPanel);
            modeSelectGroup.add(this);
        }
    }

//    private class CustomModeToggleButton extends ModeToggleButton {
//
//    }
}
