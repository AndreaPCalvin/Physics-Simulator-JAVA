package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{
	private static String name = "basic";
	private static String desc = "Default Body";
	
	public BasicBodyBuilder() {
		super(name, desc);
	}
	
	@Override
	protected Body createTheInstance(JSONObject data) throws IllegalArgumentException {
		// devuelve objeto body extrayendo info de data
		String id = data.getString("id");
		
		double[] p = jsonArrayTodoubleArray(data.getJSONArray("pos"));
		Vector pVect = new Vector(p);
		
		double[] v = jsonArrayTodoubleArray(data.getJSONArray("vel"));
		Vector vVect = new Vector(v);
		
		Vector aVect = new Vector(vVect.dim());
		//la dim de todos los vectores debe ser la misma
		double m = data.getDouble("mass");
		
		if(m<0 || vVect.dim()!=pVect.dim()) throw new IllegalArgumentException();
		
		return new Body(id, pVect, vVect, aVect, m);
	}
	
	@Override
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id", "default body");
		data.put("pos", "body's position");
		data.put("vel", "body's velocity");
		data.put("mass", "body's mass");
		
		return data;
	}

}
