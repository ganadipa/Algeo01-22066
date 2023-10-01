package GUI.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.MatrixDisplay;
import GUI.component.MatrixInput;
import GUI.component.RadioButton;
import GUI.theme.Colors;
import Matrix.Matrix;

public class BalikanPanel extends Menu {

    JPanel resultPanel;
    JPanel answerPanel;

    JLabel resultLabel;

    MatrixDisplay matrixDisplay;
    MatrixInput matrixInput;

    public BalikanPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Matrix Balikan");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput, BorderLayout.PAGE_START);

        add(fileChooserPanel);

        // tempat tulisan Inverse:
        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // tempat matrix hasil inverse
        answerPanel = new JPanel();
        answerPanel.setBackground(Colors.slate950);
        answerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // tulisan Inverse:
        resultLabel = new JLabel("Inverse: ");
        resultLabel.setForeground(Colors.slate100);
        resultLabel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.add(resultLabel);

        add(resultPanel);

        matrixDisplay = new MatrixDisplay(new Matrix(3,3));
        matrixDisplay.setVisible(false);
        add(matrixDisplay);

        add(errorPanel);
        

        // Langkah
        // addText("Langkah: ");
        // addText("<html>Hello World!<br/>blahblahblah</html>");
        
        // RadioButton
        // JPanel radioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // RadioButton jRadioButton1 = new RadioButton("Gauss Jordan");
        // jRadioButton1.setSelected(true);
        // RadioButton jRadioButton2 = new RadioButton("Adjoin");
        // JLabel L1 = new JLabel("Pilih Metode: ");
        // L1.setForeground(Colors.slate100);
        // ButtonGroup G1 = new ButtonGroup();
        // G1.add(jRadioButton1);
        // G1.add(jRadioButton2);
        // radioButtonPanel.add(L1);
        // radioButtonPanel.add(jRadioButton1);
        // radioButtonPanel.add(jRadioButton2);
        // radioButtonPanel.setBackground(Colors.slate950);
        // add(radioButtonPanel);
        // RadioButton end

        // addText("Langkah: ");


        matrixInput.onValueChanged = () -> {
            try {
                Matrix inverseMatrix = matrixInput.getMatrix().getInverse();
                matrixDisplay.setMatrix(inverseMatrix);
                resultLabel.setText("Inverse: ");
                matrixDisplay.setVisible(true);

                // all menu have this
                setResult(inverseMatrix.getMatrixString() + "");
                resetError();
                add(exportPanel);
                repaint();
                revalidate();

            }
            catch(Exception e) {
                matrixDisplay.setMatrix(new Matrix(matrixInput.row, matrixInput.col));
                matrixDisplay.setVisible(false);
                onError(e);
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
            resultLabel.setText("Inverse: ");
            resultLabel.repaint();
            resultLabel.revalidate();
            matrixDisplay.setVisible(false);
            onError(e);
            remove(exportPanel);
            repaint();
            revalidate();
        }
    }
}
