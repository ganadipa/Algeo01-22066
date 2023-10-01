package GUI.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

import GUI.theme.Colors;

public class MatrixAugmentedInput extends MatrixInput  {
    public MatrixAugmentedInput(int rows, int cols) {
        super(rows, cols, false);
        setOpaque(true);
    }
    @Override
    protected void paintChildren(Graphics grphcs) {
        super.paintChildren(grphcs);
        // setBackground(Colors.transparent);
        Graphics2D g2 = (Graphics2D) grphcs;
        int lineWidth = 4;
        int upMargin = 35;
        int leftPush = 2;
        g2.fillRect((getWidth() * (col-1)/col)-leftPush, upMargin, lineWidth, getHeight()-upMargin);
    }
}
