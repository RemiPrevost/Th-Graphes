package core;

import java.awt.Color;
import java.util.ArrayList;

import core.Graphe.Mode;

import base.Dessin;

public class Chemin {
	private ArrayList<Node> liste_nodes;
	private float cout; // en temps (minutes) ou en distance (kilometres)
	
	public Chemin() {
		liste_nodes = new ArrayList<Node>();
		cout=0;
	}
	
	public float getCout(){
		return cout;
	}
	
	public int getSize() {
		return liste_nodes.size();
	}
	
	public void addNodeDeb (Node n, Dessin d) {
		if (n!= null) {
			liste_nodes.add(0, n) ;
			if (liste_nodes.size() > 1) 
				tracerChemin(d, Color.GREEN) ;
		}
	}
	
	public void addNode(Node n, Dessin d) {
		if (n != null) {
			liste_nodes.add(n);
			//calculCout() ;
			if (liste_nodes.size() > 1) 
				tracerChemin(d, Color.BLUE) ;
		}
	}
	
	// calcul le cout d'un chemin
	public void calculCout() {
		for (int i = 0 ; i<liste_nodes.size()-1 ; i++) {
			majCout(liste_nodes.get(i), liste_nodes.get(i+1)) ;
		}
	}
	
	public static float getCout(Node n1, Node n2) {
		float cout_min = 0;
		Arc arc_elu_min = null;
		ArrayList <Arc >liste_arcs = n1.getArc() ;
		for (Arc a : liste_arcs) {
			if (Graphe.getMode()==Mode.TEMPS) {
				if ((a.getDestination()).equals(n2)) {
				if (arc_elu_min == null) {
					arc_elu_min = a;
					cout_min = 60*(a.getLongueur())/(a.getDescripteur().getVitesseMax()*1000);
				}
				else
					if (60*a.getLongueur()/(a.getDescripteur().getVitesseMax()*1000) < cout_min) {
						cout_min =  60*a.getLongueur()/(a.getDescripteur().getVitesseMax()*1000);
						arc_elu_min =a ;
					}
				}
			}
			else {
				if ((a.getDestination()).equals(n2)) {
				if (arc_elu_min == null) {
					arc_elu_min = a;
					cout_min = a.getLongueur()/1000; // cout en kilometre
				}
				else
					if (a.getLongueur() < cout_min) {
						cout_min =  a.getLongueur()/1000; // cout en kilometre
						arc_elu_min =a ;
					}
				}
			}
			
		}
		return cout_min ;
	}
	
	
	
	// calcul le cout entre deux sommets
	private void majCout (Node n1, Node n2) {
		float cout_min = 0;
		Arc arc_elu_min = null;
		ArrayList <Arc >liste_arcs = n1.getArc() ;		
		for (Arc a : liste_arcs) {
			if (Graphe.getMode()==Mode.TEMPS) {
				if ((a.getDestination()).equals(n2)) {
				if (arc_elu_min == null) {
					arc_elu_min = a;
					cout_min = 60*(a.getLongueur())/(a.getDescripteur().getVitesseMax()*1000);
				}
				else
					if (60*a.getLongueur()/(a.getDescripteur().getVitesseMax()*1000) < cout_min) {
						cout_min =  60*a.getLongueur()/(a.getDescripteur().getVitesseMax()*1000);
						arc_elu_min =a ;
					}
				}
			}
			else {
				if ((a.getDestination()).equals(n2)) {
				if (arc_elu_min == null) {
					arc_elu_min = a;
					cout_min = a.getLongueur()/1000; // cout en kilometre
				}
				else
					if (a.getLongueur() < cout_min) {
						cout_min =  a.getLongueur()/1000; // cout en kilometre
						arc_elu_min =a ;
					}
				}
			}
			
		}
		this.cout += cout_min ;
	}
	
	
	
	// trace le chemin 
	public void tracerChemin(Dessin d, Color c) {
		for (int i = 0 ; i<liste_nodes.size()-1 ; i++) {
			majChemin(d, c, liste_nodes.get(i), liste_nodes.get(i+1)) ;
		}
	}
	
	// trace le chemin entre deux sommet
	private void majChemin(Dessin d, Color c, Node n1, Node n2) {
		float long1, long2, lat1, lat2 ;
		long1 = n1.getX() ;
		long2 = n2.getX() ;
		lat1 = n1.getY() ;
		lat2 = n2.getY() ;
		d.setColor(c);
		d.drawLine(long1, lat1, long2, lat2) ;
	}
	
	
	// trace le chemin entre deux sommet
	public static void dessineRoute(Dessin d, Color c, Node n1, Node n2) {
		float long1, long2, lat1, lat2 ;
		long1 = n1.getX() ;
		long2 = n2.getX() ;
		lat1 = n1.getY() ;
		lat2 = n2.getY() ;
		d.setColor(c);
		d.drawLine(long1, lat1, long2, lat2) ;
	}
	
}