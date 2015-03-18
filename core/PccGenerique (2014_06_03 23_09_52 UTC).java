package core;

import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;

import core.Graphe.Mode;

public class PccGenerique extends Algo {

	// Numero du sommet origine
	protected int origine;

	private Label tab_labels[] = new Label[super.graphe.getNbNodes()];

	// couleur pour le parcours de Dijkstra
	protected Color couleur = Color.BLUE ;

	// boolean indiquant s'il s'agit d'un pieton ou non (une voiture)
	private boolean pieton;

	// Fonctions pour le chronometre
	static long chrono = 0 ;

	public PccGenerique(Graphe gr, PrintStream fichierSortie, int origine, boolean pieton) {
		super(gr, fichierSortie);
		this.origine = origine;
		this.pieton = pieton ;
	}

	public Label[] getTabLabels() {
		return tab_labels ;
	}
	
	private void init() {

		System.out.println("Run PCC de :" + origine
				+ " vers tous");

		// initialisation du tableau de labels
		for (int i = 0; i < tab_labels.length; i++) {
			tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
		}
	}

	public void run() {
		int compteur; // sert a compter les sommets parcourus

		Go_Chrono() ;
		this.init();

		tab_labels[origine].setPere(origine) ;
		tab_labels[origine].setCout(0) ;

		// insertion de l'origine dans le tas
		BinaryHeap<Label> tas = new BinaryHeap<Label>();
		tas.insert(tab_labels[origine]);
		compteur = 1;

		// iterations
		Label x ;
		ArrayList<Arc> liste_succ_arcs ;
		while (!tas.isEmpty()) {
			x = tas.deleteMin();
			x.setMarquage();
			liste_succ_arcs = super.graphe.getTabNode()[x.getCourant().getNum()].getArc();

			// parcours de la liste des successeurs de x
			for (Arc y : liste_succ_arcs) {
				if (!tab_labels[y.getDestination().getNum()].isMarquage()) {
					// on calcul le cout entre x et y
					Chemin.dessineRoute(this.graphe.getDessin(), couleur, x.getCourant(), y.getDestination());
					double cout = Chemin.getCout(x.getCourant(), y.getDestination(), this.pieton) ;
					if (tab_labels[y.getDestination().getNum()].getCout() > x.getCout() + cout) {
						// mis a jour du label associe a y
						tab_labels[y.getDestination().getNum()].setCout(x.getCout() + cout);
						tab_labels[y.getDestination().getNum()].setPere(x.getCourant().getNum());
						if (tas.update(tab_labels[y.getDestination().getNum()]))
							compteur++;
					}
				}
			}
		}
		if (tas.isEmpty()) {
			System.out.println("Aucun chemin trouve.") ;
		}
		System.out.println("Nombre d'elements dans tas : " + compteur) ;
		Stop_Chrono() ;

	}
	
	public Chemin buildChemin(Node destination, boolean pieton) {
		Chemin chemin = new Chemin();
		int index = destination.getNum();

		// ajout de la destination dans le chemin 
		chemin.addNodeDeb(tab_labels[index].getCourant(), this.graphe.getDessin());

		// construction du chemin
		while (index != origine) {
			index = tab_labels[index].getPere();
			chemin.addNodeDeb(tab_labels[index].getCourant(), this.graphe.getDessin());
		}
		
		chemin.calculCout(pieton) ;
		if (Graphe.getMode()==Mode.DISTANCE)
			System.out.println("\nCout du chemin du Sommet "+ this.origine+ " au sommet " + destination.getNum() +" : " +chemin.getCout()/1000+" kilometres");
		else 
			System.out.println("\nCout du chemin du Sommet "+ this.origine+ " au sommet " + destination.getNum() +" : " +chemin.getCout()+" minutes");
		System.out.println("nb sommet dans chemin : "+chemin.getSize()) ;
		return chemin ;
	}
	
	// Lancement du chrono
	static void Go_Chrono() {
		chrono = java.lang.System.currentTimeMillis() ;
	}

	// Arret du chrono
	static void Stop_Chrono() {
		long chrono2 = java.lang.System.currentTimeMillis() ;
		long temps = chrono2 - chrono ;
		System.out.println("Temps ecoule = " + temps + " ms\n") ;
	} 
}