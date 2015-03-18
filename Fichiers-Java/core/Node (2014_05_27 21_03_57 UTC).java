package core;

import java.util.ArrayList;

public class Node {

	// numero 
	private int num ;
	// coordonnees
	private float x ;
	private float y ;

	private ArrayList <Arc> liste_destinations ;
	
	public Node (int num, float x, float y) {
		this.num = num ;
		this.x = x;
		this.y = y;
		this.liste_destinations = new ArrayList <Arc> () ;
	}

	public int getNum() {
		return num ;
	}
	
	
	public float getX() {
		return x;
	}


	public float getY() {
		return y;
	}

	public Arc getArc (int indice) {
		return liste_destinations.get(indice) ;
	}
	
	public ArrayList<Arc> getArc () {
		return liste_destinations ;
	}

	
	public void addDestionation (Arc dest) {
		if (!this.liste_destinations.contains(dest))
			this.liste_destinations.add(dest) ;
	}

	
	public boolean equals(Node obj) {
	      return  (obj instanceof Node) &&
		  (((Node)obj).getX() == this.x) && (((Node)obj).getY() == this.y) &&
		  ((Node)obj).getArc().equals(liste_destinations);
	}
	
	
}