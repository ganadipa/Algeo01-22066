package GUI.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import GUI.theme.Colors;
import Matrix.Matrix;

public class MatrixDisplay extends JPanel {
    Matrix matrix;

    public MatrixDisplay(Matrix newMatrix) {
        setLayout(new GridLayout(newMatrix.row, newMatrix.col));
        setBackground(Colors.slate950);
        setMatrix(newMatrix);
    }

    // reset dan repaint matrix yang ditampilkan
    public void setMatrix(Matrix newMatrix) {
        matrix = newMatrix;
        removeAll();
        setLayout(new GridLayout(matrix.row, matrix.col));
        for (int i = 0; i < matrix.row; i++) {
            for (int j = 0; j < matrix.col; j++) {
                JLabel label = new JLabel(String.format("%.3f", matrix.matrix[i][j]), SwingConstants.CENTER);
                label.setForeground(Colors.slate100);
                // label.setAlignmentX(CENTER_ALIGNMENT);
                add(label);
            }
        }
        repaint();
        revalidate();
    }

    @Override
    protected void paintChildren(Graphics grphcs) {
        super.paintChildren(grphcs);
        // setBackground(Colors.transparent);
        Graphics2D g2 = (Graphics2D) grphcs;
        setBorder(new EmptyBorder(0,15,0,0));
        setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15)); // Add padding

        // draw matrix border
        int lineWidth = 2;
        int margin = 5;
        int lineSmallLength = 8;
        g2.setColor(Colors.indigo400);
        //left
        g2.fillRect(margin, 0, lineWidth, getHeight()-lineWidth);
        //right
        g2.fillRect(getWidth()-lineWidth-margin, 0, lineWidth, getHeight()-lineWidth);

        //top left
        g2.fillRect(margin, 0, lineSmallLength, lineWidth);
        //bottom left
        g2.fillRect(margin, getHeight()-lineWidth, lineSmallLength, lineWidth);
        //top right
        g2.fillRect(getWidth()-margin-lineSmallLength, 0, lineSmallLength, lineWidth);
        //bottom right
        g2.fillRect(getWidth()-margin-lineSmallLength, getHeight()-lineWidth, lineSmallLength, lineWidth);
    }
}
