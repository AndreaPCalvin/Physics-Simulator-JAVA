package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingtoCenterGravityBuilder extends Builder<GravityLaws>{
	private static String name = "ftcg";
	private static String desc = "Falling to center gravitation law";
	
	public FallingtoCenterGravityBuilder() {
		super(name, desc);
	}
	
	@Override
	protected GravityLaws createTheInstance(JSONObject j) {
		return new FallingToCenterGravity();
	}

}
