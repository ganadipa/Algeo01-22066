package GUI.component;

import javax.swing.*;

import GUI.theme.Colors;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class InputField extends JTextField {
    private int cornerRadius = 10;
    private Color outlineColor = Colors.indigo400;
    private Color backgroundColor = Colors.slate700;
    private Color textColor = Colors.slate100;
    private int outlineThickness = 2;

    public InputField() {
        this(10); // Default columns
    }

    public InputField(int columns) {
        super(columns);
        setCaretColor(Colors.slate100);
        setOpaque(false); // Make the text field transparent so that the round corner works
        setForeground(textColor);
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10)); // Add padding

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        repaint();
    }

    public void setOutlineThickness(int outlineThickness) {
        this.outlineThickness = outlineThickness;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Paint the background
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 3, getWidth() - 1, getHeight() - 1 -3, cornerRadius, cornerRadius);

        // Paint the outline when in focus
        if (hasFocus()) {
            g2d.setColor(outlineColor);
            g2d.setStroke(new BasicStroke(outlineThickness));
            g2d.drawRoundRect(0, 3, getWidth() - 1, getHeight() - 1 -3, cornerRadius, cornerRadius);
        }

        g2d.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Do not paint the default border
    }
}
