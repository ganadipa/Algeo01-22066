package GUI.component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Class.Matrix;
import GUI.theme.Colors;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MatrixInput extends JPanel {
    public int row = 3;
    public int col = 3;
    public boolean isMatrixSquare = false;

    public int hGap = 5;
    public int vGap = 3;
    public int inputWidth = 5;

    JPanel inputPanel = new JPanel();
    JPanel matrixPanel = new JPanel();
    InputField textFieldLength = new InputField(5);
    InputField textFieldCol = new InputField(5);

    public Runnable onValueChanged;

    List<InputField> inputFieldList = new ArrayList<InputField>();
    
    public MatrixInput(int row, int col, boolean isMatrixSquare) {
        this.isMatrixSquare = isMatrixSquare;
        setBackground(Colors.slate950);
        matrixPanel.setBackground(Colors.slate950);
        inputPanel.setBackground(Colors.slate950);

        this.row = row; this.col = col;
        
        initMatrix();

        if(isMatrixSquare) initMatrixSquareInput();
        else initInput();
        
        initPanel();
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
        textFieldLength.setText(this.row + "");
        textFieldCol.setText(this.col + "");

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
        inputPanel.add(textFieldLength);
        inputPanel.add(colLabel);
        inputPanel.add(textFieldCol);
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
        if(isMatrixSquare) {
            textFieldLength.setText(this.row + "");
            textFieldLength.repaint();
            textFieldLength.revalidate();
        }
        else {
            textFieldLength.setText(this.row + "");
            textFieldCol.setText(this.col + "");
            textFieldLength.repaint();
            textFieldLength.revalidate();
            textFieldCol.repaint();
            textFieldCol.revalidate();  
        }

        matrixPanel.removeAll();
        initMatrix();
        for (int i = 0; i < row*col; i++) {
            inputFieldList.get(i).setText(matrix.matrix[i/col][i%col] + "");
        }
        matrixPanel.repaint();
        matrixPanel.revalidate();

    }
    public void importMatrixFromFile(File file) throws Exception {
        Matrix matrix = new Matrix();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            bufferedReader.mark(10000);
            int row = 0, col = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {   
                int tmpCol = line.split(" ").length;
                if (tmpCol < col) {
                    continue;
                }
                col = Math.max(tmpCol, col);
                row += 1;
            }
            if(isMatrixSquare && (row != col)) throw new Exception("Matrix harus berbentuk persegi.");

            matrix.initMatrix(row, col);
            bufferedReader.reset();


            int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
                if (elmts.length == 0) continue;
                if (elmts.length < matrix.col) throw new IllegalArgumentException();
                for (int j = 0; j < elmts.length; j++)
                {
                    matrix.matrix[i][j] = Double.parseDouble(elmts[j]);
                }
                i++;
            }

            setMatrix(matrix);
        } catch (IOException e) {
            // Handle case saat file not found atau ada IO error.
            throw new Exception("File tidak ditemukan.");
        } catch (NumberFormatException e) {
            // Handle case saat ada nonnumeric di input.
            throw new Exception("Sepertinya terdapat suatu nonnumeric value di file Anda. Program berhenti.");
        } catch (IllegalArgumentException e) {
            // Jumlah elemen di setiap baris tidak konsisten.
            throw new Exception("Jumlah elemen pada setiap baris tidak konsisten.");
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void hideListSizeField() {
        inputPanel.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        MatrixInput mi = new MatrixInput(3, 3, false);
        jf.add(mi);

        jf.pack();
        jf.setVisible(true);
    }

}
