package GUI.component;

import java.awt.Graphics;
import java.awt.Graphics2D;

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
        int lineWidth = 2;
        int upMargin = 42;
        g2.setColor(Colors.indigo400);
        int a = (getWidth() + hGap)/col;
        int posX = getWidth() - a;
        g2.fillRect(posX, upMargin, lineWidth, getHeight()-upMargin);
    }
}
