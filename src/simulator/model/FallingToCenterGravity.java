package simulator.model;

import java.util.List;

public class FallingToCenterGravity implements GravityLaws {

	private static final double G=-9.81;
	
	@Override
	public void apply(List<Body> bodies) {
		
		for(int i=0; i<bodies.size();i++) {	
			bodies.get(i).setAcceleration(bodies.get(i).getPosition().direction().scale(G));
		}
	}
	
	@Override
	public String toString() {
		return "Bodies fall to center.";
		
	}

}
