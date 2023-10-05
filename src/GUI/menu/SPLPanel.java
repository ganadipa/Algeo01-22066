package GUI.menu;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Class.Matrix;
import Class.Solvable.SPL;
import GUI.component.MatrixAugmentedInput;
import GUI.component.RadioButton;
import GUI.theme.Colors;

public class SPLPanel extends Menu {
    JPanel resultPanel;

    JLabel answerLabel;

    MatrixAugmentedInput matrixInput;
    SPL spl;

    public SPLPanel() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Sistem Persamaan Linear");

        matrixInput = new MatrixAugmentedInput(3,4);
        add(matrixInput);

        // File chooser
        add(fileChooserPanel);
        

        resultPanel = new JPanel();
        resultPanel.setBackground(Colors.slate950);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));



        // RadioButton
        JPanel radioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Gauss, GaussJordan, Inverse, Cramer
        RadioButton rb1 = new RadioButton("Gauss");
        RadioButton rb2 = new RadioButton("Gauss Jordan");
        RadioButton rb3 = new RadioButton("Inverse");
        RadioButton rb4 = new RadioButton("Cramer");
        rb1.setSelected(true);
        JLabel L1 = new JLabel("Pilih Metode: ");
        L1.setForeground(Colors.slate100);
        ButtonGroup G1 = new ButtonGroup();
        G1.add(rb1);
        G1.add(rb2);
        G1.add(rb3);
        G1.add(rb4);
        radioButtonPanel.add(L1);
        radioButtonPanel.add(rb1);
        radioButtonPanel.add(rb2);
        radioButtonPanel.add(rb3);
        radioButtonPanel.add(rb4);
        radioButtonPanel.setBackground(Colors.slate950);
        add(radioButtonPanel);
        rb1.addActionListener(e -> {
            spl.setMethod(SPL.SPLMethod.Gauss);
            matrixInput.onValueChanged.run();
        });
        rb2.addActionListener(e -> {
            spl.setMethod(SPL.SPLMethod.GaussJordan);
            matrixInput.onValueChanged.run();
        });
        rb3.addActionListener(e -> {
            spl.setMethod(SPL.SPLMethod.Inverse);
            matrixInput.onValueChanged.run();
        });
        rb4.addActionListener(e -> {
            spl.setMethod(SPL.SPLMethod.Cramer);
            matrixInput.onValueChanged.run();
        });
        // RadioButton end



        
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
                spl.setMatrix(matrix);
                spl.solve();

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
                answerLabel.setText("");
                answerLabel.repaint();
                answerLabel.revalidate();
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
            matrixInput.importMatrixFromFile(file);
            matrixInput.repaint();
            matrixInput.revalidate();
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
