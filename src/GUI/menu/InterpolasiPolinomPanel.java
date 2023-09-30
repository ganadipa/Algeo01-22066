package GUI.menu;

import java.awt.Color;

import javax.swing.JLabel;

import GUI.theme.Colors;

public class InterpolasiPolinomPanel extends javax.swing.JPanel {

    public InterpolasiPolinomPanel() {
        setBackground(Colors.transparent);
        JLabel label = new JLabel("InterpolasiPolinom");
        label.setForeground(Colors.slate100);
        add(label);
    }
}
