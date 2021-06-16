package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws>{
	private static String name = "ng";
	private static String desc = "No gravity";
	
	public NoGravityBuilder() {
		super(name, desc);
	}
	
	@Override
	protected GravityLaws createTheInstance(JSONObject j) {
		return new NoGravity();
	}

}
