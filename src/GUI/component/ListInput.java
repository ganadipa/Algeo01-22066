package GUI.component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import GUI.theme.Colors;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.util.List;

public class ListInput extends JPanel {
    public int length = 3;
    public boolean isMatrixSquare = false;

    public int hGap = 5;
    public int vGap = 3;
    public int inputWidth = 5;

    JPanel inputPanel = new JPanel();
    JPanel listPanel = new JPanel();
    public InputField textFieldLength = new InputField(5);

    public Runnable onValueChanged;

    public List<InputField> inputFieldList = new ArrayList<InputField>();
    
    public ListInput(int length) {
        setBackground(Colors.slate950);
        listPanel.setBackground(Colors.slate950);
        inputPanel.setBackground(Colors.slate950);

        this.length = length;
        
        initList();

        initInput();
        
        initPanel();
    }

    public void setListSize(int newRow) {
        this.length = newRow;
        listPanel.removeAll();
        initList();
        listPanel.repaint();
        listPanel.revalidate();
    }

    void initPanel () {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(inputPanel);
        add(listPanel);
    }

    void initList() {
        GridLayout gl = new GridLayout(2, length);
        gl.setHgap(hGap);
        gl.setVgap(vGap);
        listPanel.setBorder(BorderFactory.createEmptyBorder(hGap,hGap,hGap,hGap));
        listPanel.setLayout(gl);
        inputFieldList.clear();

        for(int i = 0; i < length; i++) {
            // label x1, x2, x3 untuk taksiran
            JLabel label = new JLabel("x" + (i+1));
            label.setForeground(Colors.slate100);
            label.setBackground(Colors.slate950);
            listPanel.add(label);
        }
        for (int i = 0; i < length; i++) {
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
            listPanel.add(textField);
        }
    }
    
    
    void initInput() {
        textFieldLength.setText(this.length + "");

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
                    setListSize(newRow);
                    onValueChanged.run();
                }
                catch(NumberFormatException e){

                }
            }
        });
        
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lengthLabel = new JLabel("Taksiran:");
        lengthLabel.setForeground(Colors.slate100);

        inputPanel.add(lengthLabel);
        inputPanel.add(textFieldLength);
    }
    
    
    public double[] getList() {
        double[] list = new double[length];
        for (int i = 0; i < length; i++) {
            list[i] = Double.parseDouble(inputFieldList.get(i).getText());
        }
        return list;
    }
    public void setList(double[] list) {
        this.length = list.length;
        
        // Update text field for matrix size
        textFieldLength.setText(this.length + "");
        textFieldLength.repaint();
        textFieldLength.revalidate();

        listPanel.removeAll();
        initList();
        for (int i = 0; i < length; i++) {
            inputFieldList.get(i).setText(list[i] + "");
        }
        listPanel.repaint();
        listPanel.revalidate();

    }
    
    public void hideMatrixSizeField() {
        inputPanel.setVisible(false);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        ListInput mi = new ListInput(3);
        jf.add(mi);

        jf.pack();
        jf.setVisible(true);
    }

}
