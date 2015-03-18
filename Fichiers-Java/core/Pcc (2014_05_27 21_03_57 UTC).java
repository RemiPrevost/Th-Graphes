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
	
	protected Color couleur = Color.BLUE ;

	
	protected Label tab_labels[] = new Label[super.graphe.getNbNodes()];
	
	public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg);

		this.zoneOrigine = gr.getZone();
		this.origine = readarg.lireInt("Numero du sommet d'origine ? ");

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone();
		this.destination = readarg.lireInt("Numero du sommet destination ? ");
		
		int mode = readarg.lireInt("Cout en temps(0) ou distance(1) ? (par défaut c'est le cout en temps)") ;
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
		int compteur; // sert a compter les sommets parcourus
		
		if (this.zoneOrigine == this.zoneDestination)
			// les deux sommets sont dans la meme zone
		{
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
			while (!tab_labels[destination].isMarquage() && !tas.isEmpty()) {
				x = tas.deleteMin();
				x.setMarquage();
				liste_succ_arcs = super.graphe.getTabNode()[x.getCourant().getNum()].getArc();

				// parcours de la liste des successeurs de x
				for (Arc y : liste_succ_arcs) {
					if (!tab_labels[y.getDestination().getNum()].isMarquage()) {
						// on calcul le cout entre x et y
						Chemin.dessineRoute(this.graphe.getDessin(), couleur, x.getCourant(), y.getDestination());
						float cout = Chemin.getCout(x.getCourant(), y.getDestination()) ;
						if (tab_labels[y.getDestination().getNum()].getCout() > x.getCout() + cout) {
							// mis a jour du label associe a y
							tab_labels[y.getDestination().getNum()].setCout(x.getCout() + cout);
							tab_labels[y.getDestination().getNum()].setPere(x.getCourant().getNum());
							if (tas.insert(tab_labels[y.getDestination().getNum()]))
								compteur++;
						}
					}
				}
			}
			buildChemin();
			this.afficherResultats(compteur) ;
		}

	}

	
	public void afficherResultats (int compteur) {
		// afficher le cout du chemin
		System.out.println("Cout du chemin par methode Dijkstra Standard : "	+ tab_labels[destination].getCout());
		// afficher le nombre max d'element dans le tas
		System.out.println("Nb max d'element : " + compteur);
	}
	
	public void buildChemin() {
		Chemin chemin = new Chemin();
		int index = destination;

		chemin.addNodeDeb(tab_labels[destination].getCourant(),
				this.graphe.getDessin());

		while (index != origine) {
			index = tab_labels[index].getPere();
			chemin.addNodeDeb(tab_labels[index].getCourant(),
					this.graphe.getDessin());
		}
		
		chemin.calculCout() ;
		
		System.out.println("index :" +index) ;
		System.out.println("cout chemin par methode chemin: "+chemin.getCout());
		System.out.println("nb sommet dans chemin : "+chemin.getSize()) ;
		System.out.println("vit max: " + this.graphe.getVitMax()) ;
	}

}