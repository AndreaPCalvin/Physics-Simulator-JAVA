//Andrea 
package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	ControlPanel controlPanel;
	Controller _ctrl;
	StatusBar statusBarPanel;
	BodiesTable bodiesTable;
	Viewer viewer;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		controlPanel = new ControlPanel(_ctrl);
		statusBarPanel = new StatusBar(_ctrl);
		mainPanel.add(controlPanel , BorderLayout.PAGE_START);
		mainPanel.add(statusBarPanel, BorderLayout.PAGE_END);
		
		viewer=new Viewer(_ctrl);
		bodiesTable= new BodiesTable(_ctrl);
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.add(bodiesTable, BorderLayout.PAGE_START);//se come el viewer
		center.add(viewer, BorderLayout.PAGE_END);
		center.setVisible(true);
		mainPanel.add(center, BorderLayout.CENTER);
		
		
		this.setBounds(100, 100, 500, 500);
		this.setVisible(true);
	}
	// other private/protected methods
	// ...
}
