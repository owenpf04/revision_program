package program.helpers;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class Misc {
    // For some reason using UIManager.getColor(String) doesn't seem to properly update the colours
    // of components - I believe because it returns a javax.swing.plaf.ColorUIResource rather than
    // a java.awt.Color, although I don't know why that is an issue, as the former is a subclass of
    // the latter (as far as I am aware). Regardless, this simply converts the ColorUIResource into
    // a Color.
    public static Color getUIManagerColor(String key) {
        return new Color(UIManager.getColor(key).getRGB());
    }

    public static void updateCheckboxIcons(JCheckBox checkBox, int size) {
        checkBox.setIcon(FontIcon.of(CarbonIcons.CHECKBOX, size,
                Misc.getUIManagerColor("CheckBox.icon.borderColor")));
        checkBox.setSelectedIcon(FontIcon.of(CarbonIcons.CHECKBOX_CHECKED_FILLED, size,
                Misc.getUIManagerColor("colorSelectionBackground")));
    }
}
