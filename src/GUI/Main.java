package GUI;

import GUI.event.EventMenuSelected;
import GUI.menu.DeterminanPanel;
import GUI.menu.InterpolasiBicubicSplinePanel;
import GUI.menu.BalikanPanel;
import GUI.menu.InterpolasiPolinomPanel;
import GUI.menu.RegresiLinierBergandaPanel;
import GUI.menu.SPLPanel;
import GUI.menu.Tentang;
import GUI.theme.Colors;
import GUI.component.SideBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class Main {

    private static SPLPanel home = new SPLPanel();
    private static DeterminanPanel menu1 = new DeterminanPanel();
    private static BalikanPanel menu2 = new BalikanPanel();
    private static InterpolasiPolinomPanel menu3 = new InterpolasiPolinomPanel();
    private static InterpolasiBicubicSplinePanel menu4 = new InterpolasiBicubicSplinePanel();
    private static RegresiLinierBergandaPanel menu5 = new RegresiLinierBergandaPanel();
    private static Tentang menu6 = new Tentang();

    private static JPanel mainPanel = new JPanel();
    private static SideBar menu = new SideBar();

    private static void setMenu(JComponent com) {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    
    public static void main(String args[]) {
        
        JFrame f = new JFrame("Sistem Persamaan Linear");

        menu.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if (index == 0) {
                    setMenu(home);
                } else if (index == 1) {
                    setMenu(menu1);
                } else if (index == 2) {
                    setMenu(menu2);
                } else if (index == 3) {
                    setMenu(menu3);
                } else if (index == 4) {
                    setMenu(menu4);
                } else if (index == 5) {
                    setMenu(menu5);
                } else if (index == 6) {
                    setMenu(menu6);
                }
            }
        });
        //  set when system open start with home form
        setMenu(new SPLPanel());

        mainPanel.setBackground(Colors.slate950);

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        f.add(menu, BorderLayout.WEST);


        // Add scroll when overflow
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        f.getContentPane().add(scrollPane, BorderLayout.CENTER);


        f.setSize(1280, 720);

        f.setVisible(true);
    }
    
}
