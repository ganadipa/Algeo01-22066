package GUI.component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import GUI.theme.Colors;
import Matrix.Matrix;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MatrixRegresiLinearBergandaInput extends MatrixInput {

    // row = row, col = col+1
    // pas di display, rownya row+1
    public MatrixRegresiLinearBergandaInput(int row, int col, boolean isMatrixSquare) {
        super(row, col, isMatrixSquare); // perhatikan col+1
    }
    @Override
    void initMatrix() {
        GridLayout gl = new GridLayout(row+1, col);
        gl.setHgap(hGap);
        gl.setVgap(vGap);
        matrixPanel.setBorder(BorderFactory.createEmptyBorder(hGap,hGap,hGap,hGap));
        matrixPanel.setLayout(gl);
        inputFieldList.clear();
        for (int i = 0; i < (row+1)*(col+1); i++) { // perhatikan row+1
            // baris pertama cuma label x1, x2, x3..., y
            if(i < col) {
                JLabel label = new JLabel("x" + (i+1));
                label.setForeground(Colors.slate100);
                matrixPanel.add(label);
                continue;
            } else if(i == col) {
                JLabel label = new JLabel("y");
                label.setForeground(Colors.slate100);
                matrixPanel.add(label);
                continue;
            }


            InputField textField = new InputField(inputWidth);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setCaretColor(Colors.slate100);
            
            textField.getDocument().addDocumentListener(new DocumentListener() {
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
                        onValueChanged.run();
                    }
                    catch(NumberFormatException e){
                    }
                }
            });
            
            
            
            inputFieldList.add(textField);
            matrixPanel.add(textField);
        }
    }
    
    @Override
    void initInput() {
        textFieldRow.setText(this.row + "");
        textFieldCol.setText(this.col + "");

        textFieldRow.setBorder(BorderFactory.createEmptyBorder(7,10,7,10));
        textFieldRow.getDocument().addDocumentListener(new DocumentListener() {
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
                    int newRow = Integer.parseInt(textFieldRow.getText());
                    setMatrixSize(newRow, col);
                    onValueChanged.run();
                }
                catch(NumberFormatException e){

                }
            }
        });
        
        textFieldCol.setBorder(BorderFactory.createEmptyBorder(7,10,7,10));
        textFieldCol.getDocument().addDocumentListener(new DocumentListener() {
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
                    int newCol = Integer.parseInt(textFieldCol.getText());
                    setMatrixSize(row,newCol);
                    onValueChanged.run();
                }
                catch(NumberFormatException e){

                }
            }
        });
        
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel rowLabel = new JLabel("Banyak Sampel:");
        rowLabel.setForeground(Colors.slate100);
        JLabel colLabel = new JLabel("Banyak Peubah x:");
        colLabel.setForeground(Colors.slate100);

        inputPanel.add(rowLabel);
        inputPanel.add(textFieldRow);
        inputPanel.add(colLabel);
        inputPanel.add(textFieldCol);
    }
    
    @Override
    public void setMatrix(Matrix matrix) {
        this.row = matrix.row;
        this.col = matrix.col-1;

        // Update text field for matrix size
        textFieldRow.setText(this.row + "");
        textFieldCol.setText(this.col + "");
        textFieldRow.repaint();
        textFieldRow.revalidate();
        textFieldCol.repaint();
        textFieldCol.revalidate(); 

        matrixPanel.removeAll();
        initMatrix();

        for (int i = 0; i < row*(col+1); i++) {
            inputFieldList.get(i).setText(matrix.matrix[i/(col+1)][i%(col+1)] + "");
        }
        matrixPanel.repaint();
        matrixPanel.revalidate();
        this.col += 1;
    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();

        MatrixRegresiLinearBergandaInput mi = new MatrixRegresiLinearBergandaInput(3, 3, false);
        jf.add(mi);

        jf.pack();
        jf.setVisible(true);
    }

}
