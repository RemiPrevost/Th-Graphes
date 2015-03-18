package core;

import base.Descripteur;

public class Arc {

	private Node destination ;
	private Descripteur descripteur ;
	private float longueur ; // en metre
	
	public Arc (Node dest, Descripteur descripteur, float longueur) {
		this.destination = dest ;
		this.descripteur = descripteur ;
		this.longueur = longueur ;
	}

	public Node getDestination() {
		return destination;
	}

	public Descripteur getDescripteur() {
		return descripteur;
	}

	public float getLongueur() {
		return longueur;
	}
	
	
}