package GUI.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.MatrixInput;
import GUI.theme.Colors;
import Matrix.Matrix;

public class DeterminanPanel extends Menu {
    JPanel titlePanel;
    JPanel resultPanel;

    JLabel titleLabel;
    JLabel answerLabel;

    MatrixInput matrixInput;

    public DeterminanPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Determinan");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput, BorderLayout.PAGE_START);

        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        answerLabel = new JLabel("Determinan: ");
        answerLabel.setForeground(Colors.slate100);
        answerLabel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.add(answerLabel);

        // File chooser
        add(fileChooserPanel);
        //

        
        add(resultPanel, BorderLayout.PAGE_START);

        add(errorPanel);


        matrixInput.onValueChanged = () -> {
            try {
                Matrix matrix = matrixInput.getMatrix();
                double determinan = matrix.getDeterminant();
                answerLabel.setText("Determinan: "+determinan);
                answerLabel.repaint();
                answerLabel.revalidate();
                setResult(determinan + "");
                resetError();
                add(exportPanel);
                repaint();
                revalidate();
            }
            catch(Exception e) {
                answerLabel.setText("Determinan: ");
                answerLabel.repaint();
                answerLabel.revalidate();
                onError(e);
                remove(exportPanel);
                repaint();
                revalidate();
            }
        };

    }

    @Override
    public void onFileChoosen(File file) {
        try {
            matrixInput.importMatrixFromFile(file);
            matrixInput.repaint();
            matrixInput.revalidate();
            resetError();
        }
        catch(Exception e) {
            answerLabel.setText("Determinan: ");
            answerLabel.repaint();
            answerLabel.revalidate();
            onError(e);
            remove(exportPanel);
            repaint();
            revalidate();
        }
    }
    
}
