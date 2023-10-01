package GUI.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Cursor;

import GUI.theme.Colors;

public class SideBarItem extends JPanel {

    private boolean selected;
    private boolean over;
    private int arc = 20;
    private int margin = 20;

    private JLabel lbName;

    public SideBarItem(String menuName) {
        initComponents();
        setOpaque(false);
        lbName.setText(menuName);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public void setOver(boolean over) {
        this.over = over;
        repaint();
    }

    private void initComponents() {

        lbName = new javax.swing.JLabel();

        lbName.setForeground(Colors.slate100);
        lbName.setText("Menu Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25)
                .addComponent(lbName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbName, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (selected || over) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (selected) {
                g2.setColor(Colors.indigo400);
            } else {
                g2.setColor(Colors.slate800);
            }
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(margin/2, 0, getWidth()-margin, getHeight(), arc, arc);
            g2.fill(roundedRectangle);
        }
        super.paintComponent(grphcs);
        
    }

    
}
