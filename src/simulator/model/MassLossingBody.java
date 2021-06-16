package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body{

	private double lossFactor;
	private double lossFrequency;
	private double c_time;//contador de tiempo para la perdida de masa
	
	public MassLossingBody(String id, Vector p, Vector v, Vector a, double m, double freq, double fact) {
		super(id, p, v, a, m);
		lossFactor=fact;
		lossFrequency=freq;
		c_time= 0.0;
	}
	
	@Override
	void move(double t) {
		super.move(t);
		//DESPUES DE MOVERSE
		c_time+=t;
		if(lossFrequency <= c_time) {
			//pierde masa y contador a 0
			c_time=0.0;
			m=m*(1-lossFactor);
		}
	}
}
