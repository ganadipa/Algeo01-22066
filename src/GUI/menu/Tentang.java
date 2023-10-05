package GUI.menu;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import GUI.theme.Colors;

public class Tentang extends Menu {

    JLabel tentangLabel = new JLabel();

    public Tentang() {
        setBackground(Colors.transparent);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addBigText("Tentang");
        addBigText("Sistem Persamaan Linear");
        // addText("Tugas Besar Aljabar Linier dan Geometri");
        // addText("Teknik Informatika ITB angkatan 2022");
        addText("<html>Tugas Besar Aljabar Linier dan Geometri<br/>Teknik Informatika ITB angkatan 2022</html>");
        
        // Link
        JLabel hyperlink = new JLabel("Visit CodeJava");
        hyperlink.setOpaque(true);
        hyperlink.setHorizontalAlignment(JLabel.LEFT);
        hyperlink.setForeground(Colors.indigo600);
        hyperlink.setBackground(Colors.slate950);
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlink.setText("Lihat repository di GitHub");
        hyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/ganadipa/Algeo01-22066"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setForeground(Colors.indigo800);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setForeground(Colors.indigo600);
            }
        });
        JPanel hyperlinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hyperlinkPanel.setBackground(Colors.slate950);
        hyperlinkPanel.add(hyperlink);
        add(hyperlinkPanel);
        // Link end


        addText("""
<html>
Anggota<br/>
- 13522066 Nyoman Ganadipa Narayana<br/>
- 13522084 Dhafin Fawwaz Ikramullah<br/>
- 13522117 Mesach Harmasendro
</html>""");

        tentangLabel.setForeground(Colors.slate100);
        add(tentangLabel);
    }
}
