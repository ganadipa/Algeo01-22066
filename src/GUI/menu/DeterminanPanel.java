package GUI.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.MatrixInput;
import GUI.theme.Colors;
import GUI.theme.Fonts;
import Matrix.Matrix;

public class DeterminanPanel extends JPanel {
    JPanel titlePanel;
    JPanel resultPanel;

    JLabel titleLabel;
    JLabel answerLabel;

    public DeterminanPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titlePanel = new JPanel();
        titlePanel.setBackground(Colors.slate950);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel("Determinan");
        titleLabel.setForeground(Colors.slate100);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        titleLabel.setFont(Fonts.font20);
        titlePanel.add(titleLabel);
        add(titlePanel);



        MatrixInput matrixInput = new MatrixInput(3,3, true);
        add(matrixInput, BorderLayout.PAGE_START);

        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        answerLabel = new JLabel("Determinan: ");
        answerLabel.setForeground(Colors.slate100);
        answerLabel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.add(answerLabel);
        
        add(resultPanel, BorderLayout.PAGE_START);

        matrixInput.onValueChanged = () -> {
            try {
                Matrix matrix = matrixInput.getMatrix();
                answerLabel.setText("Determinan: "+matrix.getDeterminant());
                answerLabel.repaint();
                answerLabel.revalidate();
            }
            catch(Exception e) {
                answerLabel.setText("Determinan: "+e.getMessage());
            }
        };

    }

    
}
