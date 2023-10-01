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
import java.util.List;

public class MatrixInterpolasiLinearInput extends MatrixInput {
    
    public MatrixInterpolasiLinearInput(int row) {
        super(row, 2, false);
        textFieldCol.setVisible(false);
    }

    void setMatrixSize(int newRow) {
        this.row = newRow;
        matrixPanel.removeAll();
        initMatrix();
        matrixPanel.repaint();
        matrixPanel.revalidate();
    }

    void initPanel () {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(inputPanel);
        add(matrixPanel);
    }

    void initMatrix() {
        GridLayout gl = new GridLayout(row+1, col);
        gl.setHgap(hGap);
        gl.setVgap(vGap);
        matrixPanel.setBorder(BorderFactory.createEmptyBorder(hGap,hGap,hGap,hGap));
        matrixPanel.setLayout(gl);
        inputFieldList.clear();

        // label
        JLabel labelX = new JLabel("x");
        labelX.setForeground(Colors.slate100);
        labelX.setBackground(Colors.slate950);
        labelX.setHorizontalAlignment(JLabel.CENTER);
        JLabel labelY = new JLabel("y");
        labelY.setForeground(Colors.slate100);
        labelX.setBackground(Colors.slate950);
        labelY.setHorizontalAlignment(JLabel.CENTER);
        matrixPanel.add(labelX);
        matrixPanel.add(labelY);

        for (int i = 0; i < row*col; i++) {
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
    
    
    void initInput() {
        textFieldLength.setText(this.row + "");

        textFieldLength.setBorder(BorderFactory.createEmptyBorder(7,10,7,10));
        textFieldLength.getDocument().addDocumentListener(new DocumentListener() {
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
                    int newRow = Integer.parseInt(textFieldLength.getText());
                    setMatrixSize(newRow, col);
                    onValueChanged.run();
                }
                catch(NumberFormatException e){

                }
            }
        });
        
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel rowLabel = new JLabel("Banyak Titik:");
        rowLabel.setForeground(Colors.slate100);

        inputPanel.add(rowLabel);
        inputPanel.add(textFieldLength);
    }
    
    void initMatrixSquareInput() {
        textFieldLength.setText(this.row + "");

        textFieldLength.setBorder(BorderFactory.createEmptyBorder(7,10,7,10));
        textFieldLength.getDocument().addDocumentListener(new DocumentListener() {
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
                    int newRowCol = Integer.parseInt(textFieldLength.getText());
                    setMatrixSize(newRowCol, newRowCol);
                    onValueChanged.run();
                }
                catch(NumberFormatException e){

                }
            }
        });
        
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel rowLabel = new JLabel("Baris dan Kolom:");
        rowLabel.setForeground(Colors.slate100);

        inputPanel.add(rowLabel);
        inputPanel.add(textFieldLength);
    }
    
    public Matrix getMatrix() {
        Matrix matrix = new Matrix(row, col);
        for (int i = 0; i < row*col; i++) {
            matrix.matrix[i/col][i%col] = Double.parseDouble(inputFieldList.get(i).getText());
        }
        return matrix;
    }
    public void setMatrix(Matrix matrix) {
        this.row = matrix.row;
        this.col = matrix.col;
        
        // Update text field for matrix sizw
        textFieldLength.setText(this.row + "");
        textFieldLength.repaint();
        textFieldLength.revalidate();

        matrixPanel.removeAll();
        initMatrix();
        for (int i = 0; i < row*col; i++) {
            inputFieldList.get(i).setText(matrix.matrix[i/col][i%col] + "");
        }
        matrixPanel.repaint();
        matrixPanel.revalidate();

    }
    
    public static void main(String[] args) {
        JFrame jf = new JFrame();

        MatrixInterpolasiLinearInput mi = new MatrixInterpolasiLinearInput(3);
        jf.add(mi);

        jf.pack();
        jf.setVisible(true);
    }

}
