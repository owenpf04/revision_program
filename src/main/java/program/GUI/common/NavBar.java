package program.GUI.common;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.helpers.Misc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class NavBar extends JPanel {
    public NavBar(String backButtonTitle, String continueButtonTitle) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton backButton = new JButton( backButtonTitle,
                FontIcon.of(CarbonIcons.ARROW_LEFT, 30,
                        Misc.getUIManagerColor("colorForeground")));
        backButton.setIconTextGap(15);
        backButton.putClientProperty("FlatLaf.styleClass", "h2.regular");

        int fontHeight = getFontMetrics(backButton.getFont()).getHeight();
        backButton.setPreferredSize(new Dimension((int) (1.05 *
                backButton.getPreferredSize().width), (int) (2.2 * fontHeight)));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonActionPerformed();
            }
        });

        JButton continueButton = new JButton(continueButtonTitle,
                FontIcon.of(CarbonIcons.ARROW_RIGHT, 30,
                        Misc.getUIManagerColor("colorSelectionForeground")));

        continueButton.setHorizontalTextPosition(SwingConstants.LEFT);
        continueButton.setIconTextGap(15);
        continueButton.putClientProperty("FlatLaf.styleClass", "h2.regular");
        continueButton.setPreferredSize(new Dimension((int) (1.05 *
                continueButton.getPreferredSize().width), (int) (2.2 * fontHeight)));
        //TODO this colour palette is hardcoded, and border colour is wrong
        continueButton.putClientProperty("FlatLaf.styleClass", "recommended h2.regular");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueButtonActionPerformed();
            }
        });

        add(backButton);
        add(continueButton);
        setBorder(new EmptyBorder(10, 0, 0, 0));
    }

    protected abstract void backButtonActionPerformed();
    protected abstract void continueButtonActionPerformed();
}
