package GUI.menu;

import java.awt.Color;

import javax.swing.JLabel;

import GUI.theme.Colors;

public class SPLPanel extends javax.swing.JPanel {

    public SPLPanel() {
        setBackground(Colors.transparent);
        JLabel label = new JLabel("Sistem Persamaan Linear");
        label.setForeground(Colors.slate100);
        add(label);
    }
}
