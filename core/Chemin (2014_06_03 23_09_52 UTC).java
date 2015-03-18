package core;

import java.awt.Color;
import java.util.ArrayList;

import core.Graphe.Mode;

import base.Dessin;

public class Chemin {
	private ArrayList<Node> liste_nodes;
	private double cout; // en temps (minutes) ou en distance (metres)
	
	public Chemin() {
		liste_nodes = new ArrayList<Node>();
		cout=0;
	}
	
	public double getCout(){
		return cout;
	}
	
	public int getSize() {
		return liste_nodes.size();
	}
	
	// ajoute un sommet en debut de liste
	public void addNodeDeb (Node n, Dessin d) {
		if (n!= null) {
			liste_nodes.add(0, n) ;
			if (liste_nodes.size() > 1) 
				tracerChemin(d, Color.GREEN) ;
		}
	}
	
	// ajoute un sommet en fin de liste
	public void addNode(Node n, Dessin d) {
		if (n != null) {
			liste_nodes.add(n);
			if (liste_nodes.size() > 1) 
				tracerChemin(d, Color.BLUE) ;
		}
	}
	
	// cree le chemin inverse
	public Chemin CheminInv (Dessin d) {
		Chemin chemin_inv = new Chemin() ;
		for (int i = liste_nodes.size()-1 ; i>0 ; i--) {
			chemin_inv.addNode(liste_nodes.get(i), d) ;
		}
		return chemin_inv ;
	}
	
	// calcul le cout d'un chemin complet
	public void calculCout(boolean pieton) {
		for (int i = 0 ; i<liste_nodes.size()-1 ; i++) {
			this.cout+= getCout(liste_nodes.get(i), liste_nodes.get(i+1), pieton) ;
		}
	}
	
	// calcul le cout entre deux sommets 
	public static double getCout(Node n1, Node n2, boolean pieton) {
		if (pieton)
			return calculCoutP(n1, n2);
		else
			return calculCoutV(n1, n2);
	}
	
	// calcul le cout entre deux sommets pour un pieton
	public static double calculCoutP(Node n1, Node n2) {
		double cout_min = Double.MAX_VALUE ;
		int vitessePieton = 30 ;
		Arc arc_elu_min = null;
		ArrayList <Arc >liste_arcs = n1.getArc() ;
		for (Arc a : liste_arcs) {
			if (a.getDescripteur().vitesseMax()< 110) {
				if (Graphe.getMode()==Mode.TEMPS) {
					if ((a.getDestination()).equals(n2)) {
						if (arc_elu_min == null) {
							arc_elu_min = a;
							cout_min = 60*(a.getLongueur())/(vitessePieton*1000);
						}
						else
							if (60*a.getLongueur()/(vitessePieton*1000) < cout_min) {
								cout_min =  60*a.getLongueur()/(vitessePieton*1000);
								arc_elu_min =a ;
							}
					}
				}
				else {
					if ((a.getDestination()).equals(n2)) {
						if (arc_elu_min == null) {
							arc_elu_min = a;
							cout_min = a.getLongueur();
						}
						else
							if (a.getLongueur() < cout_min) {
								cout_min =  a.getLongueur();
								arc_elu_min =a ;
							}
					}
				}
			}
		}
		return cout_min ;
	}
	
	// calcul le cout entre deux sommets pour une voiture
	public static double calculCoutV(Node n1, Node n2) {
		double cout_min = 0 ;
		Arc arc_elu_min = null;
		ArrayList <Arc >liste_arcs = n1.getArc() ;
		for (Arc a : liste_arcs) {
			if (Graphe.getMode()==Mode.TEMPS) {
				if ((a.getDestination().getNum()==n2.getNum())) {
					if (arc_elu_min == null) {
						arc_elu_min = a;
						cout_min = 60*(a.getLongueur())/(a.getDescripteur().vitesseMax()*1000);
					}
					else
						if (60*a.getLongueur()/(a.getDescripteur().vitesseMax()*1000) < cout_min) {
							cout_min =  60*a.getLongueur()/(a.getDescripteur().vitesseMax()*1000);
							arc_elu_min =a ;
						}
				}
			}
			else {
				if (a.getDestination().getNum()==n2.getNum()) {
					if (arc_elu_min == null) {
						arc_elu_min = a;
						cout_min = a.getLongueur();
					}
					else
						if (a.getLongueur() < cout_min) {
							cout_min =  a.getLongueur();
							arc_elu_min =a ;
						}
				}
			}
		}
		return cout_min;
	}
	

	
	
	
	// trace le chemin 
	public void tracerChemin(Dessin d, Color c) {
		for (int i = 0 ; i<liste_nodes.size()-1 ; i++) {
			dessineRoute(d, c, liste_nodes.get(i), liste_nodes.get(i+1)) ;
		}
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
	
	// trace un point
	public static void dessineSommet(Dessin d, Color c, Node n) {
		d.setColor(c);
		d.drawPoint (n.getX(), n.getY(), 20) ;
	}
	
}