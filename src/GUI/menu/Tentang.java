package GUI.menu;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import GUI.component.MatrixInput;
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
