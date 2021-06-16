package simulator.model;

import simulator.misc.Vector;

public class Body {
	
	protected String id;
	protected Vector pos;//posicion
	protected Vector v;//velocidad
	protected Vector a;//aceleracion
	protected double m;//masa
	
	public Body(String id, Vector p, Vector v, Vector a, double m) {
		this.id=id;
		pos=p;
		this.v=v;
		this.a=a;
		this.m=m;
	}
	
	public String getId() {// devuelve el identificador del cuerpo.
		return id;
	}
	
	public Vector getVelocity() {
		return new Vector(v);//devuelve una copia del vector de velocidad.
	}
	//devuelve una copia del vector de aceleración.
	public Vector getAcceleration() {
		return new Vector(a);
	}
	// devuelve una copia del vector de posición.
	public Vector getPosition() {
		return new Vector(pos);
	}
	// devuelve la masa del cuerpo.
	public double getMass() {
		return m;	
	}
	// hace una copia de v y se la asigna al vector de velocidad.
	void setVelocity(Vector vel) {
		this.v= new Vector(vel);
	}
	//: hace una copia de a y se la asigna al vector de aceleración.
	void setAcceleration(Vector acc) {
		this.a=new Vector(acc);
	}
	// hace una copia de p y se la asigna al vector de posición.
	void setPosition(Vector p) {
		this.pos=new Vector(p);
	}
	// mueve el cuerpo durante t segundos utilizando los atributos del mismo.
	void move(double t) {
		this.pos= pos.plus(v.scale(t).plus(a.scale(t*t).scale(0.5)));
		this.v= v.plus(a.scale(t));
	}
	// devuelve un string con la información del cuerpo en formato JSON
	public String toString() {
		
		String s ="{ " + " \"id\": " + '\"' + this.id + '\"' +
				", " + "\"mass\": " + this.m +
				", "+"\"pos\": " + this.pos.toString() +
				", "+"\"vel\": " + this.v.toString() +
				", "+"\"acc\": " + this.a.toString() +
				" }";
		return s;
	}
}
