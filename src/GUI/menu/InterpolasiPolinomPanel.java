package GUI.menu;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Class.Solvable.Interpolasi;
import Class.Solvable.SPL;
import GUI.component.InputField;
import GUI.component.MatrixInput;
import GUI.component.MatrixInterpolasiLinearInput;
import GUI.theme.Colors;

public class InterpolasiPolinomPanel extends Menu {
    MatrixInterpolasiLinearInput matrixInput;

    JPanel xPanel = new JPanel();
    InputField xField = new InputField();

    JPanel resultPanel = new JPanel();
    JLabel answerLabel = new JLabel();

    Interpolasi interpolasi = new Interpolasi();

    public InterpolasiPolinomPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Interpolasi Polinom");

        matrixInput = new MatrixInterpolasiLinearInput(3);
        add(matrixInput);

        addText("Taksir x:");
        xPanel.setLayout(new GridLayout(1,2));
        xPanel.setBackground(Colors.slate950);
        xPanel.add(xField);
        add(xPanel);

        // File chooser
        add(fileChooserPanel);

        addText("Hasil:");
        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setForeground(Colors.slate100);
        answerLabel = new JLabel("");
        answerLabel.setBackground(Colors.slate950);
        answerLabel.setForeground(Colors.slate100);
        resultPanel.add(answerLabel);
        add(resultPanel);

        

        add(errorPanel);

        matrixInput.onValueChanged = () -> {
            try{
                interpolasi.setMatrix(matrixInput.getMatrix());
                
                interpolasi.solve();
                answerLabel.setText(stringToHtml(interpolasi.getSolutionString()));
                setResult(interpolasi.getSolutionString());
                answerLabel.repaint();
                answerLabel.revalidate();
                add(exportPanel);
                repaint();
                revalidate();
                resetError();
            }
            catch(Exception e){
                answerLabel.setText("");
                answerLabel.repaint();
                answerLabel.revalidate();
                onException(e);
                remove(exportPanel);
                repaint();
                revalidate();
            }
        };


        xField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent evt) {
                updateValue();
            }
            public void removeUpdate(DocumentEvent evt) {
                updateValue();
            }
            public void insertUpdate(DocumentEvent evt) {
                updateValue();
            }
            void updateValue() {
                try {
                    interpolasi.setX(Double.parseDouble(xField.getText()));
                
                    interpolasi.solve();
                    answerLabel.setText(stringToHtml(interpolasi.getSolutionString()));
                    setResult(interpolasi.getSolutionString());
                    answerLabel.repaint();
                    answerLabel.revalidate();
                    add(exportPanel);
                    repaint();
                    revalidate();
                    resetError();
                }
                catch(Exception e){
                    answerLabel.setText("");
                    answerLabel.repaint();
                    answerLabel.revalidate();
                    onException(e);
                    remove(exportPanel);
                    repaint();
                    revalidate();
                }
            }
        });
    }


    @Override
    public void onFileChoosen(File file) {
        try {
            interpolasi.setVariablesFromFile(file);
            matrixInput.setMatrix(interpolasi.getInputMatrix());
            xField.setText(interpolasi.getX() + "");
            matrixInput.onValueChanged.run();
            repaint();
            revalidate();
            resetError();
        }
        catch(Exception e) {
            answerLabel.setText("");
            answerLabel.repaint();
            answerLabel.revalidate();
            onException(e);
            remove(exportPanel);
            repaint();
            revalidate();
        }
    }
}
