package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

	private GravityLaws _gravityLaws;
	private List<Body> _bodies; 
	private double _dt;//incremento de tiempo/tiempo actual
	private double _time;//numero de pasos
	private List<SimulatorObserver> _observers;
	
	public PhysicsSimulator(GravityLaws ley, double tiempo) throws IllegalArgumentException {
		if(ley==null || tiempo<=0.0)throw new IllegalArgumentException();//si t=0, no se avanzaria en simulacion
		_gravityLaws=ley;
		_time=tiempo;
		_bodies= new ArrayList<Body>();
		_observers= new ArrayList<SimulatorObserver>();
		_dt=0.0;
	}
	
	public void advance() {
		//aplicar leyes
		_gravityLaws.apply(_bodies);
		//mover cuerpos
		for(Body b: _bodies) {
			b.move(_time);
		}
		//incrementar tiempo
		_dt+=_time;
		
		for(SimulatorObserver obs: _observers) {
			obs.onAdvance(_bodies, _time);
		}
	}
	public void addBody(Body b) {
		for(Body i: _bodies) {
			if(i.id.equals(b.id)) throw new IllegalArgumentException();
		}
		_bodies.add(b);
	
		for(SimulatorObserver obs: _observers) {
			obs.onBodyAdded(_bodies, b);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder datos= new StringBuilder();
		datos.append("{ \"time\": ");
		datos.append(_dt);
		datos.append(", ");
		datos.append("\"bodies\": [ ");
		for(int i=0;i<_bodies.size();i++) {
			datos.append( _bodies.get(i).toString());
			if(i<_bodies.size()-1)
				datos.append(", ");
		}
		datos.append(" ] }");
		return datos.toString();
	}
	
	public void reset() {// vacía la lista de cuerpos y pone el tiempo a 0,0
		_dt=0.0;
		//_time=0.0;
		_bodies.clear();
		
		for(SimulatorObserver obs: _observers) {
			obs.onReset(_bodies, _time, _time, _gravityLaws.toString());
		}
	}
	
	public void setDeltaTime(double dt){
		if(dt<=0.0)
			throw new IllegalArgumentException();
		else{
			_dt=dt;
			
			for(SimulatorObserver obs: _observers) {
				obs.onDeltaTimeChanged(_dt);
			}
		}
			
	}
	
	public void setGravityLaws(GravityLaws law){
		if(law==null)
			throw new IllegalArgumentException();
		else {
			_gravityLaws=law;
			for(SimulatorObserver obs: _observers) {
				obs.onGravityLawChanged(_gravityLaws.toString());
			}
		}
	}
	
	public void addObserver(SimulatorObserver o) {
		for(SimulatorObserver i: _observers) {
			if(i.equals(o)) throw new IllegalArgumentException();
		}
		_observers.add(o);
		o.onRegister(_bodies, _time, _dt, _gravityLaws.toString());
	}
}
