package GUI.menu;

import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Class.Solvable.MultipleLinearRegression;
import GUI.component.ListInput;
import GUI.component.MatrixRegresiLinearBergandaInput;
import GUI.theme.Colors;

public class RegresiLinierBergandaPanel extends Menu {
    MatrixRegresiLinearBergandaInput matrixInput;
    ListInput listInput = new ListInput(3);
    JLabel answerLabel = new JLabel();
    JPanel answerPanel = new JPanel();
    MultipleLinearRegression mlr = new MultipleLinearRegression();
    public RegresiLinierBergandaPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Regresi Linier Berganda");

        matrixInput = new MatrixRegresiLinearBergandaInput(3,3, false);
        add(matrixInput);

        listInput = new ListInput(matrixInput.col);
        listInput.textFieldLength.setVisible(false);
        add(listInput);


        // File chooser
        add(fileChooserPanel);
        addText("Hasil:");

        answerLabel.setForeground(Colors.slate100);
        answerPanel.setBackground(Colors.slate950);
        answerPanel.add(answerLabel);
        add(answerPanel);
        // answerLabel.setVisible(true);

        add(errorPanel);

        matrixInput.onValueChanged = () -> {
            try {
                listInput.setListSize(matrixInput.col);
                listInput.repaint();
                listInput.revalidate();
                answerLabel.setText("");
                answerLabel.repaint();
                answerLabel.revalidate();
            }
            catch(Exception e) {
                answerLabel.setText("");
                answerLabel.repaint();
                answerLabel.revalidate();

                // all menu have this
                onException(e);
                remove(exportPanel);
                repaint();
                revalidate();
                exportPanel.setVisible(false);
            }
        };
    
        listInput.onValueChanged = () -> {
            try {
                mlr.setX(listInput.getList());
                mlr.setMatrix(matrixInput.getMatrix());
                mlr.setupMlrMatrix();

                mlr.solve();
                answerLabel.setText(stringToHtml(mlr.getSolutionString()));
                answerLabel.repaint();
                answerLabel.revalidate();

                // all menu have this
                setResult(mlr.getSolutionString());
                resetError();
                repaint();
                revalidate();
                add(exportPanel);
            }
            catch(Exception e) {
                answerLabel.setText("");
                answerLabel.repaint();
                answerLabel.revalidate();

                // all menu have this
                onException(e);
                remove(exportPanel);
                repaint();
                revalidate();
            }
        };
    }


    @Override
    public void onFileChoosen(File file) {
        try {
            mlr.setVariablesFromFile(file);
            matrixInput.setMatrix(mlr.matrix);
            matrixInput.repaint();
            matrixInput.revalidate();
            // listInput.setListSize(matrixInput.col+1);
            listInput.setList(mlr.getX());
            resetError();

            listInput.onValueChanged.run();

        }
        catch(Exception e) {
            answerLabel.setText("");
            answerLabel.repaint();
            answerLabel.revalidate();
            onException(e);
            remove(exportPanel);
            repaint();
            revalidate();
            exportPanel.setVisible(false);
        }
    }

    

}
