package GUI.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.MatrixInput;
import GUI.component.RadioButton;
import GUI.theme.Colors;
import Matrix.Matrix;

public class DeterminanPanel extends Menu {
    JPanel resultPanel;

    JLabel answerLabel;

    MatrixInput matrixInput;
    Matrix.DeterminantMethod determinantMethod = Matrix.DeterminantMethod.CofactorExpansion;

    public DeterminanPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Determinan");

        matrixInput = new MatrixInput(3,3, true);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
        

        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        answerLabel = new JLabel("Determinan: ");
        answerLabel.setForeground(Colors.slate100);
        answerLabel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.add(answerLabel);


        // RadioButton
        JPanel radioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        RadioButton jRadioButton1 = new RadioButton("Reduksi Baris");
        jRadioButton1.setSelected(true);
        RadioButton jRadioButton2 = new RadioButton("Ekspansi Kofaktor");
        JLabel L1 = new JLabel("Pilih Metode: ");
        L1.setForeground(Colors.slate100);
        ButtonGroup G1 = new ButtonGroup();
        G1.add(jRadioButton1);
        G1.add(jRadioButton2);
        radioButtonPanel.add(L1);
        radioButtonPanel.add(jRadioButton1);
        radioButtonPanel.add(jRadioButton2);
        radioButtonPanel.setBackground(Colors.slate950);
        add(radioButtonPanel);
        jRadioButton1.addActionListener(e -> {
            determinantMethod = Matrix.DeterminantMethod.RowReduction;
            matrixInput.onValueChanged.run();
        });
        jRadioButton2.addActionListener(e -> {
            determinantMethod = Matrix.DeterminantMethod.CofactorExpansion;
            matrixInput.onValueChanged.run();
        });
        // RadioButton end



        add(resultPanel, BorderLayout.PAGE_START);

        add(errorPanel);


        matrixInput.onValueChanged = () -> {
            try {
                Matrix matrix = matrixInput.getMatrix();
                double determinan = matrix.getDeterminant(determinantMethod);
                answerLabel.setText("Determinan: "+determinan);
                answerLabel.repaint();
                answerLabel.revalidate();

                // all menu have this
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
