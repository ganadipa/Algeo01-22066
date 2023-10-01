package GUI.menu;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import GUI.component.MatrixInput;
import GUI.theme.Colors;

public class SPLPanel extends Menu {
    MatrixInput matrixInput;

    public SPLPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Sistem Persamaan Linear");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
    }
}
