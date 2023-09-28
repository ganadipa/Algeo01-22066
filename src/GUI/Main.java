package GUI;

import GUI.event.EventMenuSelected;
import GUI.menu.DeterminanPanel;
import GUI.menu.BalikanPanel;
import GUI.menu.InterpolasiPolinomPanel;
import GUI.menu.SPLPanel;
import GUI.theme.Colors;
import GUI.component.SideBar;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.concurrent.Flow;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;


public class Main {

    private static SPLPanel home;
    private static DeterminanPanel form1;
    private static BalikanPanel form2;
    private static InterpolasiPolinomPanel form3;

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

        home = new SPLPanel();
        form1 = new DeterminanPanel();
        form2 = new BalikanPanel();
        form3 = new InterpolasiPolinomPanel();
        menu.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if (index == 0) {
                    setMenu(home);
                } else if (index == 1) {
                    setMenu(form1);
                } else if (index == 2) {
                    setMenu(form2);
                } else if (index == 3) {
                    setMenu(form3);
                }
            }
        });
        //  set when system open start with home form
        setMenu(new SPLPanel());

        mainPanel.setBackground(Colors.slate950);

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        f.add(menu, BorderLayout.WEST);
        f.add(mainPanel, BorderLayout.CENTER);


        f.setSize(1280, 720);
        f.setVisible(true);
    }
    
}
