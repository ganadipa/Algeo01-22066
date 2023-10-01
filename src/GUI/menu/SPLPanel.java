package GUI.menu;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.MatrixInput;
import GUI.theme.Colors;
import Matrix.Matrix;
import Matrix.SPL;
import Matrix.SPL.SPLMethod;

public class SPLPanel extends Menu {
    JPanel resultPanel;

    JLabel answerLabel;

    MatrixInput matrixInput;
    SPL spl;
    SPLMethod method = SPLMethod.GaussJordan;

    public SPLPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Sistem Persamaan Linear");

        matrixInput = new MatrixInput(3,3, false);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
        

        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        addText("Hasil: ");

        answerLabel = new JLabel("");
        answerLabel.setForeground(Colors.slate100);
        answerLabel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.add(answerLabel);

        add(resultPanel);

        add(errorPanel);


        matrixInput.onValueChanged = () -> {
            try {
                Matrix matrix = matrixInput.getMatrix();
                spl = new SPL(matrix.row, matrix.col-1);
                spl.setMethod(method);
                spl.setMatrix(matrix);
                spl.solve(false);

                answerLabel.setText(stringToHtml(spl.getSolutionString()));
                setResult(spl.getSolutionString());
                answerLabel.repaint();
                answerLabel.revalidate();

                // all menu have this
                // setResult(determinan + "");
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
