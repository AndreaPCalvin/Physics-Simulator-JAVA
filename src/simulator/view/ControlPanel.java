package simulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private boolean _stopped;
	private String defaultLaw = "nlug";
	
	private JButton exitButton;
	private JButton openButton;
	private JButton physicsButton;
	private JButton runButton;
	private JButton stopButton;
	private JLabel pasos = new JLabel("Steps: ");
	private JSpinner steps;
	private JLabel delta = new JLabel("Delta-Time: ");
	private JTextField deltaTimeField;
	private JToolBar toolbar;
	private JFileChooser fc;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		fc=new JFileChooser();
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		toolbar = new JToolBar();
		openButton();
		toolbar.addSeparator();
		physicsButton();
		toolbar.addSeparator();
		runButton();
		toolbar.addSeparator();
		stopButton();
		toolbar.addSeparator();
		spiner();
		toolbar.addSeparator();
		delta();
		toolbar.addSeparator();
		exitButton();
		
		toolbar.setVisible(true);
		this.add(toolbar);
		this.setVisible(true);
	}

	private void openButton() {
		openButton = new JButton();
		openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		openButton.setToolTipText("choose file");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//se abre por defecto en el directorio HOME
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int v = fc.showOpenDialog(null);
				if (v==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();  // obtenemos el fichero seleccionado
					System.out.println("loading " +file.getName());
					
					_ctrl.reset();
					
					try {					
						_ctrl.loadBodies(new FileInputStream(file));
					} catch (FileNotFoundException e1) {
						//no va a ocurrir porque file es escogido de los existentes en el dispositivo
						System.out.println("File not found.");
					}
				}
				else {//opcion cancelada, no se hace reset ni se cragan ficheros
					System.out.println("load cancelled by user");
				}	
			}
		});
		openButton.setVisible(true);
		openButton.setEnabled(true);
		toolbar.add(openButton);
	}

	private void physicsButton() {
		physicsButton = new JButton();
		physicsButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		physicsButton.setToolTipText("choose law");
		physicsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Object[] leyes =  new Object[_ctrl.getGravityLawsFactory().getInfo().size()];
				JSONObject[] jleyes =  new JSONObject[_ctrl.getGravityLawsFactory().getInfo().size()];
				int index = 0;
				for(int i=0; i<leyes.length; i++) {
					jleyes[i] = _ctrl.getGravityLawsFactory().getInfo().get(i);
					String d = _ctrl.getGravityLawsFactory().getInfo().get(i).get("desc").toString();
					String t = _ctrl.getGravityLawsFactory().getInfo().get(i).get("type").toString();
					leyes[i] = d +" (" + t + ")";
					if(t.equals(defaultLaw)) index=i;
				}
				
				String n = (String) JOptionPane.showInputDialog(new JFrame(), 
						"Select gravity laws to be used:",  
						"Gravity Laws Selector",  
						JOptionPane.QUESTION_MESSAGE,
						null, leyes, leyes[index]);
				
				for(int i=0; i<leyes.length; i++) {
					if(n.equals(leyes[i])) {
						_ctrl.setGravityLaws(jleyes[i]);
					}
				}
				
			}
		});
		physicsButton.setVisible(true);
		physicsButton.setEnabled(true);
		toolbar.add(physicsButton);
	}
	
	private void runButton() {
		runButton = new JButton();
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setToolTipText("play");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enableButtons(false);
				_stopped=false;
				
				//_ctrl.setDeltaTime(deltaTimeField);
				//run_sim(steps);
			}			
		});
		runButton.setVisible(true);
		runButton.setEnabled(true);
		toolbar.add(runButton);
	}
	
	private void stopButton() {
		stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("stop");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
			
		});
		stopButton.setVisible(true);
		runButton.setEnabled(true);
		toolbar.add(stopButton);
	}

	private void spiner() {
		steps = new JSpinner();
		toolbar.add(pasos);
		toolbar.add(steps);
	}
	
	private void delta() {
		toolbar.add(delta);
		deltaTimeField = new JTextField(5);
	//	deltaTimeField.setEditable(true);
		toolbar.add(deltaTimeField);
	}

	private void exitButton() {
		exitButton = new JButton();
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.setToolTipText("exit");
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(new JFrame(),"Are sure you want to quit?", 
						"Quit",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null, null);
				if (n == 0) System.exit(0); 
			}
			
		});
		exitButton.setVisible(true);
		exitButton.setEnabled(true);
		toolbar.add(exitButton);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				// TODO show the error in a dialog box
				// TODO enable all buttons
				_stopped = true;
				enableButtons(true);
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			_stopped = true;
			// TODO enable all buttons
			enableButtons(true);
		}
	}
	
	private void enableButtons(boolean b) {
		openButton.setEnabled(b);
		physicsButton.setEnabled(b);
		runButton.setEnabled(b);
		exitButton.setEnabled(b);		
	}

	// SimulatorObserver methods

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}
}
