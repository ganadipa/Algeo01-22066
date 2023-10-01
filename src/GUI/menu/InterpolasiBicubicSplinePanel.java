package GUI.menu;

import javax.swing.BoxLayout;

import GUI.component.MatrixInput;
import GUI.theme.Colors;

public class InterpolasiBicubicSplinePanel extends Menu {
    MatrixInput matrixInput;

    public InterpolasiBicubicSplinePanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Interpolasi Bicubic Spline");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
    }
}
