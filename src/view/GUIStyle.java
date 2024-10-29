package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

public class GUIStyle {

    protected static Font FONT = new Font("Sans serif", Font.BOLD, 15);

    public static void applyStyle(JButton button) {
        button.setBackground(Color.WHITE);
        button.setFont(FONT);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    public static Font getFont(float fontSize) {
        if (fontSize != 0)
            return FONT.deriveFont(fontSize);
        return FONT;
    }
}
