package GUI.menu;

import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.component.ListInput;
import GUI.component.MatrixInput;
import GUI.theme.Colors;
import Matrix.BicubicSplineInterpolation;

public class InterpolasiBicubicSplinePanel extends Menu {
    MatrixInput matrixInput;
    ListInput listInput;

    JPanel answerPanel = new JPanel();
    JLabel answerLabel = new JLabel("");

    BicubicSplineInterpolation bsi = new BicubicSplineInterpolation();

    public InterpolasiBicubicSplinePanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Interpolasi Bicubic Spline");
        addText("Matrix 4x4: ");
        matrixInput = new MatrixInput(4,4, false);
        matrixInput.hideListSizeField();
        add(matrixInput);

        addText("Taksir f(x1,x2): ");
        listInput = new ListInput(2);
        listInput.hideMatrixSizeField();
        add(listInput);

        // File chooser
        add(fileChooserPanel);

        addText("Hasil:");

        answerPanel.setBackground(Colors.slate950);
        answerPanel.add(answerLabel);
        answerLabel.setForeground(Colors.slate100);
        answerLabel.setBackground(Colors.slate950);
        add(answerPanel);

        add(errorPanel);

        matrixInput.onValueChanged = () -> {
            bsi.setInputMatrix(matrixInput.getMatrix());
            tryAnswer();   
        };

        listInput.onValueChanged = () -> {
            tryAnswer();
        };
    }
    void tryAnswer() {
        try {
            bsi.setX(listInput.getList());
            bsi.solve(); // here's the problem
            
            answerLabel.setText(stringToHtml(bsi.getSolutionString()));
            answerLabel.repaint();
            answerLabel.revalidate();
            setResult(bsi.getSolutionString());
            resetError();
            add(exportPanel);
            repaint();
            revalidate();
        }
        catch(Exception e) {
            answerLabel.setText("");
            answerLabel.repaint();
            answerLabel.revalidate();
            onError(e);
            remove(exportPanel);
            repaint();
            revalidate();
        }
    }

    @Override
    public void onFileChoosen(File file) {
        try {
            bsi.setVariablesFromFile(file);
            matrixInput.setMatrix(bsi.getInputMatrix());
            listInput.setList(bsi.getX());
            matrixInput.repaint();
            matrixInput.revalidate();
            resetError();
        }
        catch(Exception e) {
            answerLabel.setText("");
            answerLabel.repaint();
            answerLabel.revalidate();
            onError(e);
            remove(exportPanel);
            repaint();
            revalidate();
        }
    }
}
