package GUI.component;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JRadioButton;

import GUI.theme.Colors;

public class RadioButton extends JRadioButton {
    public RadioButton(String text) {
        super(text);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setForeground(Colors.slate100);
        setOpaque(false);
        setAlignmentX(LEFT_ALIGNMENT);
        setFocusPainted(false);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int ly = (getHeight() - 16) / 2;
        if(isSelected()) {
            if(isEnabled()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Colors.indigo400);
            }
            g2.setColor(Colors.indigo400);
            g2.fillOval(1, ly, 16, 16);

            g2.setColor(Colors.slate950);
            g2.fillOval(3, ly+2, 12, 12);
            
            g2.setColor(Colors.indigo400);
            g2.fillOval(6, ly+5, 6, 6);

        } else {
            if(isFocusOwner()) {
                g2.setColor(getBackground());
            } else {
                g2.setColor(Colors.slate100);
            }
            g2.fillOval(1, ly, 16, 16);
            g2.setColor(Colors.slate100);
            g2.fillOval(2, ly+1, 14, 14);
        }
        // g2.dispose();
    }

}
