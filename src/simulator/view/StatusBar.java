package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	
	private JLabel tiempo = new JLabel("Time: ");
	private JLabel cuerpos = new JLabel("Bodies: ");
	private JLabel ley = new JLabel("Laws: ");
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies

	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// complete the code to build the tool bar
		this.add(tiempo);
		_currTime = new JLabel("0.0");
		this.add(_currTime);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(cuerpos);
		_numOfBodies = new JLabel("0");
		this.add(_numOfBodies);
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(ley);
		_currLaws = new JLabel("N/A");
		this.add(_currLaws);
	}

	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		/*_numOfBodies.setText(Integer.toString(bodies.size()));
		_currTime.setText(Double.toString(time));
		_currLaws.setText(gLawsDesc);*/
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		/*_numOfBodies.setText(Integer.toString(bodies.size()));
		_currTime.setText(Double.toString(time));
		_currLaws.setText(gLawsDesc);*/
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		//_numOfBodies.setText(Integer.toString(bodies.size()));
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_currTime.setText(Double.toString(time));
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		//no pertenece a status bar
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		_currLaws.setText(gLawsDesc);
	}
}
