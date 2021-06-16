package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	private PhysicsSimulator _sim;
	private Factory<Body> _bodiesFactory;
	private Factory<GravityLaws> _lawsFactory;
	
	public Controller(PhysicsSimulator sim, Factory<Body> b, Factory<GravityLaws> g) {
		_sim=sim;
		_bodiesFactory=b;
		_lawsFactory=g;
	}
	
	public Factory<GravityLaws> getGravityLawsFactory(){
		return _lawsFactory;
	}
	
	public void setGravityLaws(JSONObject info) {
		_sim.setGravityLaws(_lawsFactory.createInstance(info));
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		for (int i = 0; i < bodies.length(); i++) 
			_sim.addBody(_bodiesFactory.createInstance(bodies.getJSONObject(i)));
	}
	
	public void run(int steps, OutputStream out) throws IOException {
		PrintStream p=(out==null)?null: new PrintStream(out);
		StringBuilder datos= new StringBuilder();
		datos.append("{\n \"states\": [\n");
		
		for(int i=0;i<steps;i++) {
			datos.append(_sim.toString());
			datos.append(",\n");
			_sim.advance();
		}
		//para almacenar el estado final de la simulacion 
		datos.append(_sim.toString());
		datos.append("\n]");
		datos.append("\n}");
		
		if(p==null) System.out.println(datos);
		else {
			p.println(datos);
			p.close();
		}
	}
	
	public void run(int n) {
		for(int i=0;i<n;i++) {
			_sim.advance();
		}
	}
	
	public void reset() {
		_sim.reset();
	}
	
	public void setDeltaTime(double dt) {
		_sim.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		_sim.addObserver(o);
	}
}
