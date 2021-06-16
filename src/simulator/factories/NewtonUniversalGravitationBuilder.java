package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws>{
	private static String name = "nlug";
	private static String desc = "Newton law of Universal Gravitation";
	
	public NewtonUniversalGravitationBuilder() {
		super(name, desc);
	}
	
	@Override
	protected GravityLaws createTheInstance(JSONObject j) {
		return new NewtonUniversalGravitation();
	}

}