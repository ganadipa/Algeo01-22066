package GUI.component;

import GUI.event.EventMenuSelected;
import GUI.theme.Colors;
import GUI.theme.Fonts;
import GUI.swing.ListMenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.GroupLayout;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;



public class SideBar extends JPanel {

    private JLabel label;
    private ListMenu<String> listMenu;
    private JPanel leftPanel;

    public void addEventMenuSelected(EventMenuSelected event) {
        listMenu.addEventMenuSelected(event);
    }

    public SideBar() {
        initComponents();
        setOpaque(false);
        listMenu.setOpaque(false);
        init();
    }

    private void init() {
        listMenu.addItem("Sistem Persamaaan Linier");
        listMenu.addItem("Determinan");
        listMenu.addItem("Matriks balikan");
        listMenu.addItem("Interpolasi Polinom");
        listMenu.addItem("Interpolasi Bicubic Spline");
        listMenu.addItem("Regresi linier berganda");
        listMenu.addItem("Tentang");
}

    private void initComponents() {

        leftPanel = new JPanel();
        label = new JLabel();
        listMenu = new ListMenu<>();

        leftPanel.setOpaque(false);

        label.setFont(Fonts.font14); 
        label.setForeground(new Color(255, 255, 255));
        
        label.setIcon(null);
        label.setBorder(new EmptyBorder(0,12,0,0));
        label.setText("Sistem Persamaan Linear");

        GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(label)
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(listMenu, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(listMenu, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );
    }

    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setPaint(Colors.slate900);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
        super.paintChildren(grphcs);
    }

    private int x;
    private int y;

    public void initMoving(JFrame fram) {
        leftPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }

        });
        leftPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                fram.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }

    
}
