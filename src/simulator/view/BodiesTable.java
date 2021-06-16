package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {
	BodiesTable(Controller ctrl) {
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Bodies",
				TitledBorder.LEFT, TitledBorder.TOP));
		
		BodiesTableModel modelo = new BodiesTableModel(ctrl);
		JTable tabla = new JTable(modelo);
		this.add(new JScrollPane(tabla));
	}
}
