package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{
	private static String name = "mlb";
	private static String desc = "Mass losing body";
	
	public MassLosingBodyBuilder() {
		super(name, desc);
	}
	
	@Override
	protected Body createTheInstance(JSONObject data) throws IllegalArgumentException{
		String id = data.getString("id");
		double[] p = jsonArrayTodoubleArray(data.getJSONArray("pos"));
		Vector pVect = new Vector(p);
		double[] v = jsonArrayTodoubleArray(data.getJSONArray("vel"));
		Vector vVect = new Vector(v);
		Vector aVect = new Vector(vVect.dim());
		//la dim de todos los vectores debe ser la misma
		double m = data.getDouble("mass");
		double freq=data.getDouble("freq");
		double factor=data.getDouble("factor");
		
		if(m<0 || vVect.dim()!=pVect.dim() || freq<0 || factor<0 || factor>1) 
			throw new IllegalArgumentException();
		
		return new MassLossingBody(id, pVect, vVect, aVect, m, freq, factor);
	}
	@Override
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id", "mass losing body");
		data.put("pos", "body's position");
		data.put("vel", "body's velocity");
		data.put("mass", "body's mass");
		data.put("freq", "frequency of losing mass");
		data.put("factor", "body's losing mass factor");
		
		return data;
	}
	
}
