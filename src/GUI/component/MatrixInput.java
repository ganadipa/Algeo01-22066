package GUI.component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import GUI.theme.Colors;
import Matrix.Matrix;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.List;

public class MatrixInput extends JPanel {
    public int row = 3;
    public int col = 3;
    boolean isMatrixSquare = false;

    public int hGap = 5;
    public int vGap = 3;
    public int inputWidth = 5;

    JPanel inputPanel = new JPanel();
    JPanel matrixPanel = new JPanel();
    InputField textFieldRow = new InputField(5);
    InputField textFieldCol = new InputField(5);

    public Runnable onValueChanged;

    List<InputField> inputFieldList = new java.util.ArrayList<InputField>();
    

    public MatrixInput(int row, int col) {
        setBackground(Colors.slate950);
        matrixPanel.setBackground(Colors.slate950);
        inputPanel.setBackground(Colors.slate950);

        this.row = row; this.col = col;
        
        initMatrix();

        if(!isMatrixSquare) initMatrixSquareInput();
        else initInput();
        
        initPanel();
    }

    public MatrixInput(int row, int col, boolean isMatrixSquare) {
        this(row, col);
        this.isMatrixSquare = isMatrixSquare;
    }

    void setMatrixSize(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
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
        GridLayout gl = new GridLayout(row, col);
        gl.setHgap(hGap);
        gl.setVgap(vGap);
        matrixPanel.setBorder(BorderFactory.createEmptyBorder(hGap,hGap,hGap,hGap));
        matrixPanel.setLayout(gl);
        inputFieldList.clear();
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

        JLabel rowLabel = new JLabel("Baris:");
        rowLabel.setForeground(Colors.slate100);
        JLabel colLabel = new JLabel("Kolom:");
        colLabel.setForeground(Colors.slate100);

        inputPanel.add(rowLabel);
        inputPanel.add(textFieldRow);
        inputPanel.add(colLabel);
        inputPanel.add(textFieldCol);
    }
    
    void initMatrixSquareInput() {
        textFieldRow.setText(this.row + "");

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
                    int newRowCol = Integer.parseInt(textFieldRow.getText());
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
        inputPanel.add(textFieldRow);
    }
    
    public Matrix getMatrix() {
        Matrix matrix = new Matrix(row, col);
        for (int i = 0; i < row*col; i++) {
            matrix.matrix[i/col][i%col] = Double.parseDouble(inputFieldList.get(i).getText());
        }
        return matrix;
    }
    
    public static void main(String[] args) {
        JFrame jf = new JFrame();

        MatrixInput mi = new MatrixInput(3, 3);
        jf.add(mi);

        jf.pack();
        jf.setVisible(true);
    }

}
