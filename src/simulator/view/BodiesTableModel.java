package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	private static String[] columnNames = {"Id", "Mass", "Position", "Velocity", "Acceleration"};
	private List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}
	
	@Override
	public boolean isCellEditable(int row, int colum) {
		return false;
	}

	@Override
	public int getRowCount() {
		return _bodies==null ? 0 : _bodies.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		switch (columnIndex){
		case 0:
			o = _bodies.get(rowIndex).getId();
			break;
		case 1:
			o = _bodies.get(rowIndex).getMass();
			break;
		case 2:
			o = _bodies.get(rowIndex).getPosition();
			break;
		case 3:
			o = _bodies.get(rowIndex).getVelocity();
			break;
		case 4:
			o = _bodies.get(rowIndex).getAcceleration();
			break;
			default:
				assert(false);
		}
		return o;
	}
	
	// SimulatorObserver methods

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies= new ArrayList<>(bodies);
		fireTableStructureChanged();
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies= new ArrayList<>(bodies);
		fireTableStructureChanged();
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies= new ArrayList<>(bodies);
		fireTableStructureChanged();
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies= new ArrayList<>(bodies);
		fireTableStructureChanged();
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
