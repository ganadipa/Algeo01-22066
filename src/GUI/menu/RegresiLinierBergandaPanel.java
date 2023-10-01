package GUI.menu;

import javax.swing.BoxLayout;

import GUI.component.MatrixInput;
import GUI.theme.Colors;

public class RegresiLinierBergandaPanel extends Menu {
    MatrixInput matrixInput;

    public RegresiLinierBergandaPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Regresi Linier Berganda");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
    }
}
