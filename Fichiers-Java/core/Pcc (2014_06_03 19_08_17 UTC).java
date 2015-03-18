package core;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;

import base.Readarg;

import core.Graphe.Mode;
public class Pcc extends Algo {

	// Numero des sommets origine et destination
	protected int zoneOrigine;
	protected int origine;

	protected int zoneDestination;
	protected int destination;
	
	// couleur pour le parcours de Dijkstra
	protected Color couleur = Color.BLUE ;

	protected Label tab_labels[] = new Label[super.graphe.getNbNodes()];
	
	// Fonctions pour le chronometre
	static long chrono = 0 ;

	
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg);
		this.zoneOrigine = gr.getZone();
		this.origine = readarg.lireInt("Numero du sommet d'origine ? ");

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone();
		this.destination = readarg.lireInt("Numero du sommet destination ? ");
		
		int mode = readarg.lireInt("Cout en temps(0) ou distance(1) ? (par défaut c'est le cout en temps) ") ;
		
		if (mode == 1) 
			Graphe.setMode(Mode.DISTANCE);
		else
			Graphe.setMode(Mode.TEMPS) ;	
	}
	
	protected void init() {
		
		System.out.println("Run PCC de " + zoneOrigine + ":" + origine
				+ " vers " + zoneDestination + ":" + destination);

		// initialisation du tableau de labels
		for (int i = 0; i < tab_labels.length; i++) {
			tab_labels[i] = new Label(super.graphe.getTabNode()[i]);
		}
	}

	public void run() {
		int max_tas; // sert a compter les sommets places dans le tas
		int max_elements ; // sert a compter les sommets explores
		
		Go_Chrono() ;
		
		if (origine >=0 && origine <= this.graphe.getNbNodes() && destination >=0 && destination <= this.graphe.getNbNodes())
		{
			this.init();
			
			tab_labels[origine].setPere(origine) ;
			tab_labels[origine].setCout(0) ;
			
			// insertion de l'origine dans le tas
			BinaryHeap<Label> tas = new BinaryHeap<Label>();
			tas.insert(tab_labels[origine]);
			max_tas = 1 ;
			max_elements = 1 ;
		
			// iterations
			Label x ;
			ArrayList<Arc> liste_succ_arcs ;
			while (!tab_labels[destination].isMarquage() && !tas.isEmpty()) {
				x = tas.deleteMin();
				x.setMarquage(); 
				
				liste_succ_arcs = super.graphe.getTabNode()[x.getCourant().getNum()].getArc();

				// parcours de la liste des successeurs de x
				for (Arc y : liste_succ_arcs) {
					if (!tab_labels[y.getDestination().getNum()].isMarquage()) {
						// on calcul le cout entre x et y	
						double cout = Chemin.getCout(x.getCourant(), y.getDestination(), false) ;
						if (tab_labels[y.getDestination().getNum()].getCout() > x.getCout() + cout) {
							// mis a jour du label associe a y
							tab_labels[y.getDestination().getNum()].setCout(x.getCout() + cout);
							tab_labels[y.getDestination().getNum()].setPere(x.getCourant().getNum());
							if (tas.update(tab_labels[y.getDestination().getNum()])) {
								max_elements ++ ;
								if (max_tas < tas.size())
									max_tas = tas.size() ;
							}
						}
					}
				}
			}
			if (tas.isEmpty()) {
				System.out.println("Aucun chemin trouve.") ;
				System.out.println("Nb max d'element dans le tas : " + max_tas + "\n");
				System.out.println("Nb max d'element explores : " + max_elements + "\n");
			}
			else {
				buildChemin();
				this.afficherResultats(max_tas, max_elements) ;
			}
		}
		else 
			System.out.println("L'origine ou la destination n'existe pas.") ;
		Stop_Chrono() ;
	}

	// affichage des Resultats de Dijkstra Standard
	public void afficherResultats (int max_tas, int max_elements) {
		// afficher le cout du chemin
		if (Graphe.getMode()==Mode.DISTANCE)
			System.out.println("\ncout chemin par methode Dijkstra Standard: "+tab_labels[destination].getCout()/1000+" kilometres");
		else 
			System.out.println("\ncout chemin par methode Dijkstra Standard : "+tab_labels[destination].getCout()+" minutes");
		// afficher le nombre max d'element dans le tas et le nombre max d'elements explores
		System.out.println("Nb max d'element dans le tas : " + max_tas + "\n");
		System.out.println("Nb max d'element explores : " + max_elements + "\n");
	}
	
	// construction du plus court chemin
	// on part de la destination et on passe par tous ses sommets "peres" jusqu'a l'origine
	public void buildChemin() {
		Chemin chemin = new Chemin();
		int index = destination;

		chemin.addNodeDeb(tab_labels[destination].getCourant(), this.graphe.getDessin());

		while (index != origine) {
			index = tab_labels[index].getPere();
			chemin.addNodeDeb(tab_labels[index].getCourant(), this.graphe.getDessin());
		}
		
		chemin.calculCout(false) ;
		if (Graphe.getMode()==Mode.DISTANCE)
			System.out.println("\ncout chemin par methode chemin: "+chemin.getCout()/1000+" kilometres");
		else 
			System.out.println("\ncout chemin par methode chemin: "+chemin.getCout()+" minutes");
		System.out.println("nb sommet dans chemin : "+chemin.getSize()) ;
		System.out.println("vit max: " + this.graphe.getVitMax()) ;
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