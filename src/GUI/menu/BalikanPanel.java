package GUI.menu;

import java.awt.Color;

import javax.swing.JLabel;

import GUI.theme.Colors;

public class BalikanPanel extends javax.swing.JPanel {

    public BalikanPanel() {
        setBackground(Colors.transparent);
        JLabel label = new JLabel("Inverse");
        label.setForeground(Colors.slate100);
        add(label);
    }
}
