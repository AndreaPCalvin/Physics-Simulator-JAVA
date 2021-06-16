package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {
    private static final double G = 6.67E-11;
        
    @Override
    public void apply(List<Body> bodies) {
        if (!bodies.isEmpty()) {
            double a, b, f;
            for (int i = 0; i < bodies.size(); i++) {
                
                Vector Ftotal = new Vector(bodies.get(i).getAcceleration().dim());
                // dimension n
                
                if (bodies.get(i).getMass() != 0.0) {
                    for (int j = 0; j < bodies.size(); j++) {
                        if (j != i) {
                            // f=a/b^2
                            a = G * bodies.get(j).getMass();// masa de i simplificada
                            b = bodies.get(j).getPosition().distanceTo(bodies.get(i).getPosition());
                            f = a / (b * b);
                            // llevar la cuenta de las fuerzas
                            Vector unitario = bodies.get(j).getPosition().minus(bodies.get(i).getPosition()).direction();
                            Ftotal = Ftotal.plus(unitario.scale(f));
                        }
                        // con la fuerza, fijar la aceleracion del cuerpo i
                        bodies.get(i).setAcceleration(Ftotal);
                    } // finfor1
                } else {
                    // v y a son 0
                    int n=bodies.get(i).getAcceleration().dim();
                    //la dim de la velocidad y la aceleracion es la misma
                    bodies.get(i).setAcceleration(new Vector(n));
                    bodies.get(i).setVelocity(new Vector(n));
                }
            } // finfor2
        }
    }
    
    @Override
	public String toString() {
		return "Bodies follow Newton Universal Gravity Law.";
		
	}
}
