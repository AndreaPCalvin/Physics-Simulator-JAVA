package simulator.model;

import java.util.List;

public class NoGravity implements GravityLaws{
	
	@Override
	public void apply(List<Body> bodies) {
		// no hace nada	
	}

	@Override
	public String toString() {
		return "Bodies have no gravity.";
		
	}
}
